package ba.tiket.scores;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import ba.tiket.db.DatabaseStatements;
import ba.tiket.db.MySqlProvider;
import ba.tiket.model.Sport;
import ba.tiket.util.CacheManager;
import ba.tiket.util.RssParser;
import ba.tiket.util.XmlParser;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class GetScores implements Job, InterruptableJob {
	
	private Logger logger=LogManager.getLogger(GetScores.class);
	AtomicReference<Thread> runningThread = new AtomicReference<Thread>();
	AtomicBoolean stopFlag = new AtomicBoolean(false);
	
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		this.runningThread.set(Thread.currentThread());
		
		if(XmlParser.stopApplication())
		{
			try {
				context.getScheduler().shutdown();
				return;
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		XmlReader reader=null;
		List<Sport> sports=XmlParser.getSports();
		for(int j=0; j<sports.size(); j++)
		{
			String rssUrl="";
			if(sports.get(j).getCountryId().isEmpty())
			{
				rssUrl= "http://rss.betfair.com/RSS.aspx?format=rss&sportID="+sports.get(j).getSportId();
			}
			else
			{
				rssUrl="http://rss.betfair.com/RSS.aspx?format=rss&sportID="+sports.get(j).getSportId()+"&countryID="+sports.get(j).getCountryId();

			}
	
			
			CacheManager cache=CacheManager.getInstance();
			try {
				
				
				URL url = new URL(rssUrl);
				reader = new XmlReader(url);
			
				SyndFeed feed = new SyndFeedInput().build(reader);
				int counter=0;

				for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
					SyndEntry entry = (SyndEntry) i.next();
					
					if (!entry.getDescription().getValue().contains("Any Unquoted")) //samo gotovi rezultati
					{
						if(cache.get(RssParser.GetSportId(entry.getLink())+"|"+RssParser.GetMarketId(entry.getLink())+"|"+RssParser.GetCountryId(rssUrl))==null)
						{
							String[] params={RssParser.GetSportId(entry.getLink()),
									RssParser.GetMarketId(entry.getLink()), RssParser.GetCountryId(rssUrl),
									entry.getTitle().trim(), RssParser.GetResult(entry.getDescription().getValue())};
							MySqlProvider.getInstance().prepareGroupInsert(DatabaseStatements.InsertNewScores,params);
							
							cache.put(RssParser.GetSportId(entry.getLink())+"|"+RssParser.GetMarketId(entry.getLink())+"|"+RssParser.GetCountryId(rssUrl), Calendar.getInstance());
							counter++;
							
							if((counter%1000)==0) //blokovi po 1000
							{
								MySqlProvider.getInstance().executeGroupInsert();
								
								System.out.println("Uneseno redova:"+counter);
								counter=0;
	
							}
						}
						else
						{
							Calendar cal=(Calendar)cache.get(RssParser.GetSportId(entry.getLink())+"|"+RssParser.GetMarketId(entry.getLink())+"|"+RssParser.GetCountryId(rssUrl));
							if(compareCalendars(cal))
							{
								cache.clear(RssParser.GetSportId(entry.getLink())+"|"+RssParser.GetMarketId(entry.getLink())+"|"+RssParser.GetCountryId(rssUrl));
							}
						}
						
					}
				}
				MySqlProvider.getInstance().executeGroupInsert(); //zavrsi one koji su ostali
				//MySqlProvider.getInstance().dispose();
				
				System.out.println("Uneseno redova:"+counter);
				
				
			} catch (MalformedURLException e) {
				logger.error("URL nije ispravan,"+rssUrl+" | "+e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.toString());
				
			} finally {
				
				if (reader != null)
					try {
						reader.close();
						MySqlProvider.getInstance().dispose();
							
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
			}
		}
	

	}
	
	private boolean compareCalendars(Calendar cal)
	{
		Calendar now= Calendar.getInstance();
		if(now.HOUR_OF_DAY>cal.HOUR_OF_DAY && now.DAY_OF_YEAR>cal.DAY_OF_YEAR)
		{
			return true;
		}
		else if(Calendar.HOUR_OF_DAY>cal.HOUR_OF_DAY && now.DAY_OF_YEAR==1 && cal.DAY_OF_MONTH==31 && cal.DAY_OF_MONTH==12)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void interrupt() throws UnableToInterruptJobException {
		
		Thread thread = runningThread.getAndSet(null);
        if (thread != null)
            thread.interrupt();
       
	}

}

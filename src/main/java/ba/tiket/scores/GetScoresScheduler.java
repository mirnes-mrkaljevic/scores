package ba.tiket.scores;

import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import ba.tiket.model.Sport;
import ba.tiket.util.Configuration;
import ba.tiket.util.XmlParser;

public class GetScoresScheduler {
	
	private static Logger logger=LogManager.getLogger(GetScoresScheduler.class);
	
	public static void fireScheduler()
	{
		JobDetail job = new JobDetail();
		job.setName("GetScoresJob");
		job.setJobClass(GetScores.class);
		
		
		
		SimpleTrigger trigger = new SimpleTrigger();
		trigger.setName("GetScoresTrigger");
		trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		trigger.setRepeatInterval(Configuration.getSchedulerTime());
	
		
		Scheduler scheduler=null;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			
			scheduler.scheduleJob(job, trigger);
			
		} catch (SchedulerException e) {
			logger.error(e.getMessage());
			
			
		}
		
	}

}

package ba.tiket.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


import ba.tiket.model.Sport;

public class XmlParser {
	
	private static Logger logger=LogManager.getLogger(XmlParser.class);
	
	public static boolean stopApplication()
	{
		 SAXBuilder builder = new SAXBuilder();
		  
		  
		  try {
			  File xmlFile = new File("app_params.xml");
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("param");
			int stop=Integer.parseInt(((Element)list.get(0)).getChildText("stop").toString());		
			return stop==0?false:true;

		  } catch (IOException io) {
			System.out.println(io.getMessage());
			//logger.error(io.getMessage());
			return false;
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
			//logger.error(jdomex.getMessage());
			return false;
		  }
	}
	
	public static List<Sport> getSports()
	{
		  SAXBuilder builder = new SAXBuilder();
		  List<Sport> sports=new ArrayList<Sport>();
		  
		  try {
			  File xmlFile = new File("requested_sports.xml");
			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			List list = rootNode.getChildren("item");
			
			
			for (int i = 0; i < list.size(); i++) {
	 
			   Element node = (Element) list.get(i);
			   Sport sport=new Sport();
			   sport.setSportId(node.getChildText("sportId"));
			   sport.setSportName(node.getChildText("sportName"));
			   sport.setCountryId(node.getChildText("countryId"));
			   
			   /*System.out.println("sportId : " + node.getChildText("sportId"));
			   System.out.println("sportName : " + node.getChildText("sportName"));
			   System.out.println("countryId: " + node.getChildText("countryId"));*/
			   
			   sports.add(sport);
			}
		
			return sports;
	 
		  } catch (IOException io) {
			System.out.println(io.getMessage());
			//logger.error(io.getMessage());
			return sports;
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
			//logger.error(jdomex.getMessage());
			return sports;
		  }
		
	}

}

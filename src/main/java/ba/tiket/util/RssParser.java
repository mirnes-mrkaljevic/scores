package ba.tiket.util;

public class RssParser {

	private static String findValue(String key, String url)
	{
		String qeryString=url.substring(url.indexOf("?"));
		String[] key_value_pair=qeryString.split("&");
		for(int i=0; i<key_value_pair.length; i++)
		{
			if(key_value_pair[i].contains(key))
			{
				String[] pair=key_value_pair[i].split("=");
				return pair[1];
			}
		}
		return null;
	}
	
	public static String GetSportId(String link)
	{
		return findValue("sportID",link);
	}
	
	public static String GetMarketId(String link)
	{
		return findValue("marketID",link);
	}
	
	public static String GetCountryId(String link)
	{
		return findValue("countryID",link);
	}
	
	public static String GetResult(String description)
	{
		return description.substring(description.indexOf("Winner(s):")+"Winner(s):".length()).trim();
	}
}

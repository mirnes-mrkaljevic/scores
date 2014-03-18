package ba.tiket.db;

public class DatabaseStatements {
	
	public static String InsertNewScores="INSERT INTO event_score (SPORT_ID, MARKET_ID, " +
			"COUNTRY_ID, TITLE, RESULT) VALUES (?, ?, ?, ?, ?)";
	
	public static String GetSports="SELECT ID, NAME, COUNTRY_ID  FROM sport";

}

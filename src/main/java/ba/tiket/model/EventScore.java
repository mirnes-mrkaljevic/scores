package ba.tiket.model;

public class EventScore {
	
	private int sportID;
	private int marketID;
	private Integer countryId;
	private String title;
	private String result;
	
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	public int getSportID() {
		return sportID;
	}
	public void setSportID(int sportID) {
		this.sportID = sportID;
	}
	public int getMarketID() {
		return marketID;
	}
	public void setMarketID(int marketID) {
		this.marketID = marketID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	

}

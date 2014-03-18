package ba.tiket.model;

public class Sport {
	
	private String sportId;
	private String sportName;
	private String countryId;
	
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getSportId() {
		return sportId;
	}
	public void setSportId(String string) {
		this.sportId = string;
	}
	public String getSportName() {
		return sportName;
	}
	public void setSportName(String sportName) {
		this.sportName = sportName;
	}
	

}

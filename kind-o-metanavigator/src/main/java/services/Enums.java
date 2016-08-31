package services;

public enum Enums {
	
	HTTP_ERROR("httperror"),
	TIMEOUT_ERROR("Connection timed out"),
	LINK_CLASS("wsOnClick");
	
	public final String property;
	Enums(String prop){
		this.property = prop;
	}
	
	public String getProp(){
		return this.property;
	}
}

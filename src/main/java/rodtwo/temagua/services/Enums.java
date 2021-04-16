package rodtwo.temagua.services;

public enum Enums {
	
	HTTP_ERROR("httperror"),
	TIMEOUT_ERROR("Connection timed out"),
	LINK_CLASS("wsOnClick"),
	TOKEN_RAPI("202cbd4e624c53adc2c8f60b04723314cae26fbc"),
	ENDPOINT_RAPI("https://readability.com/api/content/v1/parser");
	public final String property;
	Enums(String prop){
		this.property = prop;
	}
	
	public String getProp(){
		return this.property;
	}
}

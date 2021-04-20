package rodtwo.temagua.services.bean;

public class HtmlStringBuilderHelper {
	
	private StringBuilder sb;
	
	public HtmlStringBuilderHelper() {
		this.sb = new StringBuilder();
	}

	/**
	 * Pra usar métodos não implementados pelo helper
	 */
	public StringBuilder getStringBuilder() {
        return sb;
    }
	
	public StringBuilder append(String str) {
        sb.append(str);
        return sb;
    }
	
	public StringBuilder appendLn(String str) {
        sb.append(str);
        sb.append(System.lineSeparator());
        return sb;
    }
	
	@Override
	public String toString() {
		return sb.toString();
	}
}

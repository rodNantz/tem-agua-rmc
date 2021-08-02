package rodtwo.temagua.util;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.util.MultiMap;

public class MyLog {
	
	MultiMap<String> map;
	static boolean[] print = new boolean[3];
	private static MyLog logInstance; 
	static final Group defaultSeverity = getDefaultSeverity();
	
	public enum Group {
		INFO("INFO"),
		WARNING("WARN"),
		ERROR("ERROR"),
		IMPORTANT("IMPORTANT");		// same severity as error
		public final String property;
		
		Group(String prop){
			this.property = prop;
		}
		
		public static Group getGroup(String prop){
			if (prop.equalsIgnoreCase(INFO.property))
				return Group.INFO;
			if (prop.equalsIgnoreCase(WARNING.property))
				return Group.WARNING;
			if (prop.equalsIgnoreCase(ERROR.property))
				return Group.ERROR;
			if (prop.equalsIgnoreCase(IMPORTANT.property))
				return Group.IMPORTANT;
			return null;
		}
		
	};
	
	public static MyLog getInstance() {
		if (logInstance == null || !logInstance.matchesCurrentSeverity(defaultSeverity)) {
			// log everyting
			logInstance = new MyLog();
		}
		return logInstance;
	}
	
	public static MyLog getInstance(boolean printInfo, boolean printWarning, boolean printError) {
		if (logInstance == null ||
			printInfo != print[0] || printWarning != print[1] || printError != print[2]   
		   ){
			logInstance = new MyLog(printInfo, printWarning, printError);
		}
		return logInstance;
	}
	
	private MyLog(){
		// log everything
		System.out.println("Log switched to: "+ defaultSeverity.property);
		map = new MultiMap<String>();
		switch (defaultSeverity) {
			// não usar 'break' - ex. se for ERROR, [1] [2] e [3] = TRUE
			case INFO:
				print[0] = true;
			case WARNING:
				print[1] = true;
			case ERROR:
			case IMPORTANT:
				print[2] = true;
		}
	}
	
	private MyLog(boolean printInfo, boolean printWarning, boolean printError) {	
		map = new MultiMap<String>();
		print[0] = printInfo;
		print[1] = printWarning;
		print[2] = printError;
	
	}
	
	private boolean matchesCurrentSeverity(Group severity) {
		// começar da mais abrangente (INFO) pra mais específica (ERROR)
		if (print[0] && print[1] && print[2] && severity == Group.INFO )
			return true;
		else if (print[1] && print[2] && severity == Group.WARNING )
			return true;
		else if (print[2] && severity == Group.ERROR )
			return true;
		
		return false;
	}
	
	public void add(Group group, String msg){
		map.add(group.property, msg);
		if(isPrinted(group)){
			System.out.println(msg);
		}
	}
	
	public void add(Group group, String[] msgs){
		StringBuilder builder = new StringBuilder();
		for(String msg : msgs){
			builder.append(msg + "\n");
		}
		map.add(group.property, builder.toString());
		if(isPrinted(group)){
			System.out.println(builder.toString());
		}
	}
	
	public boolean isPrinted(Group group){
		if(group == Group.INFO){
			return print[0];
		} else if(group == Group.WARNING){
			return print[1];
		} else if(group == Group.ERROR || group == Group.IMPORTANT){
			return print[2];
		} 
		return false;
	}
	
	public void retrieveLog(){
		Set<String> keys = this.map.keySet();
		for(String key : keys){
			System.out.println("Group: " + key);
			List<String> values = this.map.getValues(key);
			for(String value : values){
				System.out.println("\t" + value);
			}			
		}
	}
	
	static private Group getDefaultSeverity() {
		String severityArg = System.getProperty("myloglevel");
		if (severityArg != null) {
			switch (Group.getGroup(severityArg)) {
				case INFO:
				case WARNING:
				case ERROR:
				case IMPORTANT:
					return Group.getGroup(severityArg);
			}
		}
		return Group.ERROR; 	// default if not specified on init parameter
	}
}

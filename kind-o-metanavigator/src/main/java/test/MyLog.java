package test;

import java.util.List;
import java.util.Set;

import org.eclipse.jetty.util.MultiMap;

public class MyLog {
	
	MultiMap<String> map;
	static boolean[] print = new boolean[3];
	
	public enum Group {
		INFO("info"),
		WARNING("warn"),
		ERROR("error"); 
		public final String property;
		
		Group(String prop){
			this.property = prop;
		}
		
		public String getProp(){
			return this.property;
		}
		public String newProp(String prop){
			return prop;
		}
		
	};
	
	public MyLog(){
		if(true){
			map = new MultiMap<String>();
			print[0] = true;
			print[1] = true;
			print[2] = true;
		}
	}
	
	public MyLog(boolean printInfo, boolean printWarning, boolean printError){
		if(true){
			map = new MultiMap<String>();
			print[0] = printInfo;
			print[1] = printWarning;
			print[2] = printError;
		}
	}
	
	public void add(Group group, String msg){
		map.add(group.getProp(), msg);
		if(isPrinted(group)){
			System.out.println(msg);
		}
	}
	
	public void add(Group group, String[] msgs){
		StringBuilder builder = new StringBuilder();
		for(String msg : msgs){
			builder.append(msg + "\n");
		}
		map.add(group.getProp(), builder.toString());
		if(isPrinted(group)){
			System.out.println(builder.toString());
		}
	}
	
	public boolean isPrinted(Group group){
		if(group == Group.INFO){
			return print[0];
		} else if(group == Group.WARNING){
			return print[1];
		} else if(group == Group.ERROR){
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
	
}

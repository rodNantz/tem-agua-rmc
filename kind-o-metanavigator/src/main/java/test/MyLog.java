package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jetty.util.MultiMap;

public class MyLog {
	
	MultiMap<String> map;
	
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
		}
	}
	
	
	public MyLog(Group group, String msg){
		if(true){
			map = new MultiMap<String>();
			map.add(group.getProp(), msg);
			System.out.println(msg);
		}
	}
	

	public void add(Group group, String msg){
		map.add(group.getProp(), msg);
		System.out.println(msg);
	}
	
	public void add(Group group, String[] msgs){
		StringBuilder builder = new StringBuilder();
		for(String msg : msgs){
			builder.append(msg + "\n");
		}
		map.add(group.getProp(), builder.toString());
		System.out.println(builder.toString());
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

package test;

import java.util.List;
import java.util.Set;

import org.eclipse.jetty.util.MultiMap;

public class MyLog {
	
	MultiMap<String> map;
	
	public MyLog(){
		if(true){
			map = new MultiMap<String>();
		}
	}
	
	public MyLog(String group, String msg){
		if(true){
			map = new MultiMap<String>();
			map.add(group, msg);
			System.out.println(msg);
		}
	}
	
	public void add(String group, String msg){
		map.add(group, msg);
		System.out.println(msg);
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

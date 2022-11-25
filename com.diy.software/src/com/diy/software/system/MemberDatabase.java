package com.diy.software.system;

import java.util.HashMap;
import java.util.Map;

/**
Represents a set of databases that the simulation can interact with. 
The databases have to be populated in order to be usable.
*/

public class MemberDatabase {

	public MemberDatabase() 
	{
		//MEMBER_DATABASE.put(555,"J");
		//adding data into Membership database
	}
	
	public static final  Map<Integer,String> MEMBER_DATABASE = new HashMap<>();
	
	/* Not required
	public static boolean containsValue(int integer) {  
		//return MEMBER_DATABASE.containsKey(555); 
		return true;
		}
	*/
}
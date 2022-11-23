package com.diy.software.system;

import java.util.HashMap;
import java.util.Map;

/**
Represents a set of databases that the simulation can interact with. 
The databases have to be populated in order to be usable.
*/

public class MemberDatabase {

	private MemberDatabase() {}
	
	public static final  Map<Integer,String> MEMBER_DATABASE = new HashMap<>();
	
}
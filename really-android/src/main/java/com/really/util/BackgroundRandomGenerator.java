package com.really.util;

import java.util.HashMap;
import java.util.Map;

public class BackgroundRandomGenerator {
	
	private static Map<Integer, String> backgroundFactory;
	
	 static {
		backgroundFactory = new HashMap<Integer, String>();
		backgroundFactory.put(0, "#0FC000");
		backgroundFactory.put(1, "#724652");
		backgroundFactory.put(2, "#0F92CC");
		backgroundFactory.put(3, "#CEEF8C");
		backgroundFactory.put(4, "#5ACF00");
		backgroundFactory.put(5, "#2C2255");
		backgroundFactory.put(6, "#E46F19");
		backgroundFactory.put(7, "#E4D286");
		backgroundFactory.put(8, "#0E72CB");
		backgroundFactory.put(9, "#E699D7");
		backgroundFactory.put(10, "#F6B744");
	 }
	
	public static String generate() {
		int i = (int)(Math.random() * 10);
		return backgroundFactory.get(i);
	}

}

package org.nationsatwar.goldfish;

import org.bukkit.plugin.java.JavaPlugin;

public class GoldfishAPI {
	
	JavaPlugin externalPlugin;
	
	public static int test = 0;
	public static String test2 = "what";
	
	public GoldfishAPI(JavaPlugin externalPlugin) {
		
		this.externalPlugin = externalPlugin;
	}
	
	public int denyAccess(JavaPlugin plugin) {
		
		test = 2;
		
		return 5;
	}
}
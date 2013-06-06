package com.github.Fritos.Goldfish.Utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.Fritos.Goldfish.Goldfish;

public class GoldfishInstanceConfig {
	
	// Instance Config Options
	public static String instanceLoadOnStartup = "instance.loadonstartup";

	public static String timerAmount = "timer.amount";
	
	private static Logger log = Logger.getLogger("Minecraft");
	private static String lineBreak = "\r\n";
	
	public static void saveInstanceConfig(String worldName) {

	    File dataFile = new File(Goldfish.instancePath + worldName + "\\instancedata.yml");
		
	    FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
	    FileConfigurationOptions configOptions = config.options();

	    // Creates default config parameters on creation
	    config.addDefault(instanceLoadOnStartup, true);

	    config.addDefault(timerAmount, 0);
	    
	    configOptions.copyDefaults(true);
	    
	    String header = "Goldfish Instance Config File" + lineBreak;
	    
	    header += lineBreak + "It is advised not to edit any of these values unless you really know what you're doing." + lineBreak;
	    header += lineBreak + "Most of these values are manipulated through hooks or server functions." + lineBreak;
	    header += lineBreak + "These values will not be read automatically simply by editing them." + lineBreak;
	    
	    header += lineBreak + "-=(Load on Startup)=-" + lineBreak;
	    header += "Determines if an instance world will be loaded on server startup." + lineBreak;
	    header += "This value is altered every time the server restarts." + lineBreak;
	    header += "True if the instance world has players in it, false otherwise." + lineBreak;
	    
	    header += lineBreak + "-=(Timer)=-" + lineBreak;
	    header += "How long the instance has left until it expires." + lineBreak;
	    header += "The timer will only update if manipulated through a hook or upon server reboot." + lineBreak;
	    
	    configOptions.header(header);
	    configOptions.copyHeader(true);
	    
	    try { config.save(dataFile); }
	    catch (IOException e) { log.info(e.getMessage()); }
	}
}
package org.nationsatwar.goldfish.Utility;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.nationsatwar.goldfish.Goldfish;


public class GoldfishPrototypeConfig {
	
	// Prototype Config Options
	public static String limitActive = "limit.active";
	public static String limitOneX = "limit.one.x";
	public static String limitOneZ = "limit.one.z";
	public static String limitTwoX = "limit.two.x";
	public static String limitTwoZ = "limit.two.z";

	public static String blockBreakAllowAll = "block.break.allowall";
	public static String blockBreak0 = "block.break.0";
	public static String blockBreak1 = "block.break.1";
	public static String blockPlaceAllowAll = "block.place.allowall";
	public static String blockPlace0 = "block.place.0";
	public static String blockPlace1 = "block.place.1";
	public static String blockUseAllowAll = "block.use.allowall";
	public static String blockUse0 = "block.use.0";
	public static String blockUse1 = "block.use.1";

	public static String respawnInstance = "respawn.instance";
	public static String respawnInside = "respawn.inside";

	public static String timerAmount = "timer.amount";
	public static String timerActiveWhenEmpty = "timer.activewhenempty";
	
	private static Logger log = Logger.getLogger("Minecraft");
	private static String lineBreak = "\r\n";
	
	public static void savePrototypeConfig(String worldName) {

	    File dataFile = new File(Goldfish.prototypePath + worldName + "\\prototypedata.yml");
		
	    FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
	    FileConfigurationOptions configOptions = config.options();

	    // Creates default config parameters on creation
	    config.addDefault(limitActive, false);
	    config.addDefault(limitOneX, 0);
	    config.addDefault(limitOneZ, 0);
	    config.addDefault(limitTwoX, 0);
	    config.addDefault(limitTwoZ, 0);
	    
	    config.addDefault(blockBreakAllowAll, true);
	    config.addDefault(blockBreak0, true);
	    config.addDefault(blockBreak1, true);
	    config.addDefault(blockPlaceAllowAll, true);
	    config.addDefault(blockPlace0, true);
	    config.addDefault(blockPlace1, true);
	    config.addDefault(blockUseAllowAll, true);
	    config.addDefault(blockUse0, true);
	    config.addDefault(blockUse1, true);

	    config.addDefault(respawnInstance, true);
	    config.addDefault(respawnInside, true);

	    config.addDefault(timerAmount, 0);
	    config.addDefault(timerActiveWhenEmpty, false);
	    
	    configOptions.copyDefaults(true);
	    
	    String header = "Goldfish Prototype Config File" + lineBreak;
	    
	    header += lineBreak + "-=(Limit)=-" + lineBreak;
	    header += "When 'Limit' is active, the instance size will be limited between two corners." + lineBreak;
	    header += "Limiting instance size prevents player wandering and allows a much greater level " + lineBreak;
	    header += "of control over your instance." + lineBreak;
	    header += "The x and z coordinates are based on chunks, which are 16x16 blocks." + lineBreak;
	    header += "To avoid problems, make sure to set at least four chunks on each side of each spawn point." + lineBreak;
	    header += "Also, avoid chunk: (0,0) if possible unless you load at least 40 x 40 chunks." + lineBreak;

	    header += lineBreak + "-=(Block Permissions)=-" + lineBreak;
	    header += "These parameters will determine what blocks can be placed, broken, or used inside the instance." + lineBreak;
	    header += "'AllowAll' will determine the permissions of every block that isn't listed." + lineBreak;
	    header += "Individual blocks determined by Type ID will override the 'AllowAll' parameter." + lineBreak;

	    header += lineBreak + "-=(Respawn)=-" + lineBreak;
	    header += "There are two parameters here, respawn.instance and respawn.inside" + lineBreak;
	    header += "The first parameter will determine whether or not the player respawns at the instance or their default spawn point." + lineBreak;
	    header += "The second parameter will only activate if the first parameter is true." + lineBreak;
	    header += "Respawn.Inside will spawn the player inside the instance if true, or right outside if false." + lineBreak;

	    header += lineBreak + "-=(Timer)=-" + lineBreak;
	    header += "Amount determines how long an instance will last. 0 means indefinitely." + lineBreak;
	    header += "When a timer reaches 0, everyone in the instance will be teleported out and the instance deleted." + lineBreak;
	    header += "If 'Active When Empty' is true, the timer will continue even if there's no players in the instance." + lineBreak;
	    
	    configOptions.header(header);
	    configOptions.copyHeader(true);
	    
	    try { config.save(dataFile); }
	    catch (IOException e) { log.info(e.getMessage()); }
	}
}
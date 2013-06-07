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
	
	public static String instanceTimerAmount = "instancetimer.amount";
	public static String instanceTimerActiveWhenEmpty = "instancetimer.activewhenempty";
	
	public static String timeoutTimerAmount = "timeouttimer.amount";
	
	public static String staticInstance = "staticinstance";
	
	public static String entrancesLocation1Active = "entrances.location1.active";
	public static String entrancesLocation1EntranceWorld = "entrances.location1.entranceworld";
	public static String entrancesLocation1EntranceX = "entrances.location1.entrancex";
	public static String entrancesLocation1EntranceY = "entrances.location1.entrancey";
	public static String entrancesLocation1EntranceZ = "entrances.location1.entrancez";
	public static String entrancesLocation1InstanceX = "entrances.location1.instancex";
	public static String entrancesLocation1InstanceY = "entrances.location1.instancey";
	public static String entrancesLocation1InstanceZ = "entrances.location1.instancez";
	
	public static String exitsLocation1Active = "exits.location1.active";
	public static String exitsLocation1ExitWorld = "exits.location1.exitworld";
	public static String exitsLocation1ExitX = "exits.location1.exitx";
	public static String exitsLocation1ExitY = "exits.location1.exity";
	public static String exitsLocation1ExitZ = "exits.location1.exitz";
	public static String exitsLocation1InstanceX = "exits.location1.instancex";
	public static String exitsLocation1InstanceY = "exits.location1.instancey";
	public static String exitsLocation1InstanceZ = "exits.location1.instancez";
	
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

	    config.addDefault(respawnInstance, false);
	    config.addDefault(respawnInside, true);

	    config.addDefault(instanceTimerAmount, 0);
	    config.addDefault(instanceTimerActiveWhenEmpty, false);

	    config.addDefault(timeoutTimerAmount, 60);

	    config.addDefault(staticInstance, false);

	    config.addDefault(entrancesLocation1Active, false);
	    config.addDefault(entrancesLocation1EntranceWorld, "");
	    config.addDefault(entrancesLocation1EntranceX, 0);
	    config.addDefault(entrancesLocation1EntranceY, 0);
	    config.addDefault(entrancesLocation1EntranceZ, 0);
	    config.addDefault(entrancesLocation1InstanceX, 0);
	    config.addDefault(entrancesLocation1InstanceY, 0);
	    config.addDefault(entrancesLocation1InstanceZ, 0);

	    config.addDefault(exitsLocation1Active, false);
	    config.addDefault(exitsLocation1ExitWorld, "");
	    config.addDefault(exitsLocation1ExitX, 0);
	    config.addDefault(exitsLocation1ExitY, 0);
	    config.addDefault(exitsLocation1ExitZ, 0);
	    config.addDefault(exitsLocation1InstanceX, 0);
	    config.addDefault(exitsLocation1InstanceY, 0);
	    config.addDefault(exitsLocation1InstanceZ, 0);
	    
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

	    header += lineBreak + "-=(Instance Timer)=-" + lineBreak;
	    header += "Amount determines how long an instance will last. 0 means indefinitely." + lineBreak;
	    header += "When a timer reaches 0, everyone in the instance will be teleported out and the instance deleted." + lineBreak;
	    header += "If 'Active When Empty' is true, the timer will continue even if there's no players in the instance." + lineBreak;

	    header += lineBreak + "-=(Timeout Timer)=-" + lineBreak;
	    header += "Amount determines how long an instance will last. 0 means indefinitely." + lineBreak;
	    header += "Different from instance timer in that it only counts down when the instance is empty, " + lineBreak;
	    header += "and resets whenever a player re-enters the instance." + lineBreak;

	    header += lineBreak + "-=(Static Instance)=-" + lineBreak;
	    header += "If an instance is static, then only one instance can ever be active at any given time." + lineBreak;
	    header += "All players will join the same instance on entering." + lineBreak;

	    header += lineBreak + "-=(Entrances and Exits)=-" + lineBreak;
	    header += "This determines exactly where the entrances and exits are, and where they link to." + lineBreak;
	    header += "You can have as many entrances and exits as you like, just number them in connecting order." + lineBreak;
	    header += "If you have an 'entrance4' but no 'entrance3', 'entrance4' will not be checked." + lineBreak;
	    header += "No instance world needs to be specified for obvious reasons." + lineBreak;
	    header += "Also, setting entrances and exits in-game will replace entrance1 and exit1 respectively." + lineBreak;
	    
	    configOptions.header(header);
	    configOptions.copyHeader(true);
	    
	    try { config.save(dataFile); }
	    catch (IOException e) { log.info(e.getMessage()); }
	}
}
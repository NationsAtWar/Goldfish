package com.github.Fritos.Goldfish;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GoldfishCommandExecutor implements CommandExecutor {

	private Goldfish plugin;
	
	public GoldfishCommandExecutor(Goldfish plugin) {
		
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String argsLabel, String[] args) {
		
		for (int i = 0; i < args.length; i++)
			args[i] = args[i].toLowerCase();
		
		// -goldfish OR -goldfish help
		if (args.length == 0 || args[0].equals("help"))
			helpCommand(sender);
		
		// -goldfish prototype
		else if (args[0].equals("prototype"))
			prototypeCommand(sender, args);
		
		// -goldfish entrance
		else if (args[0].equals("entrance"))
			entranceCommand(sender, args);
		
		// -goldfish exit
		else if (args[0].equals("exit"))
			exitCommand(sender, args);
		
		// -goldfish activate
		else if (args[0].equals("activate"))
			activateCommand(sender, args);
		
		// -goldfish enter
		else if (args[0].equals("enter"))
			enterCommand(sender, args);
		
		// -goldfish enter
		else if (args[0].equals("leave"))
			leaveCommand(sender, args);
		
		// -goldfish destructible
		else if (args[0].equals("destructible"))
			destructibleCommand(sender, args);
		
		// -goldfish naked
		else if (args[0].equals("naked"))
			nakedCommand(sender, args);
		
		// -goldfish timer
		else if (args[0].equals("timer"))
			timerCommand(sender, args);
		
		// -goldfish <non-applicable command>
		else
			sender.sendMessage(ChatColor.DARK_RED + "Invalid command: type '/goldfish' for help.");
		
		return true;
	}
	
	private void helpCommand(CommandSender sender) {

		sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[GOLDFISH]=-");
		sender.sendMessage(ChatColor.YELLOW + "Allows you to create and manage instances.");
		sender.sendMessage(ChatColor.YELLOW + "Command List: Prototype, Entrance, Exit, Activate, " +
				"Enter, Leave, Destructible, Naked, Minimum, Timer");
	}

	/**
	 * Handles the prototype command: Create, Warp, List, Update, and Delete
	 */
	public void prototypeCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Goldfish]" + ChatColor.DARK_AQUA + " -=[PROTOTYPE]=-");
			sender.sendMessage(ChatColor.YELLOW + "Allows you to manage prototypes for instances.");
			sender.sendMessage(ChatColor.YELLOW + "Sub-args list: Create, Save, Warp, List, Update, Delete");
			return;
		}

		if (args[1].equals("create") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - CREATE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype create [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Creates a new world called [worldname] in the prototype folder.");
			return;
		}

		if (args[1].equals("warp") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - WARP]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype warp [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Warps you to the prototype of instance [worldname]");
			return;
		}

		if (args[1].equals("list") && (args.length > 2 && args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - LIST]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype list'");
			sender.sendMessage(ChatColor.YELLOW + "Gives you a list of all prototype worlds.");
			return;
		}

		if (args[1].equals("update") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - UPDATE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype update [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Updates [worldname] for all future instances.");
			return;
		}

		if (args[1].equals("delete") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - DELETE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype delete [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Completely destroys the prototype.");
			return;
		}
		
		// PROTOTYPE - CREATE
		
		String worldName = (args.length > 2 ? args[2] : "");
		
		if (args[1].equals("create") && !worldName.equals("")) {
			
			if (plugin.goldfishManager.exists(worldName)) {
				
				sender.sendMessage(ChatColor.YELLOW + "A prototype with that name already exists.");
				return;
			}
			
			plugin.getServer().createWorld(new WorldCreator(worldName).environment(Environment.NORMAL));
			
			GoldfishInstance instance = new GoldfishInstance(plugin, worldName);
			plugin.goldfishManager.add(instance);

			sender.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been created. You may warp there at " +
					"your convenience");
			
		    saveInstance(worldName);
		    
			return;
		}
		
		// PROTOTYPE - WARP
		
		if (args[1].equals("warp") && !worldName.equals("")) {

			if (!plugin.goldfishManager.exists(worldName)) {
				
				sender.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
				return;
			}

			World newWorld = plugin.getServer().getWorld(worldName);
			
			Player player = (Player) sender;
			
			if (!plugin.goldfishManager.find(worldName).exitSet)
				player.teleport(newWorld.getSpawnLocation());
			else
				player.teleport(plugin.goldfishManager.find(worldName).getExitLocation());
			
			return;
		}
		
		// PROTOTYPE - LIST
		
		if (args[1].equals("list")) {
			
			for ( String instanceName : plugin.goldfishManager.getInstanceNames() )
				sender.sendMessage(ChatColor.YELLOW + instanceName);

			return;
		}
		
		// PROTOTYPE - UPDATE
		
		if (args[1].equals("update") && !worldName.equals("")) {

			if (!plugin.goldfishManager.exists(worldName)) {
				
				sender.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
				return;
			}
			
		    saveInstance(worldName);

			return;
		}
		
		// PROTOTYPE - DELETE
		
		if (args[1].equals("delete") && !worldName.equals("")) {

			if (!plugin.goldfishManager.exists(worldName)) {
				
				sender.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
				return;
			}
			
			plugin.getServer().unloadWorld(worldName, true);
			plugin.goldfishManager.remove(plugin.goldfishManager.find(worldName));

		    File worldDir = new File(worldName + "\\");
		    File protoDir = new File("plugins\\instances\\prototypes\\" + worldName + "\\");
			
			try {
				FileUtils.deleteDirectory(worldDir);
				FileUtils.deleteDirectory(protoDir);
				sender.sendMessage(ChatColor.YELLOW + "Prototype world: " + worldName + " has been deleted.");
			} catch (Exception e) {
				plugin.logger("Could not delete prototype world.");
			}

			sender.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been deleted.");
			return;
		}
	}

	/**
	 * Handles the Entrance command
	 */
	private void entranceCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || (args.length > 2 && args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[ENTRANCE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish entrance [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Makes the region around your current location the entrance to [worldname].");
			return;
		}
		
		String worldName = (args.length > 1 ? args[1] : "");
		Player player = (Player) sender;

		if (!plugin.goldfishManager.exists(worldName)) {
			
			sender.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		plugin.goldfishManager.find(worldName).setNewEntranceLocation(player.getLocation());

		sender.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + "'s entrance location has been set.");
		
	    saveInstance(worldName);
	    
		return;
	}

	/**
	 * Handles the Exit command
	 */
	private void exitCommand(CommandSender sender, String[] args) {

		if (args.length > 1 && args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[EXIT]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish exit");
			sender.sendMessage(ChatColor.YELLOW + "Makes the region around your current location the exit.");
			return;
		}
		
		Player player = (Player) sender;
		
		for ( String instanceName : plugin.goldfishManager.getInstanceNames() )
			if (player.getWorld().getName().equals(instanceName)) {
				
				plugin.goldfishManager.find(instanceName).setNewExitLocation(player.getLocation());
				sender.sendMessage(ChatColor.YELLOW + "Prototype " + instanceName + "'s exit location has been set.");
				
			    saveInstance(instanceName);
				return;
			}

		sender.sendMessage(ChatColor.YELLOW + "You are not inside an instance prototype.");
	    
		return;
	}

	/**
	 * Handles the Activate command
	 */
	private void activateCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[ACTIVATE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish activate [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Activates [worldname] so it can now be instanced. Requires entrance and exit.");
			return;
		}
		
		String worldName = (args.length > 1 ? args[1] : "");
		
		GoldfishInstance instance = plugin.goldfishManager.find(worldName);
		
		if (instance == null) {
			
			sender.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		if (!instance.canActivate()) {
			
			sender.sendMessage(ChatColor.YELLOW + "You have to set this prototype's entrance and exit first.");
			return;
		}
		
	    saveInstance(worldName);
		
		instance.toggleActivated();
		
		if (instance.isActivated())
			sender.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been activated");
		else
			sender.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been deactivated");
	}

	/**
	 * Handles the Enter command
	 */
	private void enterCommand(CommandSender sender, String[] args) {

		if (args.length > 1 && args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[ENTER]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish enter");
			sender.sendMessage(ChatColor.YELLOW + "Allows normal players/parties to create an instance to warp to.");
			return;
		}
		
		for ( String instanceName : plugin.goldfishManager.getInstanceNames() ) {
			
			GoldfishInstance instance = plugin.goldfishManager.find(instanceName);
			
			Player player = (Player) sender;
			
			if (player.getWorld().getName().equals(instance.getEntranceLocation().getWorld().getName())) {

				double entranceDistance = player.getLocation().distance(instance.getEntranceLocation());
				
				if (entranceDistance < 10 && instance.isActivated()) {
					
					String newInstanceName = player.getName();
					
					newInstanceName = newInstanceName + "_" + instanceName;
					
					World world = plugin.getServer().getWorld(newInstanceName);
					
					if (world == null) {

					    File protoDir = new File("plugins\\instances\\prototypes\\" + instanceName + "\\");
					    File worldDir = new File(newInstanceName + "\\");
						
					    copyDirectory(protoDir, worldDir);
						
						plugin.getServer().createWorld(new WorldCreator(newInstanceName).environment(Environment.NORMAL));
						world = plugin.getServer().getWorld(newInstanceName);

						GoldfishThread instanceThread = new GoldfishThread(plugin, newInstanceName, 30000);
						instanceThread.start();
					}
					
					player.teleport(new Location(world, instance.getExitLocation().getX(), 
							instance.getExitLocation().getY(), instance.getExitLocation().getZ()));
					
					return;
				}
			}
		}
		
		sender.sendMessage(ChatColor.YELLOW + "No instance locations here.");
		return;
	}

	/**
	 * Handles the Leave command
	 */
	private void leaveCommand(CommandSender sender, String[] args) {

		if (args.length > 1 && args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[Leave]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish leave");
			sender.sendMessage(ChatColor.YELLOW + "Leaves the instance to the previous world.");
			return;
		}
		
		for ( String instanceName : plugin.goldfishManager.getInstanceNames() ) {
			
			GoldfishInstance instance = plugin.goldfishManager.find(instanceName);
			
			Player player = (Player) sender;
			
			if (player.getWorld().getName().equals(instance.getExitLocation().getWorld().getName())) {

				double exitDistance = player.getLocation().distance(instance.getExitLocation());
				
				if (exitDistance < 10) {
					
					player.teleport(instance.getEntranceLocation());
					return;
				}
			}
		}
		
		sender.sendMessage(ChatColor.YELLOW + "No instance locations here.");
		return;
	}
	
	/**
	 * Handles the Destructible command
	 */
	private void destructibleCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[DESTRUCTIBLE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish destructible [true/false] [blocktype] [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Changes whether or not [blocktype] is destructible in [world]");
			sender.sendMessage(ChatColor.YELLOW + "If [blocktype] is 'all', all block types are affected.");
			return;
		}
	}
	
	/**
	 * Handles the Naked command
	 */
	private void nakedCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[NAKED]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish naked [true/false] [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Changes whether or not players/parties enter with equipment.");
			return;
		}
	}
	
	/**
	 * Handles the Timer command
	 */
	private void timerCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[TIMER]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish timer [timeramount] [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Gives the instance a timer, if it reaches 0, parties are kicked out.");
			sender.sendMessage(ChatColor.YELLOW + "Set timer to 0 for indefinite.");
			return;
		}
	}
	
	private void saveInstance(String worldName) {
		
		plugin.getServer().getWorld(worldName).save();

	    File worldDir = new File(worldName + "\\");
	    File protoDir = new File("plugins\\instances\\prototypes\\" + worldName);
	    
	    copyDirectory(worldDir, protoDir);
	    
	    File uidFile = new File("plugins\\instances\\prototypes\\" + worldName + "\\uid.dat");
	    uidFile.delete();
	}
	
    private void copyDirectory(File sourceLocation, File targetLocation) {
        
        if (sourceLocation.isDirectory()) {
        	
            if (!targetLocation.exists())
                targetLocation.mkdir();
            
            String[] children = sourceLocation.list();
            
            for (int i=0; i<children.length; i++)
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            
        } else {
            
        	try {
	            InputStream in = new FileInputStream(sourceLocation);
	            OutputStream out = new FileOutputStream(targetLocation);
	            
	            // Copy the bits from instream to outstream
	            byte[] buf = new byte[1024];
	            int len;
	            
	            while ((len = in.read(buf)) > 0)
	                out.write(buf, 0, len);
	            
	            in.close();
	            out.close();
	            
        	} catch (Exception e) { plugin.logger(e.getMessage()); }
        }
    }
}
package org.nationsatwar.goldfish;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Commands.GoldfishCommandEnter;
import org.nationsatwar.goldfish.Commands.GoldfishCommandLeave;
import org.nationsatwar.goldfish.Commands.GoldfishCommandList;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandActivate;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandCreate;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandDelete;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandEntrance;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandExit;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandUpdate;
import org.nationsatwar.goldfish.Commands.Prototype.GoldfishCommandWarp;



public class GoldfishCommandExecutor implements CommandExecutor {
	
	private GoldfishCommandCreate createCommand;
	private GoldfishCommandEntrance entranceCommand;
	private GoldfishCommandExit exitCommand;
	private GoldfishCommandActivate activateCommand;
	private GoldfishCommandUpdate updateCommand;
	private GoldfishCommandDelete deleteCommand;
	private GoldfishCommandWarp warpCommand;
	private GoldfishCommandList listCommand;
	
	private GoldfishCommandEnter enterCommand;
	private GoldfishCommandLeave leaveCommand;
	
	public GoldfishCommandExecutor(Goldfish plugin) {
		
		createCommand = new GoldfishCommandCreate(plugin);
		entranceCommand = new GoldfishCommandEntrance(plugin);
		exitCommand = new GoldfishCommandExit(plugin);
		activateCommand = new GoldfishCommandActivate(plugin);
		updateCommand = new GoldfishCommandUpdate(plugin);
		deleteCommand = new GoldfishCommandDelete(plugin);
		warpCommand = new GoldfishCommandWarp(plugin);
		
		enterCommand = new GoldfishCommandEnter(plugin);
		leaveCommand = new GoldfishCommandLeave(plugin);
		listCommand = new GoldfishCommandList(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String argsLabel, String[] args) {
		
		for (int i = 0; i < args.length; i++)
			args[i] = args[i].toLowerCase();
		
		// -goldfish OR -goldfish help
		if (args.length == 0 || args[0].equals("help"))
			helpCommand(sender);
		
		// -goldfish prototype
		else if (args[0].equals("prototype") || args[0].equals("pt"))
			prototypeCommand(sender, args);
		
		// -goldfish enter
		else if (args[0].equals("enter"))
			enterCommand(sender, args);
		
		// -goldfish leave
		else if (args[0].equals("leave"))
			leaveCommand(sender, args);
		
		// -goldfish leave
		else if (args[0].equals("list"))
			listCommand(sender, args);
		
		// -goldfish debug
		else if (args[0].equals("debug"))
			debugCommand(sender, args);
		
		// -goldfish <non-applicable command>
		else
			sender.sendMessage(ChatColor.DARK_RED + "Invalid command: type '/goldfish' for help.");
		
		return true;
	}
	
	private void helpCommand(CommandSender sender) {

		sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[GOLDFISH]=-");
		sender.sendMessage(ChatColor.YELLOW + "Allows you to create and manage instances.");
		sender.sendMessage(ChatColor.YELLOW + "Command List: Prototype, Enter, Leave, List");
	}

	/**
	 * Handles the prototype commands: Create, Entrance, Exit, Activate, Update, Delete, Warp
	 */
	public void prototypeCommand(CommandSender sender, String[] args) {

		if (args.length == 1 || args[1].equals("help")) {
			sender.sendMessage(ChatColor.DARK_RED + "[Goldfish]" + ChatColor.DARK_AQUA + " -=[PROTOTYPE]=-");
			sender.sendMessage(ChatColor.YELLOW + "Allows you to manage instance prototypes. ('/goldfish pt' for short)");
			sender.sendMessage(ChatColor.YELLOW + "Sub-Command List: Create, Entrance, Exit, Activate, Update, " +
			"Delete, Warp, List");
			return;
		}

		if (args[1].equals("create") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - CREATE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype create [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Creates a new world called [worldname] in the prototype folder.");
			return;
		}

		if (args[1].equals("entrance") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - ENTRANCE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish entrance [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Makes the region around your current location the entrance to [worldname].");
			return;
		}

		if (args[1].equals("exit") && (args.length > 2 && args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - EXIT]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish exit");
			sender.sendMessage(ChatColor.YELLOW + "Makes the region around your current location the exit.");
			return;
		}

		if (args[1].equals("activate") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - ACTIVATE]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish activate [worldname]");
			sender.sendMessage(ChatColor.YELLOW + "Activates [worldname] so it can now be instanced. Requires entrance and exit.");
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

		if (args[1].equals("warp") && (args.length == 2 || args[2].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - WARP]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish prototype warp [worldname]'");
			sender.sendMessage(ChatColor.YELLOW + "Warps you to the prototype of instance [worldname]");
			return;
		}
		
		String worldName = (args.length > 2 ? args[2] : "");
		
		// PROTOTYPE - CREATE
		
		if (args[1].equals("create") && !worldName.equals(""))
			createCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - ENTRANCE
		
		if (args[1].equals("entrance") && !worldName.equals(""))
			entranceCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - EXIT
		
		if (args[1].equals("exit"))
			exitCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - ACTIVATE
		
		if (args[1].equals("activate") && !worldName.equals(""))
			activateCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - UPDATE
		
		if (args[1].equals("update") && !worldName.equals(""))
			updateCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - DELETE
		
		if (args[1].equals("delete") && !worldName.equals(""))
			deleteCommand.execute((Player) sender, worldName);
		
		// PROTOTYPE - WARP
		
		if (args[1].equals("warp") && !worldName.equals(""))
			warpCommand.execute((Player) sender, worldName);
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
		
		else
			enterCommand.execute((Player) sender);
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
		
		else
			leaveCommand.execute((Player) sender);
	}

	/**
	 * Handles the List command
	 */
	private void listCommand(CommandSender sender, String[] args) {

		if (args[0].equals("list") && (args.length > 1 && args[1].equals("help"))) {
			sender.sendMessage(ChatColor.DARK_RED + "[Nations at War]" + ChatColor.DARK_AQUA + " -=[PROTOYPE - LIST]=-");
			sender.sendMessage(ChatColor.DARK_AQUA + "e.g. '/goldfish list'");
			sender.sendMessage(ChatColor.YELLOW + "Gives you a list of all available instances.");
			return;
		}
		
		else
			listCommand.execute((Player) sender);
	}

	/**
	 * Handles the Debug command
	 */
	private void debugCommand(CommandSender sender, String[] args) {
		
		// Temporary command used for debugging purposes.
		
		Player player = (Player) sender;
		
		sender.sendMessage(player.getLocation().getWorld().getChunkAt(player.getLocation()).toString());
	}
}
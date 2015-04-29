package org.nationsatwar.goldfish.Commands.Prototype;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.Utility.GoldfishUtility;


public class GoldfishCommandDelete {

	private Goldfish plugin;
	
	public GoldfishCommandDelete(Goldfish plugin) {
		
		this.plugin = plugin;
	}
	
	public void execute(Player player, String worldName) {

		if (!plugin.goldfishManager.prototypeExists(worldName)) {
			
			player.sendMessage(ChatColor.YELLOW + "A prototype with that name does not exist.");
			return;
		}
		
		plugin.getServer().unloadWorld(Goldfish.prototypePath + worldName, true);
		plugin.goldfishManager.removePrototype(plugin.goldfishManager.findPrototype(worldName));

	    File protoDir = new File(Goldfish.prototypePath + worldName + "/");
	    File protoFile = new File(Goldfish.prototypePath + worldName + ".gf");
		
		File baseInstanceDir = new File(Goldfish.instancePath);
		
		List<File> instanceDirs = new ArrayList<File>();
		
		for (String checkFile : baseInstanceDir.list())
			if (checkFile.length() >= worldName.length() && checkFile.substring(0, worldName.length()).equals(worldName)) {
				
				instanceDirs.add(new File(Goldfish.instancePath + checkFile));
				plugin.getServer().unloadWorld(Goldfish.instancePath + checkFile, true);
			}
		
		try {
			
			GoldfishUtility.deleteDirectory(protoDir);
			
			if (protoFile.exists())
				protoFile.delete();
			
			for (File instanceDir : instanceDirs)
				GoldfishUtility.deleteDirectory(instanceDir);
			
		} catch (Exception e) {
			plugin.logger("Could not delete prototype world.");
			return;
		}

		player.sendMessage(ChatColor.YELLOW + "Prototype " + worldName + " has been deleted.");
	}
}
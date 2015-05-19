package org.nationsatwar.goldfish.gui.prototype;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.gui.GUIMainMenu;
import org.nationsatwar.goldfish.gui.config.GUIConfiguration;
import org.nationsatwar.goldfish.gui.teleports.GUITeleportsList;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketActivatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.warp.PacketWarpPlayer;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUIScreen;

public class GUIPrototypesList extends GUIScreen {
	
	private GUIButton returnButton;
	private GUIButton activateButton;
	private GUIButton renameButton;
	private GUIButton deleteButton;
	private GUIButton warpButton;
	private GUIButton teleportsButton;
	private GUIButton configButton;
	private GUIButton previousButton;
	private GUIButton nextButton;
	
	private int prototypePage = 0;
	private boolean insidePrototype;
	
	public void setElements() {
		
		setWindow((width - 140) / 2, 20, 140, 180);
		
		returnButton = addButton(windowX + 10, windowY + 40, 60, 20, "Return");
		
		Prototype prototype = PrototypeManager.getPrototypeByIndex(prototypePage);
		
		if (prototype == null) {
			
			prototypePage = 0;
			GUIHandler.openGUI(new GUIMainMenu());
			return;
		}
		
		PrototypeManager.setActivePrototype(prototype);
		
		addLabel(windowX + 15, windowY + 20, prototype.getPrototypeName());

		activateButton = addButton(windowX + 72, windowY + 40, 60, 20, 
				(prototype.isActivated() ? "Deactivate" : "Activate"));
		renameButton = addButton(windowX + 10, windowY + 62, 60, 20, "Rename");
		deleteButton = addButton(windowX + 72, windowY + 62, 60, 20, "Delete");
		
		if (player.worldObj.provider.getDimensionName().equals(prototype.getPrototypeName()))
			insidePrototype = true;
		else
			insidePrototype = false;

		warpButton = addButton(windowX + 20, windowY + 84, 100, 20, 
				(insidePrototype ? "Warp out" : "Warp to Prototype"));

		teleportsButton = addButton(windowX + 20, windowY + 106, 100, 20, "Teleports List");
		configButton = addButton(windowX + 20, windowY + 128, 100, 20, "Configure");
		
		if (PrototypeManager.getPrototypeByIndex(prototypePage - 1) != null)
			previousButton = addButton(windowX + 20, windowY + 150, 20, 20, "<");
		
		if (PrototypeManager.getPrototypeByIndex(prototypePage + 1) != null)
			nextButton = addButton(windowX + 100, windowY + 150, 20, 20, ">");
	}
	
	protected void buttonClicked(GUIButton button) {
		
		// Return
		if (button.equals(returnButton))
			GUIHandler.openGUI(new GUIMainMenu());

		// Activate Prototype
		if (button.equals(activateButton)) {
			
			boolean activated = PrototypeManager.getActivePrototype().isActivated();
			int prototypeID = PrototypeManager.getActivePrototype().getPrototypeID();
			
			PrototypeManager.getActivePrototype().setActivated(!activated);
			Goldfish.channel.sendToServer(new PacketActivatePrototype(prototypeID, !activated));
			
			initGui();
		}
		
		// Rename Prototype
		if (button.equals(renameButton))
			GUIHandler.openGUI(new GUIRenamePrototype());
		
		// Delete Prototype
		if (button.equals(deleteButton))
			GUIHandler.openGUI(new GUIDeleteConfirm());
		
		// Warp to Prototype
		if (button.equals(warpButton)) {
			
			String playerUUID = player.getUniqueID().toString();
			if (insidePrototype)
				Goldfish.channel.sendToServer(new PacketWarpPlayer(playerUUID, "Overworld"));
			else
				Goldfish.channel.sendToServer(new PacketWarpPlayer(playerUUID, PrototypeManager.getActivePrototype().getPrototypeName()));
		}
		
		// Teleports Menu
		if (button.equals(teleportsButton))
			GUIHandler.openGUI(new GUITeleportsList());
		
		// Configure Prototype
		if (button.equals(configButton))
			GUIHandler.openGUI(new GUIConfiguration());
		
		// Previous Prototype
		if (button.equals(previousButton)) {
			
			prototypePage--;
			GUIHandler.openGUI(new GUIPrototypesList());
		}
		
		// Next Prototype
		if (button.equals(nextButton)) {
			
			prototypePage++;
			GUIHandler.openGUI(new GUIPrototypesList());
		}
	}
}
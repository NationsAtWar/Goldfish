package org.nationsatwar.goldfish.gui.config;

import org.nationsatwar.goldfish.gui.GUIMainMenu;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUIScreen;

public class GUIEquipment extends GUIScreen {
	
	private GUIButton returnButton;
	private GUIButton staticButton;
	private GUIButton boundariesButton;
	private GUIButton permissionsButton;
	private GUIButton respawnButton;
	private GUIButton timersButton;
	private GUIButton equipmentButton;
	private GUIButton conditionsButton;
	
	@Override
	protected void setElements() {
		
		setWindow((width - 140) / 2, 20, 140, 180);
		
		addLabel(windowX + 30, windowY + 15, "Configuration");
		
		returnButton = addButton(windowX + 10, windowY + 40, 60, 20, "Return");
		staticButton = addButton(windowX + 10, windowY + 60, 60, 20, "Static Instance");
		boundariesButton = addButton(windowX + 10, windowY + 80, 60, 20, "Map Boundaries");
		permissionsButton = addButton(windowX + 10, windowY + 100, 60, 20, "Block Permissions");
		respawnButton = addButton(windowX + 10, windowY + 120, 60, 20, "Respawn Options");
		timersButton = addButton(windowX + 10, windowY + 140, 60, 20, "Instance Timers");
		equipmentButton = addButton(windowX + 10, windowY + 160, 60, 20, "Equipment Storage");
		conditionsButton = addButton(windowX + 10, windowY + 180, 60, 20, "Conditional Entry");
	}
	
	@Override
	protected void buttonClicked(GUIButton button) {
		
		// Return
		if (button.equals(returnButton))
			GUIHandler.openGUI(new GUIMainMenu());
		
		// Static Entrance
		if (button.equals(staticButton))
			return;
		
		// Map Boundaries
		if (button.equals(boundariesButton))
			return;
		
		// Block Permissions
		if (button.equals(permissionsButton))
			return;
		
		// Respawn Options
		if (button.equals(respawnButton))
			return;
		
		// Instance Timers
		if (button.equals(timersButton))
			return;
		
		// Equipment Storage
		if (button.equals(equipmentButton))
			return;
		
		// Conditional Entry
		if (button.equals(conditionsButton))
			return;
	}
}
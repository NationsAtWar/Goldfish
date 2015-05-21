package org.nationsatwar.goldfish.gui.prototype;

import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.gui.GUIMainMenu;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketDeletePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.chat.ChatMessage;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUILabel;
import org.nationsatwar.palette.gui.GUIScreen;

public class GUIDeleteConfirm extends GUIScreen {
	
	private GUIButton confirmButton;
	private GUILabel errorLabel;
	
	@Override
	protected void setElements() {

		setWindow((width - 140) / 2, 64, 140, 84);
		
		confirmButton = addButton(windowX + 15, windowY + windowHeight - 20, 50, 20, "Confirm");
		addButton(windowX + windowWidth - 65, windowY + windowHeight - 20, 50, 20, "Cancel");
		
		String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();

		addLabel(windowX + (windowWidth / 2), windowY + 15, "Delete Prototype:").setCentered(true);
		addLabel(windowX + (windowWidth / 2), windowY + 28, prototypeName + "?").setCentered(true);
		
		errorLabel = addLabel(windowX + (windowWidth / 2), windowY + 48, "");
		errorLabel.setFontColor(FontColor.RED);
		errorLabel.setCentered(true);
	}
	
	@Override
	protected void buttonClicked(GUIButton button) {
		
		if (button.equals(confirmButton)) {
			
			Prototype prototype = PrototypeManager.getActivePrototype();
			
			// Can only delete a prototype if it is not occupied
			if (prototype != null && 
					DimensionManager.getWorld(prototype.getPrototypeID()).playerEntities.size() < 1) {
				
				String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();
				
				PrototypeManager.deletePrototype(prototypeName);
				Goldfish.channel.sendToServer(new PacketDeletePrototype(prototypeName));
				ChatMessage.sendMessage(player, "Prototype: '" + prototypeName + "' has been deleted.");
			} else {
				
				errorLabel.setText("World is occupied.");
				return;
			}
		}
		
		GUIHandler.openGUI(new GUIMainMenu(), true);
	}
}
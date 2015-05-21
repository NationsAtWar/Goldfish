package org.nationsatwar.goldfish.gui.prototype;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketCreatePrototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.chat.ChatMessage;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUIScreen;

public class GUICreateConfirm extends GUIScreen {
	
	private GUIButton confirmButton;
	
	public void setElements() {
		
		setWindow((width - 140) / 2, 64, 140, 64);
		
		confirmButton = addButton(windowX + 15, windowY + windowHeight - 20, 50, 20, "Confirm");
		addButton(windowX + windowWidth - 65, windowY + windowHeight - 20, 50, 20, "Cancel");
		
		String prototypeName = PrototypeManager.getCreatePrototypeName();

		addLabel(windowX + (windowWidth / 2), windowY + 15, "Create Prototype:").setCentered(true);
		addLabel(windowX + (windowWidth / 2), windowY + 28, prototypeName + "?").setCentered(true);
	}
	
	protected void buttonClicked(GUIButton button) {
		
		if (button.equals(confirmButton)) {
			
			String prototypeName = PrototypeManager.getCreatePrototypeName();
			
			System.out.println("Suspect 2");
			
			Goldfish.channel.sendToServer(new PacketCreatePrototype(prototypeName, 0));
			ChatMessage.sendMessage(player, "Prototype: '" + prototypeName + "' has been created.");
		}
		
		GUIHandler.openGUI(new GUIPrototypesList(), true);
	}
}
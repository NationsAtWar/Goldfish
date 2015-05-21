package org.nationsatwar.goldfish.gui.teleports;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.message.PacketSetTeleportMessage;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.goldfish.util.Constants;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUILabel;
import org.nationsatwar.palette.gui.GUIScreen;
import org.nationsatwar.palette.gui.GUITextField;

public class GUISetTeleportMessage extends GUIScreen {
	
	private GUITextField messageTextField;
	private GUIButton confirmButton;
	private GUILabel errorLabel;
	
	@Override
	protected void setElements() {
		
		setWindow((width - 140) / 2, 20, 140, 100);
		
		confirmButton = addButton(windowX + 20, windowY + 60, 50, 20, "Confirm");
		addButton(windowX + 70, windowY + 60, 50, 20, "Cancel");
		
		String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();
		
		addLabel(windowX + 10, windowY + 12, "Set Label: " + prototypeName);
		errorLabel = addLabel(windowX + 10, windowY + 160, "");
		
		int activeTeleportPointID = TeleportsManager.getActiveTeleportPointID();
		
		String teleportMessage = PrototypeManager.getActivePrototype().getTeleportPoint(activeTeleportPointID).getMessage();
		
		messageTextField = addTextField(windowX + 20, windowY + 30, 100, 20, teleportMessage);
		messageTextField.setMaxStringLength(PrototypeManager.MAX_PROTOTYPE_NAME_LENGTH);
	}
	
	@Override
	protected void buttonClicked(GUIButton button) {
		
		// Sets the new Teleport Label
		if (button.equals(confirmButton)) {
			
			if (messageTextField.getText().matches(Constants.PROTOTYPE_NAME_REGEX)) {
				
				Prototype prototype = PrototypeManager.getActivePrototype();
				int teleportID = TeleportsManager.getActiveTeleportPointID();
				
				TeleportsManager.setMessage(prototype, messageTextField.getText(), teleportID, false);
				
				Goldfish.channel.sendToServer(new PacketSetTeleportMessage(prototype.getPrototypeID(), 
						teleportID, messageTextField.getText()));
				GUIHandler.openGUI(new GUITeleportsList(), true);
			}
			else if (messageTextField.getText() == "")
				errorLabel.setText("Enter a teleport label");
			else
				errorLabel.setText("Enter valid characters");

			messageTextField.setText("");
			return;
		}
		
		// Cancel - Returns to Teleports List
		GUIHandler.openGUI(new GUITeleportsList(), true);
	}
}
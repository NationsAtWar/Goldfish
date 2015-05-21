package org.nationsatwar.goldfish.gui.prototype;

import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.prototypes.rename.PacketRenamePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.Constants;
import org.nationsatwar.palette.chat.ChatMessage;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUILabel;
import org.nationsatwar.palette.gui.GUIScreen;
import org.nationsatwar.palette.gui.GUITextField;

public class GUIRenamePrototype extends GUIScreen {
	
	private GUITextField renameTextField;
	private GUIButton confirmButton;
	private GUILabel errorLabel;
	
	@Override
	public void setElements() {

		setWindow((width - 140) / 2, 64, 140, 84);
		
		confirmButton = addButton(windowX + 15, windowY + windowHeight - 20, 50, 20, "Confirm");
		addButton(windowX + windowWidth - 65, windowY + windowHeight - 20, 50, 20, "Cancel");
		
		String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();

		addLabel(windowX + (windowWidth / 2), windowY + 15, "Rename Prototype:").setCentered(true);
		addLabel(windowX + (windowWidth / 2), windowY + 28, prototypeName + "?").setCentered(true);
		
		errorLabel = addLabel(windowX + (windowWidth / 2), windowY + 48, "");
		errorLabel.setFontColor(FontColor.RED);
		errorLabel.setCentered(true);
		
		renameTextField = addTextField(windowX + 20, windowY + 30, 100, 20, prototypeName);
		renameTextField.setRegEx(Constants.PROTOTYPE_NAME_REGEX);
		renameTextField.setMaxStringLength(PrototypeManager.MAX_PROTOTYPE_NAME_LENGTH);
	}
	
	@Override
	protected void buttonClicked(GUIButton button) {
		
		// Renames Prototype
		if (button.equals(confirmButton)) {
			
			if (renameTextField.getText().matches(Constants.PROTOTYPE_NAME_REGEX)) {
				
				Prototype prototype = PrototypeManager.getActivePrototype();
				
				// Can only rename the prototype if it is not currently occupied
				if (DimensionManager.getWorld(prototype.getPrototypeID()).playerEntities.size() < 1) {
					
					String oldPrototypeName = prototype.getPrototypeName();
					
					Goldfish.channel.sendToServer(new PacketRenamePrototype(prototype.getPrototypeName(), renameTextField.getText()));
					prototype.renamePrototype(renameTextField.getText());
					GUIHandler.openGUI(new GUIPrototypesList(), true);
					
					ChatMessage.sendMessage(player, "Prototype: '" + oldPrototypeName + 
							"' has been renamed to: " + prototype.getPrototypeName());
				} else
					errorLabel.setText("Can't rename a world that is occupied.");
			}
			else if (errorLabel.getText() == "")
				errorLabel.setText("Enter a prototype name");
			else
				errorLabel.setText("Enter valid characters");

			renameTextField.setText("");
		}
		
		// Cancel - Returns to Prototype List
		if (button.id == 1)
			GUIHandler.openGUI(new GUIPrototypesList(), true);
	}
}
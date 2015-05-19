package org.nationsatwar.goldfish.gui;

import org.nationsatwar.goldfish.gui.prototype.GUICreateConfirm;
import org.nationsatwar.goldfish.gui.prototype.GUIPrototypesList;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.Constants;
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUILabel;
import org.nationsatwar.palette.gui.GUIScreen;
import org.nationsatwar.palette.gui.GUITextField;

public class GUIMainMenu extends GUIScreen {
	
	private GUIButton prototypeList;
	private GUIButton createPrototype;
	
	private GUITextField createTextField;
	private GUILabel errorLabel;
	
	@Override
	protected void setElements() {
		
		setWindow((width - 140) / 2, 20, 140, 180);
		
		prototypeList = addButton(windowX + 20, windowY + 40, 100, 20, "Prototype List");
		createPrototype = addButton(windowX + 20, windowY + 100, 100, 20, "Create Prototype");
		
		createTextField = addTextField(windowX + 20, windowY + 130, 100, 20, "");
		createTextField.setRegEx(Constants.PROTOTYPE_NAME_REGEX);
		createTextField.setMaxStringLength(PrototypeManager.MAX_PROTOTYPE_NAME_LENGTH);
		
		GUILabel mainLabel = addLabel(windowX + (windowWidth / 2), windowY + 18, "Goldfish");
		mainLabel.setFontColor(0xCCAA22);
		mainLabel.setCentered(true);
		mainLabel.setSizeDoubled(true);
		
		errorLabel = addLabel(windowX + 10, windowY + 160, "");
		errorLabel.setFontColor(FontColor.RED);
		errorLabel.setCentered(true);
	}
	
	@Override
	protected void buttonClicked(GUIButton button) {
		
		if (button.equals(prototypeList))
			GUIHandler.openGUI(new GUIPrototypesList());
		
		if (button.equals(createPrototype)) {
			
			if (createTextField.getText().matches(Constants.PROTOTYPE_NAME_REGEX)) {
				
				PrototypeManager.setCreatePrototypeName(createTextField.getText());
				GUIHandler.openGUI(new GUICreateConfirm());
			}
			else if (createTextField.getText() == "")
				errorLabel.setText("Enter a prototype name");
			else
				errorLabel.setText("Enter valid characters");

			createTextField.setText("");
		}
	}
}
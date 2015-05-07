package org.nationsatwar.goldfish.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.PacketRenamePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.Constants;

public class RenamePrototypeGUI extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	private int windowX, windowY, windowWidth, windowHeight;
	
	private GuiTextField renameTextbox;

	private String errorText = "";
	
	public RenamePrototypeGUI(EntityPlayer player, World world, int x, int y, int z) {
		
		this.player = player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		windowWidth = 140;
		windowHeight = 100;
		windowX = (width - windowWidth) / 2;
		windowY = (height - windowHeight) / 2 - 20;
		
		buttonList.clear();
		
		GuiButton confirmButton = new GuiButton(0, windowX + 20, windowY + 60, 50, 20, "Confirm");
		buttonList.add(confirmButton);
		
		GuiButton cancelButton = new GuiButton(1, windowX + 70, windowY + 60, 50, 20, "Cancel");
		buttonList.add(cancelButton);
		
		renameTextbox = new GuiTextField(2, fontRendererObj, windowX + 20, windowY + 30, 100, 20);
		renameTextbox.setMaxStringLength(PrototypeManager.MAX_PROTOTYPE_NAME_LENGTH);
		renameTextbox.setText(PrototypeManager.getActivePrototype().getPrototypeName());
		renameTextbox.setCanLoseFocus(false);
		renameTextbox.setFocused(true);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		String charString = "" + par1;
		
		// Only accept valid characters. 8 is the key code for backspace, 27 for escape
		if (!charString.matches(Constants.PROTOTYPE_NAME_REGEX) && par1 != 8 && par1 != 27)
			return;
		
		super.keyTyped(par1, par2);
		renameTextbox.textboxKeyTyped(par1, par2);
	}
	
	@Override
	public void updateScreen() {
		
		super.updateScreen();
		renameTextbox.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		this.mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		
		renameTextbox.drawTextBox();
		
		if (!PrototypeManager.activePrototypeIsSet())
			return;
		
		String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();
		
		drawString(fontRendererObj, "Rename: " + prototypeName + "?", 
				windowX + 10, windowY + 12, 0xEE8888);
		
		drawString(fontRendererObj, errorText, windowX + 10, windowY + 160, 0xCC2222);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		
		super.mouseClicked(x, y, btn);
		renameTextbox.mouseClicked(x, y, btn);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Creates Prototype
		if (button.id == 0) {
			
			if (renameTextbox.getText().matches(Constants.PROTOTYPE_NAME_REGEX)) {
				
				Prototype prototype = PrototypeManager.getActivePrototype();
				
				Goldfish.channel.sendToServer(new PacketRenamePrototype(prototype.getPrototypeName(), renameTextbox.getText()));
				prototype.renamePrototype(renameTextbox.getText());
				player.openGui(Goldfish.instance, GUIHandler.LIST_GUI_ID, player.getEntityWorld(), 0, 0, 0);
			}
			else if (renameTextbox.getText() == "")
				errorText = "Enter a prototype name";
			else
				errorText = "Enter valid characters";

			renameTextbox.setText("");
		}
		
		// Cancel - Returns to Prototype List
		if (button.id == 1)
			player.openGui(Goldfish.instance, GUIHandler.LIST_GUI_ID, player.getEntityWorld(), 0, 0, 0);
	}
}
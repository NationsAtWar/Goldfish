package org.nationsatwar.goldfish.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;
import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.Constants;

public class MainGUI extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	
	private GuiTextField createTextbox;
	private String errorText = "";
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	public MainGUI(EntityPlayer player, World world, int x, int y, int z) {
		
		this.player = player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		windowWidth = 140;
		windowHeight = 180;
		windowX = (width - windowWidth) / 2;
		windowY = (height - windowHeight) / 2 - 20;
		
		buttonList.clear();
		
		GuiButton listPrototypes = new GuiButton(0, windowX + 20, windowY + 40, 100, 20, "Prototype List");
		buttonList.add(listPrototypes);
		
		GuiButton createPrototype = new GuiButton(1, windowX + 20, windowY + 100, 100, 20, "Create Prototype");
		buttonList.add(createPrototype);
		
		createTextbox = new GuiTextField(1, fontRendererObj, windowX + 20, windowY + 130, 100, 20);
		createTextbox.setMaxStringLength(PrototypeManager.MAX_PROTOTYPE_NAME_LENGTH);
		createTextbox.setText("");
		createTextbox.setCanLoseFocus(false);
		createTextbox.setFocused(true);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		String charString = "" + par1;
		
		// Only accept valid characters. 8 is the key code for backspace, 27 for escape
		if (!charString.matches(Constants.PROTOTYPE_NAME_REGEX) && par1 != 8 && par1 != 27)
			return;
		
		super.keyTyped(par1, par2);
		createTextbox.textboxKeyTyped(par1, par2);
	}
	
	@Override
	public void updateScreen() {
		
		super.updateScreen();
		createTextbox.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		// Draws the background window
		mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		createTextbox.drawTextBox();
		
		GL11.glPushMatrix();
		GL11.glScaled(2, 2, 2);
		drawString(fontRendererObj, "Goldfish", (windowX + 30) / 2, (windowY + 15) / 2, 0xCCAA22);
		GL11.glPopMatrix();
		
		drawString(fontRendererObj, errorText, windowX + 10, windowY + 160, 0xCC2222);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		
		super.mouseClicked(x, y, btn);
		createTextbox.mouseClicked(x, y, btn);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Creates Prototype
		if (button.id == 0)
			player.openGui(Goldfish.instance, GUIHandler.LIST_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Creates Prototype
		if (button.id == 1) {
			
			if (createTextbox.getText().matches(Constants.PROTOTYPE_NAME_REGEX)) {
				
				PrototypeManager.setCreatePrototypeName(createTextbox.getText());
				player.openGui(Goldfish.instance, GUIHandler.CREATE_CONFIRM_GUI_ID, player.getEntityWorld(), 0, 0, 0);
			}
			else if (createTextbox.getText() == "")
				errorText = "Enter a prototype name";
			else
				errorText = "Enter valid characters";

			createTextbox.setText("");
		}
	}
}
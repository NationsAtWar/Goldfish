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

public class TeleportsGUI extends GuiScreen {
	
	private final static int maxPrototypeName = 20;
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	
	private GuiTextField prototypeName;
	private String errorText = "";
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	public static final int GUI_ID = 20;
	
	public TeleportsGUI(EntityPlayer player, World world, int x, int y, int z) {
		
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
		
		prototypeName = new GuiTextField(1, fontRendererObj, windowX + 20, windowY + 130, 100, 20);
		prototypeName.setMaxStringLength(maxPrototypeName);
		prototypeName.setText("");
		prototypeName.setCanLoseFocus(false);
		prototypeName.setFocused(true);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		super.keyTyped(par1, par2);
		prototypeName.textboxKeyTyped(par1, par2);
	}
	
	@Override
	public void updateScreen() {
		
		super.updateScreen();
		prototypeName.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		// Draws the background window
		mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		prototypeName.drawTextBox();
		
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
		prototypeName.mouseClicked(x, y, btn);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Creates Prototype
		if (button.id == 0) {
			
			player.closeScreen();
		}
	}
}
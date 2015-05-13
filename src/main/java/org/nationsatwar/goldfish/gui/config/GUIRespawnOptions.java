package org.nationsatwar.goldfish.gui.config;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.gui.GUIHandler;

public class GUIRespawnOptions extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	public GUIRespawnOptions(EntityPlayer player, World world, int x, int y, int z) {
		
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
		
		GuiButton returnButton = new GuiButton(0, windowX + 10, windowY + 40, 60, 20, "Return");
		buttonList.add(returnButton);
		
		GuiButton staticButton = new GuiButton(1, windowX + 10, windowY + 60, 60, 20, "Static Instance");
		buttonList.add(staticButton);
		
		GuiButton boundariesButton = new GuiButton(2, windowX + 10, windowY + 80, 60, 20, "Map Boundaries");
		buttonList.add(boundariesButton);
		
		GuiButton permissionsButton = new GuiButton(3, windowX + 10, windowY + 100, 60, 20, "Block Permissions");
		buttonList.add(permissionsButton);
		
		GuiButton respawnButton = new GuiButton(4, windowX + 10, windowY + 120, 60, 20, "Respawn Options");
		buttonList.add(respawnButton);
		
		GuiButton timersButton = new GuiButton(5, windowX + 10, windowY + 140, 60, 20, "Instance Timers");
		buttonList.add(timersButton);
		
		GuiButton equipmentButton = new GuiButton(6, windowX + 10, windowY + 160, 60, 20, "Equipment Storage");
		buttonList.add(equipmentButton);
		
		GuiButton conditionsButton = new GuiButton(7, windowX + 10, windowY + 180, 60, 20, "Conditional Entry");
		buttonList.add(conditionsButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		// Draws the background window
		mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		
		drawString(fontRendererObj, "Configuration", (windowX + 30), (windowY + 15), 0xCCAA22);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Return
		if (button.id == 0)
			player.openGui(Goldfish.instance, GUIHandler.MAIN_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Static Entrance
		if (button.id == 1)
			return;
		
		// Map Boundaries
		if (button.id == 2)
			return;
		
		// Block Permissions
		if (button.id == 3)
			return;
		
		// Respawn Options
		if (button.id == 4)
			return;
		
		// Instance Timers
		if (button.id == 5)
			return;
		
		// Equipment Storage
		if (button.id == 6)
			return;
		
		// Conditional Entry
		if (button.id == 7)
			return;
	}
}
package org.nationsatwar.goldfish.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketActivatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.warp.PacketWarpPlayer;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;

public class GUIPrototypesList extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	private int prototypePage = 0;
	private boolean insidePrototype;
	
	public static final int GUI_ID = 20;
	
	public GUIPrototypesList(EntityPlayer player, World world, int x, int y, int z) {
		
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
		
		Prototype prototype = PrototypeManager.getPrototypeByIndex(prototypePage);
		
		if (prototype == null)
			return;
		
		PrototypeManager.setActivePrototype(prototype);
		
		GuiButton activatePrototype = new GuiButton(1, windowX + 72, windowY + 40, 60, 20, 
				(prototype.isActivated() ? "Deactivate" : "Activate"));
		buttonList.add(activatePrototype);
		
		GuiButton renamePrototype = new GuiButton(2, windowX + 10, windowY + 62, 60, 20, "Rename");
		buttonList.add(renamePrototype);
		
		GuiButton deletePrototype = new GuiButton(3, windowX + 72, windowY + 62, 60, 20, "Delete");
		buttonList.add(deletePrototype);
		
		if (player.worldObj.provider.getDimensionName().equals(prototype.getPrototypeName()))
			insidePrototype = true;
		else
			insidePrototype = false;
		
		GuiButton warpToPrototype = new GuiButton(4, windowX + 20, windowY + 84, 100, 20, 
				(insidePrototype ? "Warp out" : "Warp to Prototype"));
		buttonList.add(warpToPrototype);
		
		GuiButton prototypeTeleports = new GuiButton(5, windowX + 20, windowY + 106, 100, 20, "Teleports List");
		buttonList.add(prototypeTeleports);
		
		GuiButton prototypeConfig = new GuiButton(6, windowX + 20, windowY + 128, 100, 20, "Configure");
		buttonList.add(prototypeConfig);
		
		if (PrototypeManager.getPrototypeByIndex(prototypePage - 1) != null) {
			
			GuiButton previousPage = new GuiButton(7, windowX + 20, windowY + 150, 20, 20, "<");
			buttonList.add(previousPage);
		}
		
		if (PrototypeManager.getPrototypeByIndex(prototypePage + 1) != null) {
			
			GuiButton nextPage = new GuiButton(8, windowX + 100, windowY + 150, 20, 20, ">");
			buttonList.add(nextPage);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		// Draws the background window
		mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		
		if (!PrototypeManager.activePrototypeIsSet())
			return;
		
		drawString(fontRendererObj, PrototypeManager.getActivePrototype().getPrototypeName(), (windowX + 30), (windowY + 15), 0xCCAA22);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Forge Bug Fix - Don't ask
		if(!button.isMouseOver())
			return;
		
		// Return
		if (button.id == 0)
			player.openGui(Goldfish.instance, GUIHandler.MAIN_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Activate Prototype
		if (button.id == 1) {
			
			boolean activated = PrototypeManager.getActivePrototype().isActivated();
			int prototypeID = PrototypeManager.getActivePrototype().getPrototypeID();
			
			PrototypeManager.getActivePrototype().setActivated(!activated);
			Goldfish.channel.sendToServer(new PacketActivatePrototype(prototypeID, !activated));
			
			initGui();
		}
		
		// Rename Prototype
		if (button.id == 2)
			player.openGui(Goldfish.instance, GUIHandler.RENAME_PROTOTYPE_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Delete Prototype
		if (button.id == 3)
			player.openGui(Goldfish.instance, GUIHandler.DELETE_CONFIRM_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Warp to Prototype
		if (button.id == 4) {
			
			String playerUUID = player.getUniqueID().toString();
			if (insidePrototype)
				Goldfish.channel.sendToServer(new PacketWarpPlayer(playerUUID, "Overworld"));
			else
				Goldfish.channel.sendToServer(new PacketWarpPlayer(playerUUID, PrototypeManager.getActivePrototype().getPrototypeName()));
		}
		
		// Teleports Menu
		if (button.id == 5)
			player.openGui(Goldfish.instance, GUIHandler.TELEPORTS_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Configure Prototype
		if (button.id == 6) {
			
			
		}
		
		// Previous Prototype
		if (button.id == 7) {
			
			prototypePage--;
			initGui();
		}
		
		// Next Prototype
		if (button.id == 8) {
			
			prototypePage++;
			initGui();
		}
	}
}
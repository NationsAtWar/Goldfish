package org.nationsatwar.goldfish.gui.prototype;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.gui.GUIHandler;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketDeletePrototype;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.chat.ChatMessage;

public class GUIDeleteConfirm extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	
	private String errorText = "";
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	public GUIDeleteConfirm(EntityPlayer player, World world, int x, int y, int z) {
		
		this.player = player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		windowWidth = 140;
		windowHeight = 80;
		windowX = (width - windowWidth) / 2;
		windowY = (height - windowHeight) / 2 - 20;
		
		buttonList.clear();
		
		GuiButton confirmButton = new GuiButton(0, windowX + 10, windowY + 30, 50, 20, "Confirm");
		buttonList.add(confirmButton);
		
		GuiButton cancelButton = new GuiButton(1, windowX + 80, windowY + 30, 50, 20, "Cancel");
		buttonList.add(cancelButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		this.mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		
		if (!PrototypeManager.activePrototypeIsSet())
			return;
		
		String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();
		
		drawString(fontRendererObj, "Create: " + prototypeName + "?", 
				windowX + 10, windowY + 12, 0xEE8888);
		
		drawString(fontRendererObj, errorText, windowX + 10, windowY + 60, 0xCC2222);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Confirm Button - Deletes Prototype
		if (button.id == 0) {
			
			Prototype prototype = PrototypeManager.getActivePrototype();
			
			// Can only delete a prototype if it is not occupied
			if (DimensionManager.getWorld(prototype.getPrototypeID()).playerEntities.size() < 1) {
				
				String prototypeName = PrototypeManager.getActivePrototype().getPrototypeName();
				
				Goldfish.channel.sendToServer(new PacketDeletePrototype(prototypeName));
				ChatMessage.sendMessage(player, "Prototype: '" + prototypeName + "' has been deleted.");
			} else
				errorText = "Can't delete a world that is occupied.";
		}
		
		player.openGui(Goldfish.instance, GUIHandler.MAIN_GUI_ID, player.getEntityWorld(), 0, 0, 0);
	}
}
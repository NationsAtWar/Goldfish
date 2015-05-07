package org.nationsatwar.goldfish.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.PacketCreatePrototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.chat.ChatMessage;

public class CreateConfirmGUI extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	private int windowX, windowY, windowWidth, windowHeight;
	
	public CreateConfirmGUI(EntityPlayer player, World world, int x, int y, int z) {
		
		this.player = player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		windowWidth = 140;
		windowHeight = 64;
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
		
		String prototypeName = PrototypeManager.getCreatePrototypeName();
		
		drawString(fontRendererObj, "Create: " + prototypeName + "?", 
				windowX + 10, windowY + 12, 0xEE8888);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		
		return false;
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		
		// Confirm Button - Creates Prototype
		if (button.id == 0) {
			
			String prototypeName = PrototypeManager.getCreatePrototypeName();
			
			Goldfish.channel.sendToServer(new PacketCreatePrototype(prototypeName, 0));
			ChatMessage.sendMessage(player, "Prototype: '" + prototypeName + "' has been created.");
		}
		
		player.openGui(Goldfish.instance, GUIHandler.MAIN_GUI_ID, player.getEntityWorld(), 0, 0, 0);
	}
}
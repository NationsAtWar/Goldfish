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
import org.nationsatwar.goldfish.packets.teleports.PacketAddTeleport;
import org.nationsatwar.goldfish.packets.teleports.PacketSetMessageRadius;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportDest;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportRadius;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportSource;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportPoint;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.goldfish.util.Constants;

public class GUITeleportsList extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/GuiBackground.png");
	
	private EntityPlayer player;
	private Prototype prototype;
	
	private int windowX, windowY, windowWidth, windowHeight;
	
	private GuiTextField messageRadius;
	private GuiTextField teleportRadius;
	
	private boolean messageRadiusActive = true;
	private boolean teleportRadiusActive = false;
	
	private int teleportsPage = 0;
	
	public static final int GUI_ID = 20;
	
	public GUITeleportsList(EntityPlayer player, World world, int x, int y, int z) {
		
		this.player = player;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		
		windowWidth = 160;
		windowHeight = 180;
		windowX = (width - windowWidth) / 2;
		windowY = (height - windowHeight) / 2 - 20;
		
		buttonList.clear();
		
		GuiButton returnButton = new GuiButton(0, windowX + 10, windowY + 40, 70, 20, "Return");
		buttonList.add(returnButton);
		
		GuiButton addTeleport = new GuiButton(1, windowX + 82, windowY + 40, 70, 20, "Add Teleport");
		buttonList.add(addTeleport);
		
		prototype = PrototypeManager.getActivePrototype();
		
		if (prototype == null)
			return;
		
		TeleportPoint teleportPoint = null;
		
		if (prototype.numberofTeleportPoints() > teleportsPage)
			teleportPoint = prototype.getTeleportPoint(teleportsPage);
		
		if (teleportPoint == null)
			return;
		
		GuiButton setLabel = new GuiButton(2, windowX + 10, windowY + 62, 70, 20, "Set Label");
		buttonList.add(setLabel);
		
		GuiButton setMessage = new GuiButton(3, windowX + 82, windowY + 62, 70, 20, "Set Message");
		buttonList.add(setMessage);
		
		GuiButton setSource = new GuiButton(4, windowX + 10, windowY + 84, 70, 20, "Set Origin");
		buttonList.add(setSource);
		
		GuiButton setDest = new GuiButton(5, windowX + 82, windowY + 84, 70, 20, "Set Dest");
		buttonList.add(setDest);
		
		messageRadius = new GuiTextField(6, fontRendererObj, windowX + 10, windowY + 106, 20, 20);
		messageRadius.setText(teleportPoint.getMessageRadius() + "");
		messageRadius.setFocused(true);
		
		teleportRadius = new GuiTextField(7, fontRendererObj, windowX + 82, windowY + 106, 20, 20);
		teleportRadius.setText(teleportPoint.getTeleportRadius() + "");
		teleportRadius.setFocused(false);
		
		if (teleportsPage > 0) {
			
			GuiButton previousPage = new GuiButton(8, windowX + 20, windowY + 150, 20, 20, "<");
			buttonList.add(previousPage);
		}
		
		if (prototype.numberofTeleportPoints() > teleportsPage + 1) {
			
			GuiButton nextPage = new GuiButton(9, windowX + 100, windowY + 150, 20, 20, ">");
			buttonList.add(nextPage);
		}
		
		GuiButton activateMessageRadius = new GuiButton(10, windowX + 10, windowY + 106, 20, 20, "");
		buttonList.add(activateMessageRadius);
		activateMessageRadius.enabled = false;
		
		GuiButton activateTeleportRadius = new GuiButton(11, windowX + 82, windowY + 106, 20, 20, "");
		buttonList.add(activateTeleportRadius);
		activateMessageRadius.enabled = false;
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		String charString = "" + par1;
		
		// Only accept valid characters. 8 is the key code for backspace, 27 for escape
		if (!charString.matches(Constants.NUMBERS_ONLY_REGEX) && par1 != 8 && par1 != 27)
			return;
		
		super.keyTyped(par1, par2);
		
		if (messageRadiusActive)
			messageRadius.textboxKeyTyped(par1, par2);
		if (teleportRadiusActive)
			teleportRadius.textboxKeyTyped(par1, par2);
		
		try {
			
			int messageRadiusInteger = Integer.parseInt(messageRadius.getText());
			int teleportRadiusInteger = Integer.parseInt(teleportRadius.getText());
			
			TeleportsManager.setMessageRadius(prototype, messageRadiusInteger, teleportsPage);
			TeleportsManager.setTeleportRadius(prototype, teleportRadiusInteger, teleportsPage);
			
			Goldfish.channel.sendToServer(new PacketSetMessageRadius(messageRadiusInteger, 
					prototype.getPrototypeID(), teleportsPage));
			
			Goldfish.channel.sendToServer(new PacketSetTeleportRadius(teleportRadiusInteger, 
					prototype.getPrototypeID(), teleportsPage));
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void updateScreen() {
		
		super.updateScreen();
		
		if (messageRadiusActive && messageRadius != null)
			messageRadius.updateCursorCounter();
		if (teleportRadiusActive && teleportRadius != null)
			teleportRadius.updateCursorCounter();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
		
		// Draws the background window
		mc.getTextureManager().bindTexture(backgroundimage);
		drawTexturedModalRect(windowX, windowY, 0, 0, windowWidth,  windowHeight);
		
		if (messageRadius != null)
			messageRadius.drawTextBox();
		if (teleportRadius != null)
			teleportRadius.drawTextBox();
		
		GL11.glPushMatrix();
		GL11.glScaled(2, 2, 2);
		drawString(fontRendererObj, "Goldfish", (windowX + 30) / 2, (windowY + 15) / 2, 0xCCAA22);
		GL11.glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		
		super.mouseClicked(x, y, btn);
		
		if (messageRadiusActive && messageRadius != null)
			messageRadius.mouseClicked(x, y, btn);
		if (teleportRadiusActive && teleportRadius != null)
			teleportRadius.mouseClicked(x, y, btn);
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
			player.openGui(Goldfish.instance, GUIHandler.LIST_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Add Teleport
		if (button.id == 1) {
			
			TeleportsManager.addTeleport(prototype, player, teleportsPage);
			
			Goldfish.channel.sendToServer(new PacketAddTeleport(player.getUniqueID().toString(), 
					prototype.getPrototypeID(), teleportsPage));
			
			initGui();
		}
		
		// Label Teleport
		if (button.id == 2)
			player.openGui(Goldfish.instance, GUIHandler.SET_TELEPORT_LABEL_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Set Text
		if (button.id == 3)
			player.openGui(Goldfish.instance, GUIHandler.SET_TELEPORT_MESSAGE_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Set Source Location
		if (button.id == 4) {
			
			TeleportsManager.setSourcePoint(prototype, player, teleportsPage);
			
			Goldfish.channel.sendToServer(new PacketSetTeleportSource(player.getUniqueID().toString(), 
					prototype.getPrototypeID(), teleportsPage));
		}
		
		// Set Destination Location
		if (button.id == 5) {
			
			TeleportsManager.setDestPoint(prototype, player, teleportsPage);
			
			Goldfish.channel.sendToServer(new PacketSetTeleportDest(player.getUniqueID().toString(), 
					prototype.getPrototypeID(), teleportsPage));
		}
				
		// Previous Page
		if (button.id == 8) {
			
			teleportsPage--;
			initGui();
		}
		
		// Next Page
		if (button.id == 9) {
			
			teleportsPage++;
			initGui();
		}
		
		// Activate Message Radius
		if (button.id == 10)
			activateMessageRadius();
		
		// Activate Teleport Radius
		if (button.id == 11)
			activateTeleportRadius();
	}
	
	private void activateMessageRadius() {

		messageRadiusActive = true;
		teleportRadiusActive = false;

		teleportRadius.setFocused(false);
		messageRadius.setFocused(true);
	}
	
	private void activateTeleportRadius() {

		messageRadiusActive = false;
		teleportRadiusActive = true;

		messageRadius.setFocused(false);
		teleportRadius.setFocused(true);
	}
}
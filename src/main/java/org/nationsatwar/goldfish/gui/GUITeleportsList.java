package org.nationsatwar.goldfish.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.add.PacketAddTeleport;
import org.nationsatwar.goldfish.packets.teleports.messageradius.PacketSetMessageRadius;
import org.nationsatwar.goldfish.packets.teleports.remove.PacketRemoveTeleport;
import org.nationsatwar.goldfish.packets.teleports.teleportdest.PacketSetTeleportDest;
import org.nationsatwar.goldfish.packets.teleports.teleportradius.PacketSetTeleportRadius;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketSetTeleportSource;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportPoint;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.goldfish.util.Constants;
import org.nationsatwar.palette.WorldLocation;
import org.nationsatwar.palette.chat.ChatMessage;

public class GUITeleportsList extends GuiScreen {
	
	private ResourceLocation backgroundimage = new ResourceLocation(Goldfish.MODID + ":" + 
			"textures/client/gui/TeleportsBackground.png");
	
	private EntityPlayer player;
	private Prototype prototype;
	private TeleportPoint teleportPoint;
	
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
		windowHeight = 196;
		windowX = (width - windowWidth) / 2;
		windowY = (height - windowHeight) / 2 - 20;
		
		buttonList.clear();
		
		GuiButton returnButton = new GuiButton(0, 
				windowX + (windowWidth / 2) - 30, windowY + 150, 60, 20, "Return");
		buttonList.add(returnButton);
		
		GuiButton addTeleport = new GuiButton(1, windowX + 10, windowY + 40, 70, 20, "Add");
		buttonList.add(addTeleport);
		
		prototype = PrototypeManager.getActivePrototype();
		
		if (prototype == null)
			return;
		
		if (prototype.numberofTeleportPoints() > teleportsPage)
			teleportPoint = prototype.getTeleportPoint(teleportsPage);
		
		if (teleportPoint == null)
			return;
		
		TeleportsManager.setActiveTeleportPointID(teleportsPage);
		
		GuiButton removeTeleport = new GuiButton(2, windowX + 82, windowY + 40, 70, 20, "Remove");
		buttonList.add(removeTeleport);
		
		GuiButton setLabel = new GuiButton(3, windowX + 10, windowY + 62, 70, 20, "Set Label");
		buttonList.add(setLabel);
		
		GuiButton setMessage = new GuiButton(4, windowX + 82, windowY + 62, 70, 20, "Set Message");
		buttonList.add(setMessage);
		
		GuiButton setSource = new GuiButton(5, windowX + 10, windowY + 106, 70, 20, "Set Origin");
		buttonList.add(setSource);
		
		GuiButton setDest = new GuiButton(6, 
				windowX + (windowWidth / 2) + 2, windowY + 106, 70, 20, "Set Dest");
		buttonList.add(setDest);
		
		messageRadius = new GuiTextField(7, fontRendererObj, 
				windowX + (windowWidth / 2) - 20, windowY + 128, 20, 20);
		messageRadius.setText(teleportPoint.getMessageRadius() + "");
		messageRadius.setFocused(true);
		messageRadius.setMaxStringLength(2);
		
		teleportRadius = new GuiTextField(8, fontRendererObj, 
				windowX + windowWidth - 30, windowY + 128, 20, 20);
		teleportRadius.setText(teleportPoint.getTeleportRadius() + "");
		teleportRadius.setFocused(false);
		teleportRadius.setMaxStringLength(2);
		
		if (teleportsPage > 0) {
			
			GuiButton previousPage = new GuiButton(9, 
					windowX + 10, windowY + 150, 20, 20, "<");
			buttonList.add(previousPage);
		}
		
		if (prototype.numberofTeleportPoints() > teleportsPage + 1) {
			
			GuiButton nextPage = new GuiButton(10, 
					windowX + windowWidth - 30, windowY + 150, 20, 20, ">");
			buttonList.add(nextPage);
		}
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		String charString = "" + par1;
		
		// Only accept valid characters. 8 is the key code for backspace, 27 for escape
		if (!charString.matches(Constants.NUMBERS_ONLY_REGEX) && par1 != 8 && par1 != 27)
			return;
		
		super.keyTyped(par1, par2);
		
		if (teleportPoint == null)
			return;
		
		if (messageRadiusActive)
			messageRadius.textboxKeyTyped(par1, par2);
		if (teleportRadiusActive)
			teleportRadius.textboxKeyTyped(par1, par2);
		
		try {
			
			int messageRadiusInteger = Integer.parseInt(messageRadius.getText());
			int teleportRadiusInteger = Integer.parseInt(teleportRadius.getText());
			
			TeleportsManager.setMessageRadius(prototype, messageRadiusInteger, teleportsPage, false);
			TeleportsManager.setTeleportRadius(prototype, teleportRadiusInteger, teleportsPage, false);
			
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
		
		drawCenteredString(fontRendererObj, prototype.getPrototypeName() + " Teleports", 
				(windowX + (windowWidth / 2)), (windowY + 12), 0xCCAA22);
		
		super.drawScreen(mouseX, mouseY, renderPartialTicks);
		
		if (teleportPoint == null)
			return;
		
		drawCenteredString(fontRendererObj, "(" + teleportPoint.getLabel() + ")", 
				(windowX + (windowWidth / 2)), (windowY + 25), 0xCCAA22);
		
		messageRadius.drawTextBox();
		teleportRadius.drawTextBox();

		drawString(fontRendererObj, teleportPoint.getSourcePoint().getWorldName(), 
				(windowX + 10), (windowY + 85), 0x9999AA);
		drawString(fontRendererObj, teleportPoint.getSourcePoint().getFormattedCoords(), 
				(windowX + 10), (windowY + 95), 0x9999AA);
		
		if (teleportPoint.getDestPoint() != null) {
			
			drawString(fontRendererObj, teleportPoint.getDestPoint().getWorldName(),
					(windowX + (windowWidth / 2) + 2), (windowY + 85), 0x9999AA);
			drawString(fontRendererObj, teleportPoint.getDestPoint().getFormattedCoords(), 
					(windowX + (windowWidth / 2) + 2), (windowY + 95), 0x9999AA);
		} else {
			
			drawString(fontRendererObj, "(Null)",
					(windowX + (windowWidth / 2) + 2), (windowY + 85), 0x9999AA);
		}
		
		drawString(fontRendererObj, "Message", 
				(windowX + 10), (windowY + 128), 0x9999AA);
		drawString(fontRendererObj, " Radius", 
				(windowX + 10), (windowY + 138), 0x9999AA);
		
		drawString(fontRendererObj, "Teleport", 
				(windowX + (windowWidth / 2) + 2), (windowY + 128), 0x9999AA);
		drawString(fontRendererObj, "  Radius", 
				(windowX + (windowWidth / 2) + 2), (windowY + 138), 0x9999AA);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		
		super.mouseClicked(x, y, btn);
		
		if (teleportPoint == null)
			return;
		
		if (x >= messageRadius.xPosition && x <= messageRadius.xPosition + messageRadius.width && 
				y >= messageRadius.yPosition && y <= messageRadius.yPosition + messageRadius.height) {
			
			activateMessageRadius();
			messageRadius.mouseClicked(x, y, btn);
		}
		
		if (x >= teleportRadius.xPosition && x <= teleportRadius.xPosition + teleportRadius.width && 
				y >= teleportRadius.yPosition && y <= teleportRadius.yPosition + teleportRadius.height) {
			
			activateTeleportRadius();
			teleportRadius.mouseClicked(x, y, btn);
		}
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
			
			int prototypeID = prototype.getPrototypeID();
			int teleportID = prototype.numberofTeleportPoints();
			
			TeleportsManager.addTeleport(player.getUniqueID().toString(), prototypeID, teleportID, false);
			
			Goldfish.channel.sendToServer(new PacketAddTeleport(player.getUniqueID().toString(), 
					prototype.getPrototypeID(), teleportID));
			
			initGui();
		}
		
		// Remove Teleport
		if (button.id == 2) {
			
			int teleportAmount = prototype.numberofTeleportPoints();
			
			TeleportsManager.removeTeleport(prototype, teleportsPage, teleportAmount);
			
			Goldfish.channel.sendToServer(new PacketRemoveTeleport(prototype.getPrototypeID(), 
					teleportsPage, teleportAmount));
			
			teleportsPage = 0;
			initGui();
		}
		
		// Label Teleport
		if (button.id == 3)
			player.openGui(Goldfish.instance, GUIHandler.SET_TELEPORT_LABEL_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Set Text
		if (button.id == 4)
			player.openGui(Goldfish.instance, GUIHandler.SET_TELEPORT_MESSAGE_GUI_ID, player.getEntityWorld(), 0, 0, 0);
		
		// Set Source Location
		if (button.id == 5) {
			
			WorldLocation worldLocation = new WorldLocation(player);
			WorldLocation sourceLocation = prototype.getTeleportPoint(teleportsPage).getSourcePoint();
			
			TeleportsManager.setSourcePoint(prototype, worldLocation, teleportsPage, false);
			
			if (doesTeleportConflict(prototype)) {
				
				TeleportsManager.setSourcePoint(prototype, sourceLocation, teleportsPage, false);
				ChatMessage.sendMessage(player, "Can't set location, conflicts with another location.");
				return;
			}
			
			Goldfish.channel.sendToServer(new PacketSetTeleportSource(worldLocation.getWorldName(), 
					worldLocation.getPosX(), worldLocation.getPosY(), worldLocation.getPosZ(), 
					prototype.getPrototypeID(), teleportsPage));
		}
		
		// Set Destination Location
		if (button.id == 6) {
			
			WorldLocation worldLocation = new WorldLocation(player);
			WorldLocation destLocation = prototype.getTeleportPoint(teleportsPage).getDestPoint();
			
			TeleportsManager.setDestPoint(prototype, worldLocation, teleportsPage, false);
			
			if (doesTeleportConflict(prototype)) {
				
				TeleportsManager.setDestPoint(prototype, destLocation, teleportsPage, false);
				ChatMessage.sendMessage(player, "Can't set location, conflicts with another location.");
				return;
			}
			
			Goldfish.channel.sendToServer(new PacketSetTeleportDest(worldLocation.getWorldName(), 
					worldLocation.getPosX(), worldLocation.getPosY(), worldLocation.getPosZ(), 
					prototype.getPrototypeID(), teleportsPage));
		}
				
		// Previous Page
		if (button.id == 9) {
			
			teleportsPage--;
			initGui();
		}
		
		// Next Page
		if (button.id == 10) {
			
			teleportsPage++;
			initGui();
		}
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
	
	private boolean doesTeleportConflict(Prototype prototype) {
		
		for (int i = 0; i < prototype.numberofTeleportPoints(); i++) {
			
			WorldLocation sourcePoint = prototype.getTeleportPoint(i).getSourcePoint();

			for (int j = 0; j < prototype.numberofTeleportPoints(); j++) {
				
				WorldLocation destPoint = prototype.getTeleportPoint(i).getDestPoint();
				
				if (destPoint == null)
					continue;
				
				if (sourcePoint.getWorldName().equals(destPoint.getWorldName())) {

					int teleportRadius = prototype.getTeleportPoint(i).getTeleportRadius();
					
					System.out.println(sourcePoint.getVector().distanceTo(destPoint.getVector()));
					
					if (sourcePoint.getVector().distanceTo(destPoint.getVector()) < teleportRadius)
						return true;
					
					teleportRadius = prototype.getTeleportPoint(j).getTeleportRadius();
					
					if (sourcePoint.getVector().distanceTo(destPoint.getVector()) < teleportRadius)
						return true;
				}
			}
			
		}
		
		return false;
	}
}
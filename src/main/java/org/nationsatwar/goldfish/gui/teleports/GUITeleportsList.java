package org.nationsatwar.goldfish.gui.teleports;

import java.io.IOException;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.gui.GUIMainMenu;
import org.nationsatwar.goldfish.gui.prototype.GUIPrototypesList;
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
import org.nationsatwar.palette.gui.GUIButton;
import org.nationsatwar.palette.gui.GUIHandler;
import org.nationsatwar.palette.gui.GUILabel;
import org.nationsatwar.palette.gui.GUIScreen;
import org.nationsatwar.palette.gui.GUITextField;

public class GUITeleportsList extends GUIScreen {
	
	private Prototype prototype;
	private TeleportPoint teleportPoint;
	
	private GUIButton returnButton;
	private GUIButton addTeleportButton;
	private GUIButton removeButton;
	private GUIButton labelButton;
	private GUIButton messageButton;
	private GUIButton sourceButton;
	private GUIButton destButton;
	private GUIButton previousButton;
	private GUIButton nextButton;
	
	private GUITextField messageRadius;
	private GUITextField teleportRadius;
	
	private int teleportsPage = 0;
	
	@Override
	protected void setElements() {
		
		setWindow((width - 165) / 2, 20, 165, 180);
		
		// Default Buttons
		returnButton = addButton(windowX + (windowWidth / 2) - 30, windowY + 150, 60, 20, "Return");
		addTeleportButton = addButton(windowX + 10, windowY + 40, 70, 20, "Add");
		
		prototype = PrototypeManager.getActivePrototype();
		
		if (prototype == null) {
			
			teleportsPage = 0;
			GUIHandler.openGUI(new GUIMainMenu());
			return;
		}
		
		if (prototype.numberofTeleportPoints() > teleportsPage)
			teleportPoint = prototype.getTeleportPoint(teleportsPage);
		
		if (teleportPoint == null) {
			
			teleportsPage = 0;
			return;
		}
		
		TeleportsManager.setActiveTeleportPointID(teleportsPage);
		
		// Buttons
		removeButton = addButton(windowX + 82, windowY + 40, 70, 20, "Remove");
		labelButton = addButton(windowX + 10, windowY + 62, 70, 20, "Set Label");
		messageButton = addButton(windowX + 82, windowY + 62, 70, 20, "Set Message");
		sourceButton = addButton(windowX + 10, windowY + 106, 70, 20, "Set Origin");
		destButton = addButton(windowX + (windowWidth / 2) + 2, windowY + 106, 70, 20, "Set Dest");
		
		// Text Fields
		String messageRadiusText = teleportPoint.getMessageRadius() + "";
		messageRadius = addTextField(windowX + (windowWidth / 2) - 20, windowY + 128, 20, 20, messageRadiusText);
		messageRadius.setMaxStringLength(2);
		messageRadius.setRegEx(Constants.NUMBERS_ONLY_REGEX);

		String teleportRadiusText = teleportPoint.getTeleportRadius() + "";
		teleportRadius = addTextField(windowX + windowWidth - 32, windowY + 128, 20, 20, teleportRadiusText);
		teleportRadius.setMaxStringLength(2);
		teleportRadius.setRegEx(Constants.NUMBERS_ONLY_REGEX);
		messageRadius.setFocused(false);
		
		if (teleportsPage > 0)
			previousButton = addButton(windowX + 10, windowY + 150, 20, 20, "<");
		
		if (prototype.numberofTeleportPoints() > teleportsPage + 1)
			nextButton = addButton(windowX + windowWidth - 30, windowY + 150, 20, 20, ">");
		
		// Labels
		GUILabel teleportHeader = addLabel(windowX + (windowWidth / 2), windowY + 12, 
				prototype.getPrototypeName() + " Teleports");
		teleportHeader.setCentered(true);
		
		GUILabel teleportLabel = addLabel(windowX + (windowWidth / 2), windowY + 25, 
				"(" + teleportPoint.getLabel() + ")");
		teleportLabel.setCentered(true);
		
		addLabel(windowX + 10, windowY + 85, teleportPoint.getSourcePoint().getWorldName());
		addLabel(windowX + 10, windowY + 95, teleportPoint.getSourcePoint().getFormattedCoords());
		
		if (teleportPoint.getDestPoint() != null) {
			
			addLabel(windowX + (windowWidth / 2) + 2, windowY + 85, 
					teleportPoint.getDestPoint().getWorldName());
			addLabel(windowX + (windowWidth / 2) + 2, windowY + 95, 
					teleportPoint.getDestPoint().getFormattedCoords());
		} else
			addLabel(windowX + (windowWidth / 2) + 2, windowY + 85, "(Not yet set)");
		
		addLabel(windowX + 12, windowY + 128, "Message");
		addLabel(windowX + 15, windowY + 138, "Radius");
		
		addLabel(windowX + (windowWidth / 2) + 3, windowY + 128, "Teleport");
		addLabel(windowX + (windowWidth / 2) + 5, windowY + 138, "Radius");
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		
		super.keyTyped(par1, par2);
		
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
	protected void buttonClicked(GUIButton button) {
		
		// Return
		if (button.equals(returnButton))
			GUIHandler.openGUI(new GUIPrototypesList());
		
		// Add Teleport
		if (button.equals(addTeleportButton)) {
			
			int prototypeID = prototype.getPrototypeID();
			int teleportID = prototype.numberofTeleportPoints();
			
			TeleportsManager.addTeleport(player.getUniqueID().toString(), prototypeID, teleportID, false);
			
			Goldfish.channel.sendToServer(new PacketAddTeleport(player.getUniqueID().toString(), 
					prototype.getPrototypeID(), teleportID));
			
			GUIHandler.openGUI(new GUITeleportsList());
		}
		
		// Remove Teleport
		if (button.equals(removeButton)) {
			
			int teleportAmount = prototype.numberofTeleportPoints();
			
			TeleportsManager.removeTeleport(prototype, teleportsPage, teleportAmount);
			
			Goldfish.channel.sendToServer(new PacketRemoveTeleport(prototype.getPrototypeID(), 
					teleportsPage, teleportAmount));
			
			if (prototype.numberofTeleportPoints() <= teleportsPage)
				teleportsPage--;
			
			GUIHandler.openGUI(new GUITeleportsList());
		}
		
		// Label Teleport
		if (button.equals(labelButton)) 
			GUIHandler.openGUI(new GUISetTeleportLabel());
		
		// Set Text
		if (button.equals(messageButton)) 
			GUIHandler.openGUI(new GUISetTeleportMessage());
		
		// Set Source Location
		if (button.equals(sourceButton))  {
			
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
			GUIHandler.openGUI(new GUITeleportsList());
		}
		
		// Set Destination Location
		if (button.equals(destButton))  {
			
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
			GUIHandler.openGUI(new GUITeleportsList());
		}
				
		// Previous Page
		if (button.equals(previousButton))  {
			
			teleportsPage--;
			GUIHandler.openGUI(new GUITeleportsList());
		}
		
		// Next Page
		if (button.equals(nextButton))  {
			
			teleportsPage++;
			GUIHandler.openGUI(new GUITeleportsList());
		}
	}
	
	private boolean doesTeleportConflict(Prototype prototype) {
		
		// If the destination of a portal leads to the source of another, then return true
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
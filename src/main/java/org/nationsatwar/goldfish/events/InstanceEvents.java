package org.nationsatwar.goldfish.events;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.teleportplayer.PacketTeleportPlayer;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportPoint;
import org.nationsatwar.palette.WorldLocation;

public class InstanceEvents {
	
	private static final int COOLDOWN_PERIOD = 100;
	private int teleportBuffer = 0;
	
	@SubscribeEvent
	public void serverTickEvent(ClientTickEvent event) {
		
		if (event.side == Side.SERVER)
			return;
		
		if (teleportBuffer > 0)
			teleportBuffer--;
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		
		if (player == null)
			return;
		
		Vec3 playerPos = new Vec3(player.posX, player.posY, player.posZ);
		
		for (Prototype prototype : PrototypeManager.getAllPrototypes().values()) {
			
			if (!prototype.isActivated())
				continue;
			
			for (int i = 0; i < prototype.numberofTeleportPoints(); i++) {
				
				TeleportPoint teleportPoint = prototype.getTeleportPoint(i);
				WorldLocation sourcePos = teleportPoint.getSourcePoint();
				double distance = playerPos.distanceTo(sourcePos.getVector());
				int messageRadius = teleportPoint.getMessageRadius();
				int teleportRadius = teleportPoint.getTeleportRadius();
				
				if (!sourcePos.getWorldName().equals(player.worldObj.provider.getDimensionName()))
					continue;
				
				if (distance < teleportRadius && teleportBuffer == 0) {
					
					teleportBuffer = COOLDOWN_PERIOD;
					
					Goldfish.channel.sendToServer(new PacketTeleportPlayer(player.getUniqueID().toString(), 
							prototype.getPrototypeID(), i));
					return;
				}
				
				if (distance < messageRadius) {
					
					String message = teleportPoint.getMessage();
					PrototypeManager.overlayText = message;
					return;
				} else
					PrototypeManager.overlayText = "";
			}
		}
	}
}
package org.nationsatwar.goldfish.packets.teleports.teleportradius;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerSetTeleportRadiusSend implements IMessageHandler<PacketSetTeleportRadius, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportRadius message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		
		TeleportsManager.setTeleportRadius(prototype, message.teleportRadius, message.teleportID);
		
		// Sync the teleport for all clients as well
		for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
			for (Object playerEntity : worldServer.playerEntities) {
				
				EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
				Goldfish.channel.sendTo(new PacketSetTeleportRadius(message.teleportRadius, message.prototypeID, message.teleportID), clientPlayer);
			}
		}
		
		return null;
	}
}
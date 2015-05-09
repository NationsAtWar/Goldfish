package org.nationsatwar.goldfish.packets.teleports.label;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.Goldfish;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketSetTeleportSource;
import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.palette.PlayerUtil;

public class PacketHandlerSetTeleportLabelSend implements IMessageHandler<PacketSetTeleportLabel, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportLabel message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		UUID playerUUID = UUID.fromString(message.playerUUID);
		EntityPlayer player = PlayerUtil.getPlayerByUUID(playerUUID);
		
		TeleportsManager.setSourcePoint(prototype, player, message.teleportID);
		
		// Sync the teleport for all clients as well
		for (WorldServer worldServer : MinecraftServer.getServer().worldServers) {
			for (Object playerEntity : worldServer.playerEntities) {
				
				EntityPlayerMP clientPlayer = (EntityPlayerMP) playerEntity;
				Goldfish.channel.sendTo(new PacketSetTeleportSource(message.playerUUID, message.prototypeID, message.teleportID), clientPlayer);
			}
		}
		
		return null;
	}
}
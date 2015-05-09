package org.nationsatwar.goldfish.packets.teleports.add;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerAddTeleportReceive implements IMessageHandler<PacketAddTeleport, IMessage> {

	@Override
	public IMessage onMessage(PacketAddTeleport message, MessageContext ctx) {
		
		TeleportsManager.addTeleport(message.playerUUID, message.prototypeID, message.teleportID, false);
		return null;
	}
}
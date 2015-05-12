package org.nationsatwar.goldfish.packets.teleports.teleportplayer;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.instances.InstanceManager;

public class PacketHandlerTeleportPlayer implements IMessageHandler<PacketTeleportPlayer, IMessage> {

	@Override
	public IMessage onMessage(PacketTeleportPlayer message, MessageContext ctx) {
		
		InstanceManager.enterInstance(message.playerUUID, message.prototypeID, message.teleportID);
		return null;
	}
}
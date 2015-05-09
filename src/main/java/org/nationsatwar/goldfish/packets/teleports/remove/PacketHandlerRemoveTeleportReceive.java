package org.nationsatwar.goldfish.packets.teleports.remove;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;

public class PacketHandlerRemoveTeleportReceive implements IMessageHandler<PacketRemoveTeleport, IMessage> {

	@Override
	public IMessage onMessage(PacketRemoveTeleport message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		TeleportsManager.removeTeleport(prototype, message.teleportID, message.teleportAmount);
		
		return null;
	}
}
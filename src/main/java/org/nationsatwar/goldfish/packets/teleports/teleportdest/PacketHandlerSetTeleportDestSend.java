package org.nationsatwar.goldfish.packets.teleports.teleportdest;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.palette.WorldLocation;

public class PacketHandlerSetTeleportDestSend implements IMessageHandler<PacketSetTeleportDest, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportDest message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		WorldLocation worldLocation = new WorldLocation(message.worldName, message.posX, message.posY, message.posZ);
		
		TeleportsManager.setDestPoint(prototype, worldLocation, message.teleportID, true);
		
		return null;
	}
}
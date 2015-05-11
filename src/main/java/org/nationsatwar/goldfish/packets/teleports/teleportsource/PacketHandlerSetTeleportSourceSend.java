package org.nationsatwar.goldfish.packets.teleports.teleportsource;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.palette.WorldLocation;

public class PacketHandlerSetTeleportSourceSend implements IMessageHandler<PacketSetTeleportSource, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportSource message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		WorldLocation worldLocation = new WorldLocation(message.worldName, message.posX, message.posY, message.posZ);
		
		TeleportsManager.setSourcePoint(prototype, worldLocation, message.teleportID, true);
		
		return null;
	}
}
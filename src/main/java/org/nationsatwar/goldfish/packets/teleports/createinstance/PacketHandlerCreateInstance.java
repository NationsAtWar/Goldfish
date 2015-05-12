package org.nationsatwar.goldfish.packets.teleports.createinstance;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.instances.InstanceManager;

public class PacketHandlerCreateInstance implements IMessageHandler<PacketCreateInstance, IMessage> {

	@Override
	public IMessage onMessage(PacketCreateInstance message, MessageContext ctx) {
		
		InstanceManager.createInstance(message.prototypeID, message.instanceID);
		
		return null;
	}
}
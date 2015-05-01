package org.nationsatwar.goldfish.packets;

import java.io.File;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerPrototypeSend implements IMessageHandler<PacketPrototype, IMessage> {

	@Override
	public IMessage onMessage(PacketPrototype message, MessageContext ctx) {
		
		File dataDirectory = MinecraftServer.getServer().getDataDirectory();
		
		for (File file : dataDirectory.listFiles())
			System.out.println(file.getAbsolutePath());
		
		PrototypeManager.addPrototype(message.prototypeName);
		return null;
	}
}
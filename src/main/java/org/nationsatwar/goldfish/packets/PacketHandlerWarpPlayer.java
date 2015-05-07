package org.nationsatwar.goldfish.packets;

import java.util.UUID;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.util.TeleporterFix;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerWarpPlayer implements IMessageHandler<PacketWarpPlayer, IMessage> {

	@Override
	public IMessage onMessage(PacketWarpPlayer message, MessageContext ctx) {

		Prototype prototype = PrototypeManager.getPrototype(message.prototypeName);
		int prototypeID = prototype.getPrototypeID();
		UUID playerUUID = UUID.fromString(message.playerUUID);
		
		WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(prototypeID);
		EntityPlayerMP player = (EntityPlayerMP) worldServer.getPlayerEntityByUUID(playerUUID);
		
		MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, 
				prototypeID, new TeleporterFix(worldServer));
		
		player.posY += 2;
		return null;
	}
}
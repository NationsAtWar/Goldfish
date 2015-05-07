package org.nationsatwar.goldfish.packets;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.palette.PlayerUtil;

public class PacketHandlerWarpPlayer implements IMessageHandler<PacketWarpPlayer, IMessage> {

	@Override
	public IMessage onMessage(PacketWarpPlayer message, MessageContext ctx) {

		Prototype prototype = PrototypeManager.getPrototype(message.prototypeName);
		int prototypeID = prototype.getPrototypeID();
		UUID playerUUID = UUID.fromString(message.playerUUID);
		
		WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(prototypeID);
		
		if (worldServer == null) {
			
			System.out.println("World is null");
			return null;
		}
		
		EntityPlayer player = PlayerUtil.getPlayerByUUID(playerUUID);
		
		if (player == null) {
			
			System.out.println("Player is null");
			return null;
		}
		
		PrototypeManager.prepPlayers.put(prototypeID, player);
		return null;
	}
}
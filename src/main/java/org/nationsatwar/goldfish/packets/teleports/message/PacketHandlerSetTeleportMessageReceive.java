package org.nationsatwar.goldfish.packets.teleports.message;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.nationsatwar.goldfish.prototypes.Prototype;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.teleports.TeleportsManager;
import org.nationsatwar.palette.PlayerUtil;

public class PacketHandlerSetTeleportMessageReceive implements IMessageHandler<PacketSetTeleportMessage, IMessage> {

	@Override
	public IMessage onMessage(PacketSetTeleportMessage message, MessageContext ctx) {
		
		Prototype prototype = PrototypeManager.getPrototype(message.prototypeID);
		UUID playerUUID = UUID.fromString(message.playerUUID);
		EntityPlayer player = PlayerUtil.getPlayerByUUID(playerUUID);
		
		TeleportsManager.setSourcePoint(prototype, player, message.teleportID);
		
		return null;
	}
}
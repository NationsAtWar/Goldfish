package org.nationsatwar.goldfish;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import org.nationsatwar.goldfish.events.InstanceEvents;
import org.nationsatwar.goldfish.events.ServerEvents;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketActivatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketHandlerActivatePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.activate.PacketHandlerActivatePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketCreatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketHandlerCreatePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.create.PacketHandlerCreatePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketDeletePrototype;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketHandlerDeletePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.delete.PacketHandlerDeletePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.rename.PacketHandlerRenamePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.rename.PacketHandlerRenamePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.rename.PacketRenamePrototype;
import org.nationsatwar.goldfish.packets.prototypes.warp.PacketHandlerWarpPlayer;
import org.nationsatwar.goldfish.packets.prototypes.warp.PacketWarpPlayer;
import org.nationsatwar.goldfish.packets.teleports.add.PacketAddTeleport;
import org.nationsatwar.goldfish.packets.teleports.add.PacketHandlerAddTeleportReceive;
import org.nationsatwar.goldfish.packets.teleports.add.PacketHandlerAddTeleportSend;
import org.nationsatwar.goldfish.packets.teleports.createinstance.PacketCreateInstance;
import org.nationsatwar.goldfish.packets.teleports.createinstance.PacketHandlerCreateInstance;
import org.nationsatwar.goldfish.packets.teleports.label.PacketHandlerSetTeleportLabelReceive;
import org.nationsatwar.goldfish.packets.teleports.label.PacketHandlerSetTeleportLabelSend;
import org.nationsatwar.goldfish.packets.teleports.label.PacketSetTeleportLabel;
import org.nationsatwar.goldfish.packets.teleports.message.PacketHandlerSetTeleportMessageReceive;
import org.nationsatwar.goldfish.packets.teleports.message.PacketHandlerSetTeleportMessageSend;
import org.nationsatwar.goldfish.packets.teleports.message.PacketSetTeleportMessage;
import org.nationsatwar.goldfish.packets.teleports.messageradius.PacketHandlerSetMessageRadiusReceive;
import org.nationsatwar.goldfish.packets.teleports.messageradius.PacketHandlerSetMessageRadiusSend;
import org.nationsatwar.goldfish.packets.teleports.messageradius.PacketSetMessageRadius;
import org.nationsatwar.goldfish.packets.teleports.remove.PacketHandlerRemoveTeleportReceive;
import org.nationsatwar.goldfish.packets.teleports.remove.PacketHandlerRemoveTeleportSend;
import org.nationsatwar.goldfish.packets.teleports.remove.PacketRemoveTeleport;
import org.nationsatwar.goldfish.packets.teleports.teleportdest.PacketHandlerSetTeleportDestReceive;
import org.nationsatwar.goldfish.packets.teleports.teleportdest.PacketHandlerSetTeleportDestSend;
import org.nationsatwar.goldfish.packets.teleports.teleportdest.PacketSetTeleportDest;
import org.nationsatwar.goldfish.packets.teleports.teleportplayer.PacketHandlerTeleportPlayer;
import org.nationsatwar.goldfish.packets.teleports.teleportplayer.PacketTeleportPlayer;
import org.nationsatwar.goldfish.packets.teleports.teleportradius.PacketHandlerSetTeleportRadiusReceive;
import org.nationsatwar.goldfish.packets.teleports.teleportradius.PacketHandlerSetTeleportRadiusSend;
import org.nationsatwar.goldfish.packets.teleports.teleportradius.PacketSetTeleportRadius;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketHandlerSetTeleportSourceReceive;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketHandlerSetTeleportSourceSend;
import org.nationsatwar.goldfish.packets.teleports.teleportsource.PacketSetTeleportSource;
import org.nationsatwar.goldfish.proxy.CommonProxy;
 
@Mod(modid = Goldfish.MODID, 
	name = Goldfish.MODNAME, 
	version = Goldfish.MODVER)
public class Goldfish {

    @Instance(Goldfish.MODID)
    public static Goldfish instance;

	@SidedProxy(clientSide = Goldfish.CLIENT_PROXY_CLASS, serverSide = Goldfish.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	public static final String MODID = "goldfish";
	public static final String MODNAME = "Goldfish";
	public static final String MODVER = "0.0.2";

	public static final String GUI_FACTORY_CLASS = "org.nationsatwar.goldfish.gui.GUIFactory";
	public static final String CLIENT_PROXY_CLASS = "org.nationsatwar.goldfish.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "org.nationsatwar.goldfish.proxy.CommonProxy";
	
	public static final String goldfishPath = "Goldfish/";
	public static final String prototypePath = "Goldfish/prototypes/";
	public static final String instancePath = "Goldfish/instances/";
	
	public static SimpleNetworkWrapper channel;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// Packet Registration
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(Goldfish.MODID);
		
		// Prototype Packets
		channel.registerMessage(PacketHandlerCreatePrototypeSend.class, PacketCreatePrototype.class, 0, Side.SERVER);
		channel.registerMessage(PacketHandlerCreatePrototypeReceive.class, PacketCreatePrototype.class, 0, Side.CLIENT);
		channel.registerMessage(PacketHandlerRenamePrototypeSend.class, PacketRenamePrototype.class, 1, Side.SERVER);
		channel.registerMessage(PacketHandlerRenamePrototypeReceive.class, PacketRenamePrototype.class, 1, Side.CLIENT);
		channel.registerMessage(PacketHandlerDeletePrototypeSend.class, PacketDeletePrototype.class, 2, Side.SERVER);
		channel.registerMessage(PacketHandlerDeletePrototypeReceive.class, PacketDeletePrototype.class, 2, Side.CLIENT);
		channel.registerMessage(PacketHandlerActivatePrototypeSend.class, PacketActivatePrototype.class, 3, Side.SERVER);
		channel.registerMessage(PacketHandlerActivatePrototypeReceive.class, PacketActivatePrototype.class, 3, Side.CLIENT);
		channel.registerMessage(PacketHandlerWarpPlayer.class, PacketWarpPlayer.class, 4, Side.SERVER);
		
		// Teleports Packets - TODO: Perhaps consolidate these into one general TeleportSync packet
		channel.registerMessage(PacketHandlerAddTeleportSend.class, PacketAddTeleport.class, 5, Side.SERVER);
		channel.registerMessage(PacketHandlerAddTeleportReceive.class, PacketAddTeleport.class, 5, Side.CLIENT);
		channel.registerMessage(PacketHandlerRemoveTeleportSend.class, PacketRemoveTeleport.class, 6, Side.SERVER);
		channel.registerMessage(PacketHandlerRemoveTeleportReceive.class, PacketRemoveTeleport.class, 6, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportSourceSend.class, PacketSetTeleportSource.class, 7, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportSourceReceive.class, PacketSetTeleportSource.class, 7, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportDestSend.class, PacketSetTeleportDest.class, 8, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportDestReceive.class, PacketSetTeleportDest.class, 8, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetMessageRadiusSend.class, PacketSetMessageRadius.class, 9, Side.SERVER);
		channel.registerMessage(PacketHandlerSetMessageRadiusReceive.class, PacketSetMessageRadius.class, 9, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportRadiusSend.class, PacketSetTeleportRadius.class, 10, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportRadiusReceive.class, PacketSetTeleportRadius.class, 10, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportMessageSend.class, PacketSetTeleportMessage.class, 11, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportMessageReceive.class, PacketSetTeleportMessage.class, 11, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportLabelSend.class, PacketSetTeleportLabel.class, 12, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportLabelReceive.class, PacketSetTeleportLabel.class, 12, Side.CLIENT);
		
		channel.registerMessage(PacketHandlerTeleportPlayer.class, PacketTeleportPlayer.class, 13, Side.SERVER);
		channel.registerMessage(PacketHandlerCreateInstance.class, PacketCreateInstance.class, 14, Side.CLIENT);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		// Register Client Classes
		proxy.registerKeybindings();
		proxy.registerClientEvents();
		
		MinecraftForge.EVENT_BUS.register(new ServerEvents());
		FMLCommonHandler.instance().bus().register(new ServerEvents());
		FMLCommonHandler.instance().bus().register(new InstanceEvents());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		proxy.registerGUI();
	}
}
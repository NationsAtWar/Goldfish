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

import org.nationsatwar.goldfish.events.DebugEvents;
import org.nationsatwar.goldfish.gui.GUIHandler;
import org.nationsatwar.goldfish.packets.prototypes.PacketCreatePrototype;
import org.nationsatwar.goldfish.packets.prototypes.PacketDeletePrototype;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerCreatePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerCreatePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerDeletePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerDeletePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerRenamePrototypeReceive;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerRenamePrototypeSend;
import org.nationsatwar.goldfish.packets.prototypes.PacketHandlerWarpPlayer;
import org.nationsatwar.goldfish.packets.prototypes.PacketRenamePrototype;
import org.nationsatwar.goldfish.packets.prototypes.PacketWarpPlayer;
import org.nationsatwar.goldfish.packets.teleports.PacketAddTeleport;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerAddTeleportReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerAddTeleportSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetMessageRadiusReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetMessageRadiusSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportDestReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportDestSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportLabelReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportLabelSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportMessageReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportMessageSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportRadiusReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportRadiusSend;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportSourceReceive;
import org.nationsatwar.goldfish.packets.teleports.PacketHandlerSetTeleportSourceSend;
import org.nationsatwar.goldfish.packets.teleports.PacketSetMessageRadius;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportDest;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportLabel;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportMessage;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportRadius;
import org.nationsatwar.goldfish.packets.teleports.PacketSetTeleportSource;
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
		channel.registerMessage(PacketHandlerCreatePrototypeSend.class, PacketCreatePrototype.class, 1, Side.SERVER);
		channel.registerMessage(PacketHandlerCreatePrototypeReceive.class, PacketCreatePrototype.class, 1, Side.CLIENT);
		channel.registerMessage(PacketHandlerRenamePrototypeSend.class, PacketRenamePrototype.class, 2, Side.SERVER);
		channel.registerMessage(PacketHandlerRenamePrototypeReceive.class, PacketRenamePrototype.class, 2, Side.CLIENT);
		channel.registerMessage(PacketHandlerDeletePrototypeSend.class, PacketDeletePrototype.class, 3, Side.SERVER);
		channel.registerMessage(PacketHandlerDeletePrototypeReceive.class, PacketDeletePrototype.class, 3, Side.CLIENT);
		channel.registerMessage(PacketHandlerWarpPlayer.class, PacketWarpPlayer.class, 4, Side.SERVER);
		
		// Teleports Packets - TODO: Perhaps consolidate these into one general TeleportSync packet
		channel.registerMessage(PacketHandlerAddTeleportSend.class, PacketAddTeleport.class, 5, Side.SERVER);
		channel.registerMessage(PacketHandlerAddTeleportReceive.class, PacketAddTeleport.class, 5, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportSourceSend.class, PacketSetTeleportSource.class, 6, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportSourceReceive.class, PacketSetTeleportSource.class, 6, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportDestSend.class, PacketSetTeleportDest.class, 7, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportDestReceive.class, PacketSetTeleportDest.class, 7, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetMessageRadiusSend.class, PacketSetMessageRadius.class, 8, Side.SERVER);
		channel.registerMessage(PacketHandlerSetMessageRadiusReceive.class, PacketSetMessageRadius.class, 8, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportRadiusSend.class, PacketSetTeleportRadius.class, 9, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportRadiusReceive.class, PacketSetTeleportRadius.class, 9, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportMessageSend.class, PacketSetTeleportMessage.class, 10, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportMessageReceive.class, PacketSetTeleportMessage.class, 10, Side.CLIENT);
		channel.registerMessage(PacketHandlerSetTeleportLabelSend.class, PacketSetTeleportLabel.class, 11, Side.SERVER);
		channel.registerMessage(PacketHandlerSetTeleportLabelReceive.class, PacketSetTeleportLabel.class, 11, Side.CLIENT);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		// Register GUI
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
		
		// Register Client Classes
		proxy.registerKeybindings();
		proxy.registerClientEvents();
		
		MinecraftForge.EVENT_BUS.register(new DebugEvents());
		FMLCommonHandler.instance().bus().register(new DebugEvents());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		
	}
}
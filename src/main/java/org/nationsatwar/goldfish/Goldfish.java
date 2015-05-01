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
import org.nationsatwar.goldfish.packets.PacketHandlerPrototypeReceive;
import org.nationsatwar.goldfish.packets.PacketHandlerPrototypeSend;
import org.nationsatwar.goldfish.packets.PacketPrototype;
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
	public static final String MODVER = "0.0.1";

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
		channel.registerMessage(PacketHandlerPrototypeSend.class, PacketPrototype.class, 1, Side.SERVER);
		channel.registerMessage(PacketHandlerPrototypeReceive.class, PacketPrototype.class, 1, Side.CLIENT);
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
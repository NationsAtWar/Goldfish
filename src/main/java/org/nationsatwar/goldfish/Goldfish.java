package org.nationsatwar.goldfish;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.nationsatwar.goldfish.events.DebugEvents;
import org.nationsatwar.goldfish.prototypes.PrototypeManager;
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

	public static final String goldfishPath = "mods/Goldfish/";
	public static final String prototypePath = "mods/Goldfish/prototypes/";
	public static final String instancePath = "mods/Goldfish/instances/";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		// Register Client Classes
		proxy.registerKeybindings();
		proxy.registerClientEvents();
		
		MinecraftForge.EVENT_BUS.register(new DebugEvents());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		// Create necessary folders
		File goldfishDirectory = new File(goldfishPath);
		File prototypeDirectory = new File(prototypePath);
		File instanceDirectory = new File(instancePath);
		
		if (!goldfishDirectory.exists())
			goldfishDirectory.mkdir();
		if (!prototypeDirectory.exists())
			prototypeDirectory.mkdir();
		if (!instanceDirectory.exists())
			instanceDirectory.mkdir();
	}
}
package org.nationsatwar.goldfish;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

	public static final String GUI_FACTORY_CLASS = "org.nationsatwar.babble.gui.GUIFactory";
	public static final String CLIENT_PROXY_CLASS = "org.nationsatwar.babble.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "org.nationsatwar.babble.proxy.CommonProxy";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		
	}
}
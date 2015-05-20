package org.nationsatwar.goldfish.events;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.nationsatwar.goldfish.gui.GUIMainMenu;
import org.nationsatwar.goldfish.proxy.ClientProxy;
import org.nationsatwar.palette.gui.GUIHandler;

public class KeyEvents {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		
		if (ClientProxy.goldfishKey.isPressed())
			GUIHandler.openGUI(new GUIMainMenu());
	}
}
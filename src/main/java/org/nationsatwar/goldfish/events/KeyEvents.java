package org.nationsatwar.goldfish.events;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;
import org.nationsatwar.goldfish.proxy.ClientProxy;

public class KeyEvents {
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		
		if (ClientProxy.debugKey.isPressed()) {
			
			PrototypeManager.addPrototype("Test6");
			
			for (int dimensionID : DimensionManager.getIDs())
				System.out.println(DimensionManager.getProvider(dimensionID).getDimensionName());
		}
	}
}
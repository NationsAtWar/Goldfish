package org.nationsatwar.goldfish.gui;

import org.nationsatwar.goldfish.prototypes.PrototypeManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GUIOverlay extends Gui {
	
	private Minecraft mc;
	
	public GUIOverlay(Minecraft mc) {
		
		super();
		
		// We need this to invoke the render engine.
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent.Text event) {
		
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int width = res.getScaledWidth();
		int height = res.getScaledHeight();

		int x = width / 2;
		int y = height - 60;
		int color = 0xffffff;
		
		drawCenteredString(mc.fontRendererObj, PrototypeManager.overlayText, x, y, color);
		
		// Resets the renderer to its normal overlay texture (Drawing strings changes it)
		mc.getTextureManager().bindTexture(icons);
	}
}
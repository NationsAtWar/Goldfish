package org.nationsatwar.goldfish.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {
	
	public static final int MAIN_GUI_ID = 20;
	public static final int CREATE_CONFIRM_GUI_ID = 21;
	public static final int LIST_GUI_ID = 22;
	public static final int RENAME_PROTOTYPE_GUI_ID = 23;
	public static final int DELETE_CONFIRM_GUI_ID = 24;
	public static final int TELEPORTS_GUI_ID = 25;
	public static final int SET_TELEPORT_LABEL_GUI_ID = 26;
	public static final int SET_TELEPORT_MESSAGE_GUI_ID = 27;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		
		if (ID == MAIN_GUI_ID)
			return new GUIMainMenu(player, world, x, y, z);
		if (ID == CREATE_CONFIRM_GUI_ID)
			return new GUICreateConfirm(player, world, x, y, z);
		if (ID == LIST_GUI_ID)
			return new GUIPrototypesList(player, world, x, y, z);
		if (ID == RENAME_PROTOTYPE_GUI_ID)
			return new GUIRenamePrototype(player, world, x, y, z);
		if (ID == DELETE_CONFIRM_GUI_ID)
			return new GUIDeleteConfirm(player, world, x, y, z);
		if (ID == TELEPORTS_GUI_ID)
			return new GUITeleportsList(player, world, x, y, z);
		if (ID == SET_TELEPORT_LABEL_GUI_ID)
			return new GUISetTeleportLabel(player, world, x, y, z);
		if (ID == SET_TELEPORT_MESSAGE_GUI_ID)
			return new GUISetTeleportMessage(player, world, x, y, z);
		
		return null;
	}
}
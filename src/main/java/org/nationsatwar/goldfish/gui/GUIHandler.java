package org.nationsatwar.goldfish.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import org.nationsatwar.goldfish.gui.config.GUIConditions;
import org.nationsatwar.goldfish.gui.config.GUIConfiguration;
import org.nationsatwar.goldfish.gui.config.GUIEquipment;
import org.nationsatwar.goldfish.gui.config.GUIMapLimit;
import org.nationsatwar.goldfish.gui.config.GUIRespawnOptions;
import org.nationsatwar.goldfish.gui.config.GUITimers;
import org.nationsatwar.goldfish.gui.prototype.GUICreateConfirm;
import org.nationsatwar.goldfish.gui.prototype.GUIDeleteConfirm;
import org.nationsatwar.goldfish.gui.prototype.GUIPrototypesList;
import org.nationsatwar.goldfish.gui.prototype.GUIRenamePrototype;
import org.nationsatwar.goldfish.gui.teleports.GUISetTeleportLabel;
import org.nationsatwar.goldfish.gui.teleports.GUISetTeleportMessage;
import org.nationsatwar.goldfish.gui.teleports.GUITeleportsList;

public class GUIHandler implements IGuiHandler {
	
	public static final int MAIN_GUI_ID = 20;
	public static final int CREATE_CONFIRM_GUI_ID = 21;
	public static final int LIST_GUI_ID = 22;
	public static final int RENAME_PROTOTYPE_GUI_ID = 23;
	public static final int DELETE_CONFIRM_GUI_ID = 24;
	public static final int TELEPORTS_GUI_ID = 25;
	public static final int SET_TELEPORT_LABEL_GUI_ID = 26;
	public static final int SET_TELEPORT_MESSAGE_GUI_ID = 27;
	public static final int CONFIG_GUI_ID = 28;
	public static final int CONFIG_LIMIT_GUI_ID = 29;
	public static final int CONFIG_RESPAWN_OPTIONS_GUI_ID = 30;
	public static final int CONFIG_TIMERS_GUI_ID = 31;
	public static final int CONFIG_EQUIPMENT_GUI_ID = 32;
	public static final int CONFIG_CONDITIONS_GUI_ID = 33;

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
		if (ID == CONFIG_GUI_ID)
			return new GUIConfiguration(player, world, x, y, z);
		if (ID == CONFIG_LIMIT_GUI_ID)
			return new GUIMapLimit(player, world, x, y, z);
		if (ID == CONFIG_RESPAWN_OPTIONS_GUI_ID)
			return new GUIRespawnOptions(player, world, x, y, z);
		if (ID == CONFIG_TIMERS_GUI_ID)
			return new GUITimers(player, world, x, y, z);
		if (ID == CONFIG_EQUIPMENT_GUI_ID)
			return new GUIEquipment(player, world, x, y, z);
		if (ID == CONFIG_CONDITIONS_GUI_ID)
			return new GUIConditions(player, world, x, y, z);
		
		return null;
	}
}
package org.nationsatwar.goldfish;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GoldfishTimers extends BukkitRunnable {

	private Goldfish plugin;

	private String instanceName;
	private int timer;
	private boolean active;

	public GoldfishTimers(Goldfish plugin, String instanceName, int timer, boolean active) {

		this.plugin = plugin;

		this.instanceName = instanceName;
		this.timer = timer;
		this.active = active;
	}

	public void run() {

		if (!active)
			return;

		timer--;

		World instanceWorld = plugin.getServer().getWorld(instanceName);

		if (instanceWorld != null) {

			if (timer % 10 == 0 && timer > 0) {

				for (Player player : instanceWorld.getPlayers())
					player.sendMessage("You have " + timer + " seconds to complete this instance.");
			}

			if (timer < 10 && timer > 0) {

				for (Player player : instanceWorld.getPlayers())
					player.sendMessage(String.valueOf(timer));
			}
		}

		if (timer <= 0) {

			plugin.goldfishManager.destroyInstance(instanceName);
			plugin.getServer().getScheduler().cancelTask(getTaskId());
		}
	}

	public void activate() {

		active = true;
	}

	public void deactivate() {

		active = false;
	}

	public int getTimerAmount() {

		return timer;
	}

	public void setTimerAmount(int timerAmount) {

		timer = timerAmount;
	}
}
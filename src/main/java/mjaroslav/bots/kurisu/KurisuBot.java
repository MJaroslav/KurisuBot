package mjaroslav.bots.kurisu;

import java.util.ArrayList;

import mjaroslav.bots.kurisu.config.Config;
import mjaroslav.bots.kurisu.managers.DCManager;
import mjaroslav.bots.kurisu.managers.StatManager;
import mjaroslav.bots.kurisu.managers.VKManager;
import mjaroslav.bots.kurisu.objects.IManager;

public class KurisuBot {
	private static ArrayList<IManager> managers = new ArrayList<IManager>();

	public static void main(String... args) {
		registerManagers();
		Info.LOG.info(String.format("Found %s manager(s)", managers.size()));
		loadManagers();
		Info.LOG.info(String.format("Loading done! Start count: %s, last start: %s", Config.getStat().startCount,
				StatManager.lastStart));
	}

	public static void registerManagers() {
		managers.add(new Config());
		managers.add(new StatManager());
		managers.add(new VKManager());
		managers.add(new DCManager());
	}

	public static void loadManagers() {
		for (IManager manager : managers) {
			if (manager.canLoad())
				manager.load();
		}
	}
}

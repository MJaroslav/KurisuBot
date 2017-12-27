package mjaroslav.bots.kurisu.managers;

import java.util.Date;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.config.Config;
import mjaroslav.bots.kurisu.objects.IManager;

public class StatManager implements IManager {
	public static String lastStart = "";

	@Override
	public boolean canLoad() {
		return true;
	}

	@Override
	public void load() {
		Date date = new Date();
		lastStart = Config.getStat().lastStart;
		Config.getStat().lastStart = Info.DATE_FORMAT_FULL.format(date);
		Config.getStat().startCount++;
		Config.statReader.write();
	}

	@Override
	public boolean isLoaded() {
		return true;
	}
}

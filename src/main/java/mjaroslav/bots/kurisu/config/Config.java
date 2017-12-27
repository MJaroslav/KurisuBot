package mjaroslav.bots.kurisu.config;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.objects.IManager;
import mjaroslav.bots.kurisu.objects.JSONReader;

public class Config implements IManager {
	public static JSONReader<StatJSON> statReader = new JSONReader<StatJSON>(new StatJSON(), Info.CONFIG_STAT, true);
	public static JSONReader<AuthJSON> authReader = new JSONReader<AuthJSON>(new AuthJSON(), Info.CONFIG_AUTH, true);
	public static JSONReader<MainJSON> mainReader = new JSONReader<MainJSON>(new MainJSON(), Info.CONFIG_MAIN, true);

	public static MainJSON getMain() {
		return mainReader.json;
	}

	public static AuthJSON getAuth() {
		return authReader.json;
	}

	public static StatJSON getStat() {
		return statReader.json;
	}

	@Override
	public boolean canLoad() {
		return true;
	}

	@Override
	public void load() {
		statReader.init();
		authReader.init();
		mainReader.init();
	}

	@Override
	public boolean isLoaded() {
		return true;
	}
}

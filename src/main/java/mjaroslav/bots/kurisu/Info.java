package mjaroslav.bots.kurisu;

import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Info {
	public static final String NAME = "Kurisu Bot";

	public static final String FOLDER = "kurisu/";

	public static final String FOLDER_CONFIG = FOLDER + "config/";
	public static final String CONFIG_MAIN = FOLDER_CONFIG + "main.json";
	public static final String CONFIG_AUTH = FOLDER_CONFIG + "auth.json";
	public static final String CONFIG_STAT = FOLDER_CONFIG + "stat.json";

	public static final Logger LOG = LogManager.getLogger(NAME);

	public static final Gson gson = new GsonBuilder().create();

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd");
	public static final SimpleDateFormat DATE_FORMAT_FULL = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

	public static final String VK_ID_TYPE_CHAT = "c";

	public static final Random RAND = new Random();
}

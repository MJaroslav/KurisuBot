package mjaroslav.bots.kurisu.utils;

import com.vk.api.sdk.exceptions.ApiCaptchaException;

import mjaroslav.bots.kurisu.config.Config;

public class Utils {
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void setCaptchaInfo(ApiCaptchaException captcha) {
		Config.getStat().vkCaptchaNeeded = true;
		Config.getStat().vkLastCaptchaSid = captcha.getSid();
		Config.getStat().vkLastCaptchaImage = captcha.getImage();
		Config.getStat().vkLastCaptchaKey = "null";
		Config.statReader.write();
	}
}

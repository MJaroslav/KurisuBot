package mjaroslav.bots.kurisu.config;

import com.google.gson.annotations.SerializedName;

public class StatJSON {
	@SerializedName("start_count")
	public int startCount = 0;
	@SerializedName("last_start")
	public String lastStart = "null";
	@SerializedName("vk_last_captcha_key")
	public String vkLastCaptchaKey = "null";
	@SerializedName("vk_last_captcha_sid")
	public String vkLastCaptchaSid = "null";
	@SerializedName("vk_captcha_needed")
	public boolean vkCaptchaNeeded = false;
	@SerializedName("vk_last_captcha_image")
	public String vkLastCaptchaImage = "null";
}

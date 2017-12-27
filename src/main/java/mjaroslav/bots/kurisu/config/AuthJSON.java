package mjaroslav.bots.kurisu.config;

import com.google.gson.annotations.SerializedName;

public class AuthJSON {
	@SerializedName("vk_token")
	public String vkToken = "";
	@SerializedName("vk_id")
	public int vkId = 0;
	@SerializedName("discord_token")
	public String dcToken = "";
}

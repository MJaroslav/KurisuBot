package mjaroslav.bots.kurisu.config;

import com.google.gson.annotations.SerializedName;

public class MainJSON {
	@SerializedName("discord_module")
	public DCModule dc = new DCModule();
	@SerializedName("vk_module")
	public VKModule vk = new VKModule();

	public static class DCModule {
		@SerializedName("enabled")
		public boolean enabled = true;
		@SerializedName("chat_log_format")
		public String chatLogFormat = "[DC|{server}|[{channel}|{user} ({userId})] {text}";
	}

	public static class VKModule {
		@SerializedName("enabled")
		public boolean enabled = true;
		@SerializedName("chat_log_format")
		public String chatLogFormat = "[VK|{chat}|{user} ({userId})] {text}";
		@SerializedName("check_token_on_starting")
		public boolean checkToken = true;
		@SerializedName("check_message_rate")
		public int checkMessageRate = 1500;
		@SerializedName("answer_message_rate")
		public int answerMessageRate = 1500;
		@SerializedName("cooldown_rate")
		public int cooldownRate = 6000;
	}
}

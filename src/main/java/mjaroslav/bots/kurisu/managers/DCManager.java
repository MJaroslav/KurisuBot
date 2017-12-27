package mjaroslav.bots.kurisu.managers;

import mjaroslav.bots.kurisu.config.Config;
import mjaroslav.bots.kurisu.events.DCEvents;
import mjaroslav.bots.kurisu.objects.IManager;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IMessage;

public class DCManager implements IManager {
	public static IDiscordClient client;

	public static boolean ready = false;

	@Override
	public boolean canLoad() {
		return Config.getMain().dc.enabled;
	}

	@Override
	public void load() {
		client = login(Config.getAuth().dcToken);
		client.getDispatcher().registerListener(new DCEvents());
	}

	@Override
	public boolean isLoaded() {
		return ready;
	}

	public static IDiscordClient login(String token) {
		return new ClientBuilder().withToken(token).login();
	}

	public static String formatMessage(IMessage message) {
		String server = " ... ";
		long serverId = 0;
		String channel = " ... ";
		long channelId = 0;
		String user = " ... ";
		long userId = 0;
		String text = "null";
		if (message != null) {
			if (message.getGuild() != null) {
				server = message.getGuild().getName();
				serverId = message.getGuild().getLongID();
			}
			if (message.getChannel() != null) {
				channel = message.getChannel().getName();
				channelId = message.getChannel().getLongID();
			}
			if (message.getAuthor() != null) {
				user = message.getAuthor().getName();
				userId = message.getAuthor().getLongID();
			}
			if (message.getContent() != null)
				text = message.getContent().replace("\n", "\\n");
		}
		return Config.getMain().dc.chatLogFormat.replace("{server}", server).replace("{channel}", channel)
				.replace("{user}", user).replace("{serverId}", String.valueOf(serverId))
				.replace("{channelId}", String.valueOf(channelId)).replace("{userId}", String.valueOf(userId))
				.replace("{text}", text);
	}
}

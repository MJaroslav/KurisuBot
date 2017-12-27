package mjaroslav.bots.kurisu.events;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.managers.DCManager;
import mjaroslav.bots.kurisu.managers.VKManager;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public class DCEvents {
	@EventSubscriber
	public void onReady(ReadyEvent event) {
		DCManager.ready = true;
		Info.LOG.info("Discord module is ready!");
	}

	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) {
		if (event.getMessage() != null) {
			Info.LOG.info(DCManager.formatMessage(event.getMessage()));
			if (event.getMessage().getContent().equals("/stop")) {
				DCManager.client.logout();
				VKManager.messageChecker.breakThread();
				VKManager.commandHandler.breakThread();
			}
		}
	}
}

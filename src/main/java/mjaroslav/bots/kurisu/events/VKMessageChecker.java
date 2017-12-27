package mjaroslav.bots.kurisu.events;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiTooManyException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.responses.GetResponse;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.config.Config;
import mjaroslav.bots.kurisu.managers.VKManager;
import mjaroslav.bots.kurisu.objects.BreakableThread;
import mjaroslav.bots.kurisu.utils.Utils;

public class VKMessageChecker extends BreakableThread {
	boolean status = false;

	private GetResponse oldMessage;
	private GetResponse newMessage;

	@Override
	public void work() {
		Info.LOG.info("Tick");
		oldMessage = null;
		try {
			Thread.sleep(3000);
			oldMessage = VKManager.client.messages().get(VKManager.actor).count(1).execute();
			Info.LOG.info("Tick");
		} catch (ApiException | ClientException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		newMessage = null;
	}

	public static int i = 0;

	@Override
	public void whileWork() {
		Utils.sleep(Config.getMain().vk.checkMessageRate);
		Info.LOG.info("Tick");
		boolean whileFlag = true;
		while (whileFlag) {
			whileFlag = false;
			try {
				newMessage = VKManager.client.messages().get(VKManager.actor).count(1).execute();
			} catch (ApiException | ClientException e) {
				if (e instanceof ApiTooManyException) {
					whileFlag = true;
					Utils.sleep(Config.getMain().vk.cooldownRate);
				} else
					e.printStackTrace();
			}
		}
		if (i % 100 == 0 || i == 0)
			try {
				VKManager.client.account().setOnline(VKManager.actor).voip(false).execute();
			} catch (Exception e1) {

			}
		if (i > 1000)
			i = 0;
		if (newMessage.getItems().size() > 0 && oldMessage.getItems().size() > 0)
			if (newMessage.getItems().get(0).getId() > oldMessage.getItems().get(0).getId()) {
				oldMessage = newMessage;
				VKCommandHandler.messages.add(newMessage);
				// TODO: add bridge
			}
		i++;
	}
}

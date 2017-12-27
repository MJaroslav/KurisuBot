package mjaroslav.bots.kurisu.events;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.responses.GetResponse;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.managers.DCManager;
import mjaroslav.bots.kurisu.managers.VKManager;
import mjaroslav.bots.kurisu.objects.BreakableThread;
import mjaroslav.bots.kurisu.utils.Utils;

public class VKCommandHandler extends BreakableThread {
	public static ArrayList<GetResponse> messages = new ArrayList<GetResponse>();

	public static int id = 0;

	@Override
	public void whileWork() {
		if (messages.size() > 0 && id < messages.size()) {
			GetResponse res = messages.get(id);
			Message message = res.getItems().get(0);
			if (message != null) {
				Info.LOG.info(VKManager.formatMessage(message));
				if (!StringUtils.isEmpty(message.getBody()) && !VKManager.getChatId(message).equals("c1"))
					if (message.getBody().equals("/stop")) {
						DCManager.client.logout();
						VKManager.messageChecker.breakThread();
						VKManager.commandHandler.breakThread();
					}
			}
			id++;
		}
		Utils.sleep(100);
	}
}

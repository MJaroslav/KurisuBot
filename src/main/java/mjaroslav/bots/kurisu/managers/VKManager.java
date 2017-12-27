package mjaroslav.bots.kurisu.managers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiCaptchaException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiTooManyException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.messages.Chat;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.users.UserField;

import mjaroslav.bots.kurisu.Info;
import mjaroslav.bots.kurisu.config.Config;
import mjaroslav.bots.kurisu.events.VKCommandHandler;
import mjaroslav.bots.kurisu.events.VKMessageChecker;
import mjaroslav.bots.kurisu.objects.IManager;
import mjaroslav.bots.kurisu.utils.Utils;

public class VKManager implements IManager {
	public static final String VERSION = "5.69";

	public static VKMessageChecker messageChecker = new VKMessageChecker();
	public static VKCommandHandler commandHandler = new VKCommandHandler();

	public static final int TRUE = 1;
	public static final int FALSE = 0;

	public static TransportClient transportClient = HttpTransportClient.getInstance();
	public static VkApiClient client = new VkApiClient(transportClient);
	public static UserActor actor;

	private static boolean ready = false;

	@Override
	public boolean canLoad() {
		return Config.getMain().vk.enabled;
	}

	@Override
	public void load() {
		actor = login(Config.getAuth().vkId, Config.getAuth().vkToken);
		if (actor != null && (!Config.getMain().vk.checkToken || validVKToken(Config.getAuth().vkToken))) {
			ready = true;
			Info.LOG.info("VK module is ready!");
			messageChecker.playThread();
			messageChecker.start();
			commandHandler.playThread();
			commandHandler.start();
		}
	}

	@Override
	public boolean isLoaded() {
		return ready;
	}

	public static UserActor login(int id, String token) {
		return new UserActor(id, token);
	}

	public static boolean validVKToken(String token) {
		try {
			List<UserXtrCounters> res = client.users().get(actor).execute();
			if (res != null && !res.isEmpty())
				return true;
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void sendMessage(String chatId, String text, String... attachments) {
		if (Config.getStat().vkCaptchaNeeded) {
			Info.LOG.info("Needed captcha!");
			return;
		}
		boolean isChat = isChat(chatId);
		String id = getChatId(chatId);

		int iid = -1;

		try {
			iid = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			iid = -1;
		}

		if (iid == -1)
			return;

		String endtext = text + "\n";// + getAntiFlood();

		if (isChat) {
			boolean whileFlag = true;
			while (whileFlag) {
				whileFlag = false;
				try {
					if (attachments.length > 0)
						client.messages().send(actor).chatId(iid).message(endtext).attachment(attachments)
								.captchaKey((Config.getStat().vkLastCaptchaKey.equals("") ? "0"
										: Config.getStat().vkLastCaptchaKey))
								.captchaSid((Config.getStat().vkLastCaptchaSid.equals("") ? "0"
										: Config.getStat().vkLastCaptchaSid))
								.execute();
					else
						client.messages().send(actor).chatId(iid).message(endtext)
								.captchaKey((Config.getStat().vkLastCaptchaKey.equals("") ? "0"
										: Config.getStat().vkLastCaptchaKey))
								.captchaSid((Config.getStat().vkLastCaptchaSid.equals("") ? "0"
										: Config.getStat().vkLastCaptchaSid))
								.execute();
				} catch (ApiException | ClientException e) {
					if (e instanceof ApiCaptchaException) {
						ApiCaptchaException ae = (ApiCaptchaException) e;
						Utils.setCaptchaInfo(ae);
						// TODO: Answer capthca system
					}
					if (e instanceof ApiTooManyException) {
						whileFlag = true;
						Utils.sleep(Config.getMain().vk.answerMessageRate);
					} else
						e.printStackTrace();
				}
			}
		} else {
			boolean whileFlag = true;
			while (whileFlag) {
				whileFlag = false;
				try {
					if (attachments.length > 0)
						client.messages().send(actor).userId(iid).message(endtext).attachment(attachments)
								.captchaKey((Config.getStat().vkLastCaptchaKey.equals("") ? "0"
										: Config.getStat().vkLastCaptchaKey))
								.captchaSid((Config.getStat().vkLastCaptchaSid.equals("") ? "0"
										: Config.getStat().vkLastCaptchaSid))
								.execute();
					else
						client.messages().send(actor).userId(iid).message(endtext)
								.captchaKey((Config.getStat().vkLastCaptchaKey.equals("") ? "0"
										: Config.getStat().vkLastCaptchaKey))
								.captchaSid((Config.getStat().vkLastCaptchaSid.equals("") ? "0"
										: Config.getStat().vkLastCaptchaSid))
								.execute();
				} catch (ApiException | ClientException e) {
					if (e instanceof ApiCaptchaException) {
						ApiCaptchaException ae = (ApiCaptchaException) e;
						Utils.setCaptchaInfo(ae);
						// TODO: Answer captcha system
					}
					if (e instanceof ApiTooManyException) {
						whileFlag = true;
						Utils.sleep(Config.getMain().vk.answerMessageRate);
					} else
						e.printStackTrace();
				}
			}
		}
	}

	public static boolean isChat(String id) {
		return id.startsWith(Info.VK_ID_TYPE_CHAT) && id.length() > Info.VK_ID_TYPE_CHAT.length();
	}

	public static String getChatId(Message message) {
		String id = String.valueOf(message.getUserId());
		if (message.getChatId() != null && message.getChatId() > 0)
			id = Info.VK_ID_TYPE_CHAT + String.valueOf(message.getChatId());
		return id;
	}

	public static String getChatId(String id) {
		return id.replace(Info.VK_ID_TYPE_CHAT, "");
	}

	public static String getAntiFlood() {
		return "[AntiFlood:" + Info.RAND.nextInt(1000) + "]";
	}

	public static String formatMessage(Message message) {
		String chat = " ... ";
		String chatId = "c0";
		String user = " ... ";
		String userId = "0";
		String text = "null";
		if (message != null) {
			if (message.getChatId() > 0) {
				chatId = getChatId(String.valueOf(message.getChatId()));
				chat = getChatName(message.getChatId());
			}
			if (message.getUserId() > 0) {
				userId = String.valueOf(message.getUserId());
				user = getName(message.getUserId(), true);
			}
			if (!StringUtils.isEmpty(message.getBody()))
				text = message.getBody().replace("\n", "\\n");
		}
		return Config.getMain().vk.chatLogFormat.replace("{chat}", chat).replace("{chatId}", chatId)
				.replace("{user}", user).replace("{userId}", userId).replace("{text}", text);
	}

	public static String getName(int id, boolean domain) {
		if (id > 0)
			return getUserName(id, domain);
		else
			return getGroupName(-id, domain);
	}

	public static String getName(String id, boolean domain) {
		int idd = 0;
		boolean isChat = isChat(id);
		try {
			if (isChat)
				idd = Integer.valueOf(getChatId(id));
			else
				idd = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			idd = 0;
		}
		if (isChat)
			return getChatName(idd);
		else if (idd > 0)
			return getUserName(idd, domain);
		else
			return getGroupName(-idd, domain);
	}

	public static String getChatName(int id) {
		boolean whileFlag = true;
		while (whileFlag) {
			whileFlag = false;
			try {
				Chat res = client.messages().getChat(actor).chatId(id).execute();
				if (res != null && !StringUtils.isEmpty(res.getTitle()))
					return res.getTitle();
			} catch (ApiException | ClientException e) {
				if (e instanceof ApiTooManyException) {
					whileFlag = true;
					Utils.sleep(Config.getMain().vk.answerMessageRate);
				} else
					e.printStackTrace();
			}
		}
		return "";
	}

	public static String getUserName(int id, boolean domain) {
		boolean whileFlag = true;
		while (whileFlag) {
			whileFlag = false;
			try {
				List<UserXtrCounters> res = client.users().get(actor).fields(UserField.DOMAIN)
						.userIds(String.valueOf(id)).execute();
				if (res != null && res.size() > 0)
					return res.get(0).getFirstName() + " " + res.get(0).getLastName()
							+ (domain ? " (" + res.get(0).getDomain() + ")" : "");
			} catch (ApiException | ClientException e) {
				if (e instanceof ApiTooManyException) {
					whileFlag = true;
					Utils.sleep(Config.getMain().vk.answerMessageRate);
				} else
					e.printStackTrace();
			}
		}
		return "";
	}

	public static String getGroupName(int id, boolean domain) {
		boolean whileFlag = true;
		while (whileFlag) {
			whileFlag = false;
			try {
				List<GroupFull> res = client.groups().getById(actor).groupId(String.valueOf(id)).execute();
				if (res != null && res.size() > 0)
					return res.get(0).getName() + (domain ? " (" + res.get(0).getScreenName() + ")" : "");

			} catch (ApiException | ClientException e) {
				if (e instanceof ApiTooManyException) {
					whileFlag = true;
					Utils.sleep(Config.getMain().vk.answerMessageRate);
				} else
					e.printStackTrace();
			}
		}
		return "";
	}
}

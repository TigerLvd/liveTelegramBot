package controllers;

import model.homegroups.facade.BotFacade;
import model.homegroups.facade.DBFacade;
import model.liveInfo.services.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import model.homegroups.HomeGroupBot;
import model.liveInfo.LiveInfoBot;
import model.liveInfoAdmin.LiveInfoAdminBot;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static String botName;
    private static String botToken;
    private static String botName2;
    private static String botToken2;
    private static String botName3;
    private static String botToken3;

    private static String proxyHost;
    private static Integer proxyPort;

    private static Long adminId;
    private static final Boolean USE_PROXY = false;

    public static void main(String[] args) {

        try {
            ApiContextInitializer.init();
            init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            HomeGroupBot bot;
            LiveInfoBot bot2;
            LiveInfoAdminBot bot3;

            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

            BotFacade botFacade = (BotFacade) applicationContext.getBean("botFacade");
            DBFacade dbFacade = (DBFacade) applicationContext.getBean("dbFacade");
            FieldService fieldService = (FieldService) applicationContext.getBean("FieldService");

            if (Boolean.TRUE.equals(USE_PROXY)) {
                // use PROXY
                botOptions.setProxyHost(proxyHost);
                botOptions.setProxyPort(proxyPort);
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

                bot = new HomeGroupBot(botToken, botName, adminId, botOptions, botFacade, dbFacade);
                bot2 = new LiveInfoBot(botToken2, botName2, botOptions, fieldService);
                bot3 = new LiveInfoAdminBot(botToken3, botName3, botOptions, fieldService, adminId);
            } else {
                // use VPN
                bot = new HomeGroupBot(botToken, botName, adminId, botFacade, dbFacade);
                bot2 = new LiveInfoBot(botToken2, botName2, fieldService);
                bot3 = new LiveInfoAdminBot(botToken3, botName3, fieldService, adminId);
            }

            botsApi.registerBot(bot);
            botsApi.registerBot(bot2);
            botsApi.registerBot(bot3);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        log.info("STARTED!!!");
    }

    private static void init() {
        Properties property = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            property.load(fis);

            botName = property.getProperty("bot_name");
            botToken = property.getProperty("bot_token");
            botName2 = property.getProperty("bot_name2");
            botToken2 = property.getProperty("bot_token2");
            botName3 = property.getProperty("bot_name3");
            botToken3 = property.getProperty("bot_token3");

            proxyHost = property.getProperty("proxy_host");
            proxyPort = Integer.valueOf(property.getProperty("proxy_port"));

            adminId = Long.valueOf(property.getProperty("admin_id"));

            log.info("BOT_NAME: " + botName
                    + ", BOT_NAME2: " + botName2
                    + ", BOT_NAME3: " + botName3
                    + ", PROXY_HOST: " + proxyHost
                    + ", PROXY_PORT: " + proxyPort);
        } catch (IOException e) {
            log.error("ОШИБКА: Файл свойств отсутствует!");
        }
    }
}

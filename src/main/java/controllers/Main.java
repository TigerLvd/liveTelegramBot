package controllers;

import model.homeGroups.facade.BotFacade;
import model.homeGroups.facade.DBFacade;
import model.liveInfo.services.FieldService;
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

import model.homeGroups.HomeGroupBot;
import model.liveInfo.LiveInfoBot;
import model.liveInfoAdmin.LiveInfoAdminBot;

public class Main {
    private static String BOT_NAME;
    private static String BOT_TOKEN;
    private static String BOT_NAME2;
    private static String BOT_TOKEN2;
    private static String BOT_NAME3;
    private static String BOT_TOKEN3;

    private static String PROXY_HOST;
    private static Integer PROXY_PORT;

    private static Long ADMIN_ID;
    private final static Boolean USE_PROXY = false;

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
                botOptions.setProxyHost(PROXY_HOST);
                botOptions.setProxyPort(PROXY_PORT);
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

                bot = new HomeGroupBot(BOT_TOKEN, BOT_NAME, ADMIN_ID, botOptions, botFacade, dbFacade);
                bot2 = new LiveInfoBot(BOT_TOKEN2, BOT_NAME2, botOptions, fieldService);
                bot3 = new LiveInfoAdminBot(BOT_TOKEN3, BOT_NAME3, botOptions, fieldService, ADMIN_ID);
            } else {
                // use VPN
                bot = new HomeGroupBot(BOT_TOKEN, BOT_NAME, ADMIN_ID, botFacade, dbFacade);
                bot2 = new LiveInfoBot(BOT_TOKEN2, BOT_NAME2, fieldService);
                bot3 = new LiveInfoAdminBot(BOT_TOKEN3, BOT_NAME3, fieldService, ADMIN_ID);
            }

            botsApi.registerBot(bot);
            botsApi.registerBot(bot2);
            botsApi.registerBot(bot3);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("STARTED!!!");
    }

    private static void init() {
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            property.load(fis);

            BOT_NAME = property.getProperty("bot_name");
            BOT_TOKEN = property.getProperty("bot_token");
            BOT_NAME2 = property.getProperty("bot_name2");
            BOT_TOKEN2 = property.getProperty("bot_token2");
            BOT_NAME3 = property.getProperty("bot_name3");
            BOT_TOKEN3 = property.getProperty("bot_token3");

            PROXY_HOST = property.getProperty("proxy_host");
            PROXY_PORT = Integer.valueOf(property.getProperty("proxy_port"));

            ADMIN_ID = Long.valueOf(property.getProperty("admin_id"));

            System.out.println("BOT_NAME: " + BOT_NAME
                    + ", BOT_NAME2: " + BOT_NAME2
                    + ", BOT_NAME3: " + BOT_NAME3
                    + ", PROXY_HOST: " + PROXY_HOST
                    + ", PROXY_PORT: " + PROXY_PORT);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }
}
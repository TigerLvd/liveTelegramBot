package controllers;

import model.LiveInfoBot;
import model.MyAmazingBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    private static String BOT_NAME;
    private static String BOT_TOKEN;
    private static String BOT_NAME2;
    private static String BOT_TOKEN2;

    private static String PROXY_HOST;
    private static Integer PROXY_PORT;

    public static void main(String[] args) {

        try {
            ApiContextInitializer.init();
            init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
            botOptions.setProxyHost(PROXY_HOST);
            botOptions.setProxyPort(PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

            MyAmazingBot bot = new MyAmazingBot(BOT_TOKEN, BOT_NAME, botOptions);
            LiveInfoBot bot2 = new LiveInfoBot(BOT_TOKEN2, BOT_NAME2, botOptions);

            botsApi.registerBot(bot);
            botsApi.registerBot(bot2);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.println("qe");
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

            PROXY_HOST = property.getProperty("proxy_host");
            PROXY_PORT = new Integer(property.getProperty("proxy_port"));

            System.out.println("BOT_NAME: " + BOT_NAME
                    + ", BOT_NAME2: " + BOT_NAME2
                    + ", PROXY_HOST: " + PROXY_HOST
                    + ", PROXY_PORT: " + PROXY_PORT);

        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
    }
}
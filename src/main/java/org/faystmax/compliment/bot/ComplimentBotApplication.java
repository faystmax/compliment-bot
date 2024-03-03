package org.faystmax.compliment.bot;


import org.faystmax.compliment.bot.bot.ComplimentTelegramBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class ComplimentBotApplication {
    private static final int RECONNECT_PAUSE_SEC = 5;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting App!");
        final Path complimentsPath = Paths.get(ComplimentBotApplication.class.getClassLoader().getResource("compliments.txt").toURI());
        final List<String> compliments = Files.readAllLines(complimentsPath);
        System.out.println("Loaded compliments size= " + compliments.size());

        registerBot(compliments);
    }

    private static void registerBot(final List<String> compliments) throws TelegramApiException, InterruptedException {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            var complimentTelegramBot = new ComplimentTelegramBot(
                    "LeraComplimentBot",
                    "",
                    compliments,
                    new DefaultBotOptions()
            );
            telegramBotsApi.registerBot(complimentTelegramBot);
        } catch (final TelegramApiException ex) {
            ex.printStackTrace();
            System.out.println("Cant Connect. Pause " + RECONNECT_PAUSE_SEC + " sec and try again.");
            Thread.sleep(Duration.ofSeconds(5));
            registerBot(compliments);
        }
    }
}

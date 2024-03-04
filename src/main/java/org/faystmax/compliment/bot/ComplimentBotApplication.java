package org.faystmax.compliment.bot;


import org.faystmax.compliment.bot.bot.ComplimentTelegramBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class ComplimentBotApplication {
    private static final int RECONNECT_PAUSE_SEC = 5;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting App!");

        final List<String> compliments = extractCompliments();
        System.out.println("Loaded compliments size= " + compliments.size());

        registerBot(compliments);
    }

    private static List<String> extractCompliments() throws IOException {
        try (final InputStream inputStream = ClassLoader.getSystemResourceAsStream("compliments.txt");
             final InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(inputStream));
             final BufferedReader br = new BufferedReader(isr)) {
            return br.lines().toList();
        }
    }

    private static void registerBot(final List<String> compliments) throws TelegramApiException, InterruptedException {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            var complimentTelegramBot = new ComplimentTelegramBot(
                    "LeraComplimentBot",
                    System.getenv("COMPLIMENTS_BOT_TOKEN"),
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

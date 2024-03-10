package org.faystmax.compliment.bot;


import lombok.extern.slf4j.Slf4j;
import org.faystmax.compliment.bot.bot.ComplimentTelegramBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

@Slf4j
public class ComplimentBotApplication {
    private static final int RECONNECT_PAUSE_SEC = 5;


    public static void main(String[] args) throws Exception {
        log.info("Starting App!");

        final List<String> compliments = extractCompliments();
        log.info("Loaded compliments size= " + compliments.size());

        final ComplimentTelegramBot complimentTelegramBot = registerBot(compliments);

        final var scheduledExecutorService = Executors.newScheduledThreadPool(1);
        addShutdownHook(scheduledExecutorService);

//        scheduledExecutorService.scheduleWithFixedDelay(
//                () -> complimentTelegramBot.sendCompliment(System.getenv("COMPLIMENTS_BOT_CHAT_ID")),
//                5,
//                5,
//                TimeUnit.SECONDS
//        );
    }

    private static List<String> extractCompliments() throws IOException {
        try (final InputStream inputStream = ClassLoader.getSystemResourceAsStream("compliments.txt");
             final InputStreamReader isr = new InputStreamReader(requireNonNull(inputStream), StandardCharsets.UTF_8);
             final BufferedReader br = new BufferedReader(isr)) {
            return br.lines().toList();
        }
    }

    private static ComplimentTelegramBot registerBot(final List<String> compliments) throws InterruptedException {
        try {
            var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            var complimentTelegramBot = new ComplimentTelegramBot(
                    System.getenv("COMPLIMENTS_BOT_NAME"),
                    System.getenv("COMPLIMENTS_BOT_TOKEN"),
                    compliments,
                    new DefaultBotOptions()
            );
            telegramBotsApi.registerBot(complimentTelegramBot);
            return complimentTelegramBot;
        } catch (final Throwable ex) {
            log.error("Cant Connect. Pause {} sec and try again.", RECONNECT_PAUSE_SEC, ex);
            Thread.sleep(Duration.ofSeconds(5));
            registerBot(compliments);
        }
        throw new IllegalStateException();
    }

    private static void addShutdownHook(final ScheduledExecutorService scheduledExecutorService) {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    log.info("Application is shutting down.");
                    scheduledExecutorService.shutdown();
                    log.info("Application is down.");
                })
        );
    }
}

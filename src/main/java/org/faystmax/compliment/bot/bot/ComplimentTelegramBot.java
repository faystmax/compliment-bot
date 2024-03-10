package org.faystmax.compliment.bot.bot;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ComplimentTelegramBot extends TelegramLongPollingBot {
    private final String username;
    private final List<String> compliments;

    public ComplimentTelegramBot(final String username, final String botToken, final List<String> compliments, final DefaultBotOptions options) {
        super(options, botToken);
        this.username = username;
        this.compliments = compliments;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        try {
            log.info("UpdateReceived chatId = {}", update.getMessage().getChatId());

            final String compliment = compliments.get(ThreadLocalRandom.current().nextInt(0, compliments.size()));
            sendApiMethod(new SendMessage(update.getMessage().getChatId().toString(), compliment));
            log.info("Sending compliment = '{}' to chatId = '{}'", compliment, update.getMessage().getChatId());
        } catch (final TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onRegister() {
        log.info(username + " registered successfully!");
    }

    @SneakyThrows
    public void sendCompliment(final String chatId) {
        final String compliment = compliments.get(ThreadLocalRandom.current().nextInt(0, compliments.size()));
        log.info("Sending compliment to chatId = '{}'. compliment = '{}'", chatId, compliment);
        sendApiMethod(new SendMessage(chatId, compliment));
    }
}

package org.faystmax.compliment.bot.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ComplimentTelegramBot extends TelegramLongPollingBot {
    private final String username;
    private final List<String> compliments;

    public ComplimentTelegramBot(
            final String username,
            final String botToken,
            final List<String> compliments,
            final DefaultBotOptions options
    ) {
        super(options, botToken);
        this.username = username;
        this.compliments = compliments;
    }

    @Override
    public void onUpdateReceived(final Update update) {
        try {
            final String compliment = compliments.get(ThreadLocalRandom.current().nextInt(0, compliments.size()));
            sendApiMethod(new SendMessage(update.getMessage().getChatId().toString(), compliment));
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
        System.out.println(username + " registered successfully!");
    }
}

package org.faystmax.bot

import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import kotlin.random.Random

class ComplimentTelegramBot(
    val username: String,
    botToken: String,
    val compliments: List<String>,
    options: DefaultBotOptions,
) : TelegramLongPollingBot(options, botToken) {

    override fun getBotUsername(): String = username

    override fun onRegister() {
        println("$username registered successfully!")
    }

    override fun onUpdateReceived(update: Update) {
        sendApiMethod(SendMessage(update.message.chatId.toString(), compliments.get(Random.nextInt(0, 60))))
        println(update.message.chatId)
    }

    fun sendMsg(chatId: String, msg: String) {
        sendApiMethod(SendMessage(chatId, msg))
    }
}
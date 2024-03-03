package org.faystmax

import org.faystmax.bot.ComplimentTelegramBot
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.time.Duration

const val RECONNECT_PAUSE_SEC = 5

fun main() {
    println("Starting App!")
    val compliments: List<String> =
        object {}::class.java.classLoader.getResourceAsStream("./compliments.txt")?.bufferedReader()?.readLines()!!
    println("Loaded compliments size= " + compliments.size)
    registerBot(compliments);
}

fun registerBot(compliments: List<String>) {
    try {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        val complimentTelegramBot = ComplimentTelegramBot(
            "LeraComplimentBot", "", compliments, DefaultBotOptions()
        )
        telegramBotsApi.registerBot(complimentTelegramBot)
    } catch (ex: TelegramApiException) {
        ex.printStackTrace()
        println("Cant Connect. Pause $RECONNECT_PAUSE_SEC sec and try again.")
        Thread.sleep(Duration.ofSeconds(5))
        registerBot(compliments);
    }
}

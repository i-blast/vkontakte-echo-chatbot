package com.pii.bot.vkontakte_echo_chatbot.util

import java.time.Instant
import java.time.temporal.ChronoUnit


fun Instant.toMillisPrecision(): Instant = this.truncatedTo(ChronoUnit.MILLIS)

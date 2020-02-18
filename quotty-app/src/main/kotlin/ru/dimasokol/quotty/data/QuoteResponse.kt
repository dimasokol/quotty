package ru.dimasokol.quotty.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Ответ от бэка: цитата
 */
data class QuoteResponse(
    @JsonProperty("quoteText")
    val text: String,

    @JsonProperty("quoteAuthor")
    val author: String,

    @JsonProperty("senderName")
    val sender: String?,

    @JsonProperty("senderLink")
    val senderLink: String?,
    
    @JsonProperty("quoteLink")
    val url: String
)
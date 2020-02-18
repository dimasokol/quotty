package ru.dimasokol.quotty.presentation

data class MainScreenState(
    val loading: Boolean,
    val text: String,
    val author: String
) {
}
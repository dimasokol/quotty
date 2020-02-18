package ru.dimasokol.quotty.presentation

interface MainScreenView {

    fun renderState(state: MainScreenState)
    fun displayError(message: String)

}
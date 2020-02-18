package ru.dimasokol.quotty

import ru.dimasokol.quotty.presentation.MainScreenPresenter

interface AppDependencies {
    fun mainPresenter(): MainScreenPresenter
}
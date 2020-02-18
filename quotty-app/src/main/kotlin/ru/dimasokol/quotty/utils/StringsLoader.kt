package ru.dimasokol.quotty.utils

import androidx.annotation.StringRes

interface StringsLoader {

    fun getString(@StringRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg args: Any): String

}
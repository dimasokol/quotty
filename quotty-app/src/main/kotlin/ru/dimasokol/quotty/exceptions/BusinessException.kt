package ru.dimasokol.quotty.exceptions

import androidx.annotation.StringRes

open class BusinessException(@StringRes val messageRes: Int): Exception() {
}
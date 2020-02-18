package ru.dimasokol.quotty.exceptions

import androidx.annotation.StringRes

class NetworkException(@StringRes messageRes: Int) : BusinessException(messageRes) {

}
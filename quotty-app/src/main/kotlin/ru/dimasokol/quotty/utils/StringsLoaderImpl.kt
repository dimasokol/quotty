package ru.dimasokol.quotty.utils

import android.content.Context

class StringsLoaderImpl(private val context: Context) : StringsLoader {
    override fun getString(resId: Int) = context.getString(resId)
    override fun getString(resId: Int, vararg args: Any) = context.getString(resId, args)
}
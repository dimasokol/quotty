package ru.dimasokol.quotty

import android.app.Application
import kotlinx.coroutines.Dispatchers
import ru.dimasokol.quotty.data.NetworkQuotesRepository
import ru.dimasokol.quotty.network.DefaultUrlLoader
import ru.dimasokol.quotty.presentation.MainScreenPresenter
import ru.dimasokol.quotty.utils.StringsLoaderImpl
import ru.dimasokol.quotty.utils.ThreadSleeper

class QuottyApplication: Application(), AppDependencies {

    val presenter: MainScreenPresenter by lazy {
        MainScreenPresenter(StringsLoaderImpl(this@QuottyApplication),
            NetworkQuotesRepository(DefaultUrlLoader(), ThreadSleeper()), Dispatchers.Main, Dispatchers.IO)
    }

    override fun mainPresenter(): MainScreenPresenter = presenter


}
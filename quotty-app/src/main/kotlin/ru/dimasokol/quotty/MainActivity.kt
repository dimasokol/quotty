package ru.dimasokol.quotty

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.dimasokol.quotty.presentation.MainScreenPresenter
import ru.dimasokol.quotty.presentation.MainScreenState
import ru.dimasokol.quotty.presentation.MainScreenView

class MainActivity : AppCompatActivity(), MainScreenView {

    lateinit var presenter: MainScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshButton.setOnClickListener {
            presenter.loadNextQuote()
        }

        presenter = (application as AppDependencies).mainPresenter()

        if (savedInstanceState == null) {
            presenter.loadNextQuote()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
    }

    override fun renderState(state: MainScreenState) {
        textQuote.setText(state.text)
        textAuthor.setText(state.author)

        refreshButton.isEnabled = !state.loading
        progressBar.visibility = if (state.loading) View.VISIBLE else View.INVISIBLE
    }

    override fun displayError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

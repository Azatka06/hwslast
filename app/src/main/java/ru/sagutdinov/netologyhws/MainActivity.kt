package ru.sagutdinov.netologyhws

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.ktor.utils.io.errors.IOException
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import ru.sagutdinov.netologyhws.api.Token

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivityIfAuthorized()

        btn_login.setOnClickListener {
            val login = edt_login.text.toString()
            val password = edt_password.text.toString()
            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.empty, Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    switchDeterminateBar(true)
                    try {
                        val response = Repository.authenticate(login, password)
                        if (response.isSuccessful) {
                            val token: Token? = response.body()
                            savedToken(token, this@MainActivity)
                            Toast.makeText(
                                this@MainActivity,
                                R.string.authorization_completed,
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivityIfAuthorized()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                R.string.authorization_failed,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(
                            this@MainActivity,
                            R.string.authorization_failed,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        switchDeterminateBar(false)
                    }
                }
            }
        }
        btn_registration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        startActivityIfAuthorized()
    }

    private fun startActivityIfAuthorized() {
        val token = getToken(this)
        if (!token.isNullOrEmpty()) {
            Repository.createRetrofitWithAuth(token)
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun switchDeterminateBar(isLaunch: Boolean) {
        if (isLaunch) {
            determinateBarMain.isVisible = true
            btn_login.isEnabled = false
            btn_registration.isEnabled = false
        } else {
            determinateBarMain.isVisible = false
            btn_login.isEnabled = true
            btn_registration.isEnabled = true
            determinateBarMain.isVisible = false
        }
    }

}
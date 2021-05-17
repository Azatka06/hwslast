package ru.sagutdinov.netologyhws.service

import android.provider.Settings.Global.getString
import android.util.Log
import com.auth0.android.jwt.JWT
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.sagutdinov.netologyhws.NotificationHelper
import ru.sagutdinov.netologyhws.R
import ru.sagutdinov.netologyhws.Repository
import ru.sagutdinov.netologyhws.api.Token
import ru.sagutdinov.netologyhws.getToken

class FCMService : FirebaseMessagingService(), CoroutineScope by MainScope() {

    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"] ?: ""
        val title = message.data["title"] ?: ""
        val token = getToken(this) ?: ""
        if (token.isNotEmpty() && recipientId.isNotEmpty()) {
            val jwt = JWT(token)
            Log.i("firebase", "$recipientId = ${jwt.getClaim("id").asString()}, $title")
            if (recipientId != jwt.getClaim("id").asString()) {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } else {
                NotificationHelper.notifyFromFCM(this, title)

            }
        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        launch {
            try {
                val response = Repository.firebasePushToken(Token(token))
                if (!response.isSuccessful) {
                    NotificationHelper.notifyFromFCM(
                        this@FCMService,
                        getString(R.string.push_token_on_server_failed)
                    )
                }
            } catch (e: IOException) {
                NotificationHelper.notifyFromFCM(
                    this@FCMService,
                    getString(R.string.push_token_on_server_failed)
                )
            }
        }

    }
}
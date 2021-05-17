package ru.sagutdinov.netologyhws

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import ru.sagutdinov.netologyhws.api.Token

private const val TOKEN_KEY = "TOKEN_KEY"
private const val ATTACH_MODEL_KEY = "ATTACH_MODEL_KEY"
private const val FIRST_TIME_SHARED_KEY = "first_time_shared_key"
private const val SHARED_PREF_KEY = "SHARED_PREF"

fun savedToken(token: Token?, context: Context) {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    sharedPref.edit {
        putString(
            TOKEN_KEY,
            token?.token
        )
    }


}

fun getToken(context: Context): String? {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    return sharedPref.getString(
        TOKEN_KEY,
        ""
    )
}

fun isAuthorized(context: Context) {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    val token =sharedPref.getString(
        TOKEN_KEY,
        ""
    )
    if (token != "") {
        val intent = Intent(context, FeedActivity::class.java)
        context.startActivity(intent)
        val activity = context as Activity
        activity.finish()
    }
}

fun savedAttachModel(attachModelId: String?, context: Context) {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    sharedPref.edit {
        putString(
            ATTACH_MODEL_KEY,
            attachModelId
        )
    }
}

fun getAttachModel(context: Context): String? {
    val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY,
        Context.MODE_PRIVATE
    )
    return sharedPref.getString(
        ATTACH_MODEL_KEY,
        null
    )
}

fun isFirstTime(context: Context) =
    context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE).getBoolean(
        FIRST_TIME_SHARED_KEY, true
    )

fun setNotFirstTime(context: Context) =
    context.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(SHARED_PREF_KEY, false)
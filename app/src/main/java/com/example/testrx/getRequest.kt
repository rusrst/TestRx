package com.example.testrx

import io.reactivex.Observable
import java.net.HttpURLConnection
import java.net.URL

fun getRequest(str: String) = Observable.create<String> {
    Thread.sleep(10000)
    val urlConnection =

        URL(str).openConnection()  as HttpURLConnection
    try {
        urlConnection.connect() // само обращение в сеть

        if (urlConnection.responseCode != HttpURLConnection.HTTP_OK) // проверка результата, что он 200
            it.onError(RuntimeException(urlConnection.responseMessage))
        else {
            val str = urlConnection.inputStream.bufferedReader().readText() // читаем urlConnection как текст
            it.onNext(str)
        }
    } finally {
        urlConnection.disconnect()
    }
}
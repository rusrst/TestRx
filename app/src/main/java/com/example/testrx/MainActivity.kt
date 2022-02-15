package com.example.testrx

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private var disposable: Disposable? = null
    private var observable: Observable<CurrentCurrency>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observable = getRequest("https://www.cbr-xml-daily.ru/daily_json.js")
            .map{
                val json = Json{
                    ignoreUnknownKeys = true
                }
                json.decodeFromString<CurrentCurrency>(it)
            }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        disposable = observable?.subscribe { currentCurrency ->
            currentCurrency.valutes?.forEach {
                log(it.key)
            }
        }

        val myTextView = findViewById<TextView>(R.id.myTextView)
        myTextView.setOnClickListener{
            Log.d("TAG", "mtx")
        }
    }
    private fun log(str: String){
        Log.d("TAG", str)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        observable = null
        disposable?.dispose()
        disposable = null
        super.onStop()
    }
}
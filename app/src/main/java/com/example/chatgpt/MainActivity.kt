package com.example.chatgpt

import ChatGPT
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var chatGPT: ChatGPT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatGPT = ChatGPT("gpt2")


        findViewById<Button>(R.id.btn_view).setOnClickListener {
            val text = findViewById<EditText>(R.id.edit_text).text
            if (text.isEmpty())
                Toast.makeText(
                    this.applicationContext,
                    "please enter valid text",
                    Toast.LENGTH_LONG
                ).show()
            else
                callApi(text)
        }


    }

    private fun callApi(text: Editable) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = chatGPT.generateResponse(text.toString())
                withContext(Dispatchers.Main) {
                    findViewById<TextView>(R.id.text_view).text = response
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
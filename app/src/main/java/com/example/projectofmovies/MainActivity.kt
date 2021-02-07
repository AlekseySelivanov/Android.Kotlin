package com.example.projectofmovies

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private var number: Byte = 5
    private var textView: TextView? = null
    private var textView2: TextView? = null
    private var cLayout: ConstraintLayout? = null
    private var counter:Int = 0
    lateinit var button : Button
    private var start:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button = findViewById(R.id.button)
        button.setOnClickListener(listener)

        val genre:Genre = Genre("SciFi", 1)
        textView = findViewById(R.id.textView)
        textView?.text = genre.toString()
        textView2 = findViewById(R.id.textView2)
        cLayout = findViewById(R.id.cLayout)
        textView2?.setText(number.toString())

        Thread{
            start = true
            while(start){
                Thread.sleep(1000)
                  runOnUiThread{
                      if(counter == 5) cLayout?.setBackgroundColor(Color.BLUE)
                      textView2?.setText(counter.toString())
                      counter++
                }
            }
        }.start()
    }



    val listener= View.OnClickListener { view ->
        when (view.getId()) {
            R.id.button -> {
                // Do some work here
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        start = false
    }


}
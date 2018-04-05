package com.tjliqy.circularmenu

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circularMenu = this.findViewById<CircularMenu>(R.id.circular_menu)
        circularMenu
                .addItem(
                        CircularMenuItem(R.mipmap.email, {
                            Toast.makeText(this@MainActivity, "select Email", Toast.LENGTH_LONG).show()
                            return@CircularMenuItem true
                        })
                )
                .addItem(
                        CircularMenuItem(R.mipmap.settings, {
                            Toast.makeText(this@MainActivity, "select Settings", Toast.LENGTH_LONG).show()
                            return@CircularMenuItem true
                        })
                )
                ?.refreshItemPosition()

    }

}

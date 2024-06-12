//https://drive.google.com/drive/folders/1v-oOsfYLR4GWi_yqswW2aVRuM-1PP3Sw
package com.example.dicegamecwk

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        supportActionBar?.hide()//hides top bar
        setContentView(R.layout.activity_main)

        //button for new game and about me are set below
        val btnNewGame=findViewById<Button>(R.id.btn_newGame)
        val btnAbout=findViewById<Button>(R.id.btn_about)
        val diceImg=findViewById<ImageView>(R.id.imageView)

        diceImg.setPadding(10,10,10,10)//setting padding


        var btnInstructions=findViewById<Button>(R.id.btn_instructions)
//setting on click listener for instruction button
        btnInstructions.setOnClickListener{

            val popView=layoutInflater.inflate(R.layout.instructions,null)

            val width=1000
            val height=1200
            val focusable=true
            val popupInstructions=PopupWindow(popView,width,height,focusable)
            popupInstructions.showAtLocation(popView,Gravity.CENTER,0,0)

            popView.setOnTouchListener(OnTouchListener { v, event ->
                popupInstructions.dismiss()
                true
            })


        }


        btnNewGame.setOnClickListener{
            val eTxtPlayerName=findViewById<EditText>(R.id.eTxtPersonName)
            val playerName=eTxtPlayerName.text.toString()
            val gameIntent = Intent(
                this@MainActivity, GameActivity::class.java)

            gameIntent.putExtra("playerName",playerName)


           startActivity(gameIntent)

        }


        btnAbout.setOnClickListener{//set on click listener to about me button

        val popView=layoutInflater.inflate(R.layout.about_me_popup,null)

            //shows set instruction in a pop up window

            val width=1000
            val height=1000
            val focusable=true
            val popupAboutMe=PopupWindow(popView,width,height,focusable)
            popupAboutMe.showAtLocation(popView,Gravity.CENTER,0,0)

            popView.setOnTouchListener(OnTouchListener { v, event ->
                popupAboutMe.dismiss()
                true
            })


        }




    }
}
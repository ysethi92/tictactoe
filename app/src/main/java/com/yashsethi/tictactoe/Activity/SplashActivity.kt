package com.yashsethi.tictactoe.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.yashsethi.tictactoe.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        val tv: TextView = findViewById(R.id.textWelcome)
        val myAnim = AnimationUtils.loadAnimation(this, R.anim.mytransition)
        tv.startAnimation(myAnim)

        val imc : ImageView = findViewById(R.id.circle)
        val myCircleAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_middle)
        imc.startAnimation(myCircleAnim)

        val imcross : ImageView = findViewById(R.id.cross)
        val myCrossAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_middle)
        imcross.startAnimation(myCrossAnim)

        Handler().postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }, 3000)
    }
}

package com.dashingqi.router.dqgradle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dashingqi.router.annotaions.Destination

@Destination(url = "router://main-home", description = "应用主页-main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
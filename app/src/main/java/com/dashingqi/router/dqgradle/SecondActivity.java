package com.dashingqi.router.dqgradle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dashingqi.router.annotaions.Destination;

@Destination(
        url = "router://oage-home",
        description = "应用主页"
)
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
package com.example.custom_layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EqLayout eql = findViewById(R.id.eqlayout);
        for (int i=0;i<14;i++) {
            Button b=new Button(this);
            b.setText("#"+i);
            eql.addView(b);
        }
    }
}

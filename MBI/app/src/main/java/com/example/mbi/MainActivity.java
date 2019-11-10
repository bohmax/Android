package com.example.mbi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button calcola;
    private EditText altezza, peso;
    private TextView risposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcola=(Button)findViewById(R.id.calcola);
        altezza=(EditText)findViewById(R.id.altezza);
        peso=(EditText)findViewById(R.id.peso);
        risposta=(TextView)findViewById(R.id.risposta);
        calcola.setOnClickListener(this);
        calcola.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v==calcola) {
            try {
                float a = Float.parseFloat(altezza.getText().toString());
                float p = Float.parseFloat(peso.getText().toString());
                float bmi = p / (a * a);
                System.out.println(bmi);
                risposta.setText(String.format(Locale.getDefault(),"%.1f", bmi));
            } catch (Exception e) {
                risposta.setText("?");
            }
        }
    }
}

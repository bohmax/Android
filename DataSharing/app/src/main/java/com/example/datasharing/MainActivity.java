package com.example.datasharing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyDatabase data = null;
    private Button exec, show;
    private EditText from,key,value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new MyDatabase(getApplicationContext());
        try {
            data.createRecords( "Largo Pontecorvo");
        } catch (SQLiteConstraintException ignore){}
        //SimpleCursorAdapter mCursor = data.selectRecords(getApplicationContext());
        exec=(Button)findViewById(R.id.button);
        show=(Button)findViewById(R.id.button1);
        key=(EditText)findViewById(R.id.editText2);
        value=(EditText)findViewById(R.id.editText3);
        exec.setOnClickListener(this);
        show.setOnClickListener(this);
        show.setEnabled(true);
        exec.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v == exec){
            try {
                data.createRecords(value.getText().toString());
                Toast.makeText(getApplicationContext(),"successo",Toast.LENGTH_LONG).show();
            } catch (SQLiteConstraintException e){Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();}
        } else if (v==show){
            Intent myIntent = new Intent(getBaseContext(), ListActivity.class);
            startActivity(myIntent);
        }
    }
}

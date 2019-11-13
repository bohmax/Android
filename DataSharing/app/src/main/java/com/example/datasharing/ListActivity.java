package com.example.datasharing;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

        MyDatabase data = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.listactivity);
            data = new MyDatabase(getApplicationContext());
            final SimpleCursorAdapter mCursor = data.selectRecords(getApplicationContext());
            ListView lw = findViewById(android.R.id.list);
            lw.setAdapter(mCursor);

            lw.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean value = data.delete(((TextView)view.findViewById(R.id.number_entry)).getText().toString(),null, mCursor);
                    if (value)
                        Toast.makeText(getApplicationContext(), "successo", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
                    return value;
                }
            });
        }


}


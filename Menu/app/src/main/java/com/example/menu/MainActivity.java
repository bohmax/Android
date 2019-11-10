package com.example.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=(TextView)findViewById(R.id.text);
        this.setTitle("Miao"); //cambia il titolo della applicazione
    }

    //--------------- Menu proveniente dal file xml -----------------

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, m);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu m) {
        MenuItem mu = m.getItem(0);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1 :
                startActivity(new Intent(this, Main2Activity.class));
                return true;
            case R.id.menu2 :
                return true;
            case R.id.sotto1:
                return true;
            case R.id.sotto2:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //-----------------------------------------------------

    //--------------- Popup menu -----------------
    public void showPopup(View v) { //viene invocato dal bottone nel xml
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override //listener
            public boolean onMenuItemClick(MenuItem item) {
                text.setText("Selezionato: " + item.getTitle());
                return true;
            }
        });
        popup.show();
    }

    //--------------------------------------------
}

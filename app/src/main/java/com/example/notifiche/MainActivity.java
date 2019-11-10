package com.example.notifiche;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bottone;
    private CoordinatorLayout sblayout;
    NotificationCompat.Builder builder;
    NotificationManager nm;

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogoFreammento alertDialog = DialogoFreammento.newInstance("Some title");
        alertDialog.show(fm, "fragment_alert");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottone= findViewById(R.id.bottone);
        View layout = getLayoutInflater().inflate(R.layout.toastlayout,
                (ViewGroup) findViewById(R.id.toast_root));

        sblayout = findViewById(R.id.myCoordinatorLayout);
        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("Sono un toast custom!");
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, -60); //cambia lo posizione
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

        bottone.setOnClickListener(this);
        bottone.setEnabled(true);

        //notifica classica
        String str = "Canale";
        builder = new NotificationCompat.Builder(this, str)
                .setSmallIcon(R.drawable.ic_stat_name).setContentTitle("Daniel")
                .setContentText("I went to the zoo and saw a monkey!");
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        showAlertDialog();
    }

    @Override
    public void onClick(View v) {
        if(v==bottone) {
            Snackbar sb = Snackbar.make(sblayout, R.string.sb, 1500);
            sb.show();
            sb.setAction(R.string.etichetta, new View.OnClickListener() {
                public void onClick(View v) {
                    //Log.d("YES", "yes yes yes");
                    nm.notify(1, builder.build());
                }
            });
        }
    }
}

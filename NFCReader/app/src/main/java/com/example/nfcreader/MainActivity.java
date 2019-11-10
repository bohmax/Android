package com.example.nfcreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    NfcAdapter nfc;
    PendingIntent pendingIntent = null;
    TextView text;
    ProgressBar progress;

    Handler handler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            endKeyboard();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        progress = findViewById(R.id.progressBar_cyclic);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        nfc = NfcAdapter.getDefaultAdapter(this);
        if (nfc != null )
            if (nfc.isEnabled()) {
                Toast.makeText(this, "NFC AVAIBLE", Toast.LENGTH_LONG).show();
                pendingIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            } else showWirlessSettings();
        else
            Toast.makeText(this, "NFC NOT AVAIBLE :(", Toast.LENGTH_LONG).show();
        toolbar.setFocusable(false);
        toolbar.setFocusableInTouchMode(false);
        progress.setVisibility(View.GONE);
    }

    private void endKeyboard(){
        text.setText("");
        progress.setVisibility(View.GONE);
        handler.removeCallbacks(task);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void onBackPressed() { //se l utente preme il tasto back
        super.onBackPressed();
        progress.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //tastiera
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        String key = KeyEvent.keyCodeToString(keyCode);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE); //disabilita input
        // key will be something like "KEYCODE_A" - extract the "A"

        // use pattern to convert int keycode to some character
        //Matcher matcher = KEYCODE_PATTERN.matcher(key);
        //if (matcher.matches()) {
            // append character to textview
        String replace = key.replace("KEYCODE_", "");
        if(!replace.equals("ENTER")) {
            text.append(replace);
            progress.setVisibility(View.VISIBLE);
            if(text.length() == 1)
                handler.postDelayed(task,10000);
        }
        else {
            endKeyboard();
        }
        /*} else if (event.getAction()== KeyEvent.ACTION_UP)
            text.append("\n");
            text.setText("");*/
        // let the default implementation handle the event
        //return super.onKeyDown(keyCode, event);
        return true;
    }


    //nfc in giù
    @Override
    protected void onResume() {
        super.onResume();
        if(nfc != null)
            if (nfc.isEnabled())
                nfc.enableForegroundDispatch(this, pendingIntent,null, null);
            else showWirlessSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfc != null && nfc.isEnabled())
            nfc.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) { //viene chiamata quando lancia la pending intent?
        super.onNewIntent(intent);

        NdefMessage[] messages = getNdefMessages(intent);
        if(messages != null){
            System.out.println(messages[0]);
            text.setText(displayNFCArray(messages[0]));
        } else
            text.setText("Tag vuoto!");
    }

    private NdefMessage[] getNdefMessages(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }
            return messages;
        } else {
            return null;
        }
    }

    static String displayNFCArray(NdefMessage messag) {
        StringBuilder builder = new StringBuilder();
        NdefMessage m = messag;
        for (NdefRecord r: m.getRecords()) {
            if (r.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
                if (Arrays.equals(r.getType(), NdefRecord.RTD_TEXT)) {
                    //builder.append((char) aByte);
                    byte[] payloadBytes = r.getPayload();

                    //-----------------

                    //bisogna leggere l'header del pacchetto, altrimenti apparirà sempre un en davanti o quasi
                    //questa operazione si poteva anche fare bit a bit con payloadBytes[0] & 0x080: nota 0x080 equivale a 1000000 infatti si deve fare un & bit a bit fino al settimo e valutare questo
                    Charset charset = ((payloadBytes[0] & 0x080) == 0) ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16; //status byte: bit 7 indicates encoding (0 = UTF-8, 1 = UTF-16)
                    //0x03F i primi 5 bit che contengono la lunghezza infatti sono a 1
                    int languageLength = payloadBytes[0] & 0x03F - 1; //status byte: bits 5..0 indicate length of language code

                    //-----------------
                    builder.append(new String(payloadBytes, languageLength + 1, payloadBytes.length - 1 - languageLength,charset)).append(" \n");
                    Log.d("READING", new String(payloadBytes, StandardCharsets.UTF_8));
                }
            }
        }
        builder.deleteCharAt(builder.length()-1);
        Log.d("TAG", builder.toString());
        return builder.toString();
    }
    /*private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++ ) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumbTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }

            DisplayMsgs(msgs);
        }
    }

    private static NdefMessage getTestMessage() {
        byte[] mimeBytes = "application/com.android.cts.verifier.nfc"
                .getBytes(Charset.forName("UTF-8"));
        byte[] id = new byte[] {1, 3, 3, 7};
        byte[] payload = "CTS Verifier NDEF Push Tag".getBytes(Charset.forName("UTF-8"));
        return new NdefMessage(new NdefRecord[] {
                new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, id, payload)
        });
    }

    private void DisplayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();

        for(ParsedNdefRecord record: records) {
            String str = record.str();
            builder.append(str).append("\n");
        }

        text.setText(builder.toString());
    }*/

    private void showWirlessSettings(){ //dovrei far partire una nuova activity per fare bene questa richiesta
        //altrimenti va in crash il programma se faccio back senza attivare nfc
        Toast.makeText(this, "Abilita NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

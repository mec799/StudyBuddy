package com.mec.studybuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.security.AccessController.getContext;

public class sinif extends AppCompatActivity {
    boolean isTimeRunning = false, isBreak = false;
    final static long DEFAULT_WORKING_TIME = 1500000, DEFAULT_BREAK_TIME = 300000;
    static long startTime, breakTime, millisLeft;
    private Button mesaj_gönder_buton ;
    private EditText mesaj_yaz_buton ;
    private RecyclerView rv ;
    private ToggleButton toggle ;
    private TextView etut_ad ;
    public Integer tekrar_sayisi , etut_count , tenefus_count;
    private ImageView image ;
    private ArrayList<String> GösterilecekMesajlar ;
    private MessageAdapter adapter ;
    public Integer count ;
    ImageButton resumePauseButton, resetButton;
    CountDownTimer timer;
    ProgressBar timerProgressBar;
    TextView timerText , bitti;
    Vibrator vibrator;
    Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinif);
        Toast.makeText(sinif.this, "Etüte Başlamak İçin 40 Saniyeniz Var !", Toast.LENGTH_SHORT).show();
        resumePauseButton = findViewById(R.id.resumePauseButton);
        resetButton = findViewById(R.id.resetButton);
        toggle = findViewById(R.id.toggle);
        image = findViewById(R.id.image);
        etut_ad = findViewById(R.id.etut_ad);
        bitti = findViewById(R.id.bitti);

        timerProgressBar = findViewById(R.id.progressBar);
        rv = findViewById(R.id.rv);
        timerText = findViewById(R.id.textView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        resetButton.setVisibility(View.INVISIBLE);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        GösterilecekMesajlar = new ArrayList<>();

        adapter = new MessageAdapter(this ,GösterilecekMesajlar);
        rv.setAdapter(adapter);
        mesaj_gönder_buton = findViewById(R.id.mesaj_gönder_buton);
        mesaj_yaz_buton = findViewById(R.id.mesaj_yaz_buton);
        toggle.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        etut_ad.setVisibility(View.INVISIBLE);
        bitti.setText("1. Etüt Zamanı");
        Handler hndler = new Handler();

        hndler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resumePauseButton.setVisibility(View.INVISIBLE);

            }
        },40000);


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //The toggle is enabled
                    rv.setVisibility(View.INVISIBLE);
                    mesaj_gönder_buton.setVisibility(View.INVISIBLE);
                    mesaj_yaz_buton.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.VISIBLE);
                    etut_ad.setVisibility(View.VISIBLE);
                    onSet();

                } else {
                    // The toggle is enabled
                    rv.setVisibility(View.VISIBLE);
                    mesaj_gönder_buton.setVisibility(View.VISIBLE);
                    mesaj_yaz_buton.setVisibility(View.VISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    etut_ad.setVisibility(View.INVISIBLE);
                }
            }
        });


        Intent intent = getIntent();
        String etutQey = intent.getStringExtra("qey");
        DatabaseReference road = FirebaseDatabase.getInstance().getReference("add").child(etutQey);
        road.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Etut_Olustur etut = snapshot.getValue(Etut_Olustur.class);
                String etutA = etut.getEtut_dakika();
                String etutB = etut.getTenefus_dakika();
                long millisInput = Long.parseLong(etutA) * 60000;
                long millisInputB = Long.parseLong(etutB) * 60000;
                startTime = millisInput;
                breakTime = millisInputB;
                tekrar_sayisi = Integer.parseInt(etut.getEtut_tekrar());
                millisLeft = (isBreak) ? breakTime : startTime;
                onStart();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("add").child(etutQey).child("chat");
        mesaj_gönder_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth yetki = FirebaseAuth.getInstance();
                FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                String kullaniciId = firebaseKullanici.getUid() ;
                DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                kullaniciYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(getContext() == null ){

                            return;
                        }
                        Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                        if (mesaj_yaz_buton.getText().toString().matches("")) {
                            Toast.makeText(sinif.this, "Mesaj Boş Olamaz !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Messages mesaj = new Messages(user.getNickName()+" : "+mesaj_yaz_buton.getText().toString() ,"" );
                            myRef.push().setValue(mesaj);
                            mesaj_yaz_buton.setText("");

                        }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GösterilecekMesajlar.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    Messages mesajj =  d.getValue(Messages.class);
                    String key = d.getKey();
                    mesajj.setKey(key);
                    GösterilecekMesajlar.add(mesajj.getKullanıcı_mesaj().toString());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        count = 0 ;
        etut_count = 1 ;
        tenefus_count = 0 ;






    }
    public void onDestroy() {
        resetTimer();
        durumH();

        super.onDestroy();

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("ÇIKIŞ")
                .setMessage("Etütten ayrılmak istediğine emin misin ?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        durumH();

                        sinif.super.onBackPressed();
                    }
                }).create().show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        defineProgress();
        updateTimerProgress();
        resumePauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumePauseButton.setVisibility(View.INVISIBLE);
                toggle.setVisibility(View.VISIBLE);
                if (isTimeRunning){
                    pauseTimer();
                }
                else{
                    startTimer();
                    durumE();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }
    private void durumE(){

        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        DatabaseReference oda = FirebaseDatabase.getInstance().getReference("suanda").child(kullaniciId);
        durum durum = new durum("evet");
        oda.child("evet").setValue(durum);

    }
    private void durumH(){

        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        try {
            //  Block of code to try
            DatabaseReference ado = FirebaseDatabase.getInstance().getReference("suanda").child(kullaniciId);
            ado.child("evet").removeValue();

        }
        catch(Exception e) {
            //  Block of code to handle errors
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.workingTimerOption:
                Intent workIntent = new Intent(sinif.this, SetTimeActivty.class);

                workIntent.putExtra("startTime", startTime);
                workIntent.putExtra("requestCode", 10);
                startActivityForResult(workIntent, 10);

                return true;
            case R.id.breakTimerOption:
                Intent breakIntent = new Intent(sinif.this, SetTimeActivty.class);

                breakIntent.putExtra("breakTime", breakTime);
                breakIntent.putExtra("requestCode", 20);
                startActivityForResult(breakIntent, 20);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isTimeRunning", isTimeRunning);
        outState.putLong("millisLeft", millisLeft);
        outState.putBoolean("isBreak", isBreak);

        if (isTimeRunning) {
         //   destroyTimer();
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        isTimeRunning = savedInstanceState.getBoolean("isTimeRunning");
        millisLeft = savedInstanceState.getLong("millisLeft");
        isBreak = savedInstanceState.getBoolean("isBreak");

        defineProgress();
        updateTimerProgress();

        if (millisLeft != startTime)
            updateResumePauseButton();

        if (isTimeRunning)
            startTimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                startTime = Objects.requireNonNull(data.getExtras()).getLong("startTime");

                resetTimer();
                defineProgress();
            }
            else if (requestCode == 20) {
                breakTime = Objects.requireNonNull(data.getExtras()).getLong("breakTime");

                resetTimer();
                defineProgress();
            }
        }
    }


    private void startTimer() {
        ++count ;
        isTimeRunning = true;
        timer = new CountDownTimer(millisLeft, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisLeft = millisUntilFinished;
                updateTimerProgress();
            }
            @Override
            public void onFinish() {
                if(count == (tekrar_sayisi*2)-1){
                    bitti.setText("Etüt Bitti");
                    durumH();
                    kayit();
                    kahveKayit();
                }
                else {
                    alertTimerFinish();
                    changeTimerType();
                    defineProgress();
                    startTimer();
                }

            }
        }.start();
        updateResumePauseButton();
    }
    public void defineProgress() {
        timerProgressBar.setMax((int) TimeUnit.MILLISECONDS.toSeconds((isBreak) ?
                breakTime : startTime));
        timerProgressBar.setProgress(timerProgressBar.getMax());
    }
    private void alertTimerFinish() {
        vibrator.vibrate(1000);
        ringtone.play();
    }
    private void changeTimerType() {
        millisLeft = (!isBreak) ? breakTime : startTime;
        isBreak = !isBreak;
        if(millisLeft == startTime){
            ++etut_count;

            bitti.setText(etut_count+". Etüt Zamanı");
        }else {
            ++tenefus_count;
            bitti.setText(tenefus_count+". Tenefüs Zamanı");
        }
    }
    private void destroyTimer() {
        timer.cancel();
        isTimeRunning = false;
    }
    private void pauseTimer() {
        destroyTimer();
        updateResumePauseButton();
    }
    private void resetTimer() {
        if (isTimeRunning)
            destroyTimer();

        millisLeft = (!isBreak) ? startTime : breakTime;
        updateTimerProgress();
        updateResumePauseButton();
    }

    private void kahveKayit (){
        Intent intent = getIntent();
        String etutQey = intent.getStringExtra("qey");
        DatabaseReference nesli = FirebaseDatabase.getInstance().getReference("add").child(etutQey);
        nesli.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Etut_Olustur etut = snapshot.getValue(Etut_Olustur.class);
                String headd = etut.getEtut_baslik();
                int a = Integer.parseInt(etut.getEtut_dakika());
                int b = Integer.parseInt(etut.getEtut_tekrar());
                int c = a*b ;

                int kahve = c / 5 ;

                String damn = Integer.toString(kahve);
                FirebaseAuth yetki = FirebaseAuth.getInstance();
                FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                String kullaniciId = firebaseKullanici.getUid() ;



                DatabaseReference life = FirebaseDatabase.getInstance().getReference("kahvecore").child(kullaniciId);
                kahve_kayit coffe = new kahve_kayit(damn);
                life.push().setValue(coffe);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }
    private void kayit() {
        Intent intent = getIntent();
        String etutQey = intent.getStringExtra("qey");


        DatabaseReference neset = FirebaseDatabase.getInstance().getReference("add").child(etutQey);
        neset.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Etut_Olustur etut = snapshot.getValue(Etut_Olustur.class);
                String headd = etut.getEtut_baslik();
                int a = Integer.parseInt(etut.getEtut_dakika());
                int b = Integer.parseInt(etut.getEtut_tekrar());
                int c = a*b ;
                String d = Integer.toString(c);
                etut_gecmisi_kayit kayit = new etut_gecmisi_kayit(headd , d);

                FirebaseAuth yetki = FirebaseAuth.getInstance();
                FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                String kullaniciId = firebaseKullanici.getUid();
                String child = etut.getEtut_baslik()+" --- "+d+" dakika " ;
                DatabaseReference ince = FirebaseDatabase.getInstance().getReference("etutKayitlar").child(kullaniciId).child(child);
                ince.setValue(kayit);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateTimerProgress() {
        String second = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(millisLeft) % 60);
        String minute = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(millisLeft) % 60);
        String hour = String.valueOf(TimeUnit.MILLISECONDS.toHours(millisLeft));

        int hourInt = Integer.parseInt(hour);

        if (Integer.parseInt(minute) < 10 && hourInt > 0)
            minute = "0" + minute;
        if (Integer.parseInt(second) < 10)
            second = "0" + second;

        if (hourInt > 0)
            timerText.setText(getString(R.string.hour_time, hour, minute, second));
        else
            timerText.setText(getString(R.string.time, minute, second));

        timerProgressBar.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(millisLeft));
    }



    private void updateResumePauseButton() {
        resumePauseButton.setImageResource(isTimeRunning ?
                R.drawable.baseline_pause_24 : R.drawable.baseline_play_arrow_24);
    }
    private void onSet() {


        int minimum = 0;
        int maksimum = 4;

        int randomSayi = new Random().nextInt((maksimum - minimum ) + 1) + minimum;

        switch (randomSayi){

            case 0:
                etut_ad.setText("Telefona bakmayı bırak");
                break;
            case 1:
                etut_ad.setText("İşine odaklan");
                break;
            case 2:
                etut_ad.setText("Bir kahve iç ve işine bak");
                break;
            case 3:
                etut_ad.setText("Hedeflerini hatırla");
                break;
            case 4:
                etut_ad.setText("Asla vazgeçme");
        }

    }
}
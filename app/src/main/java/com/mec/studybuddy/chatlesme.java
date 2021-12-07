package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class chatlesme extends AppCompatActivity {
    private Button send ;
    private EditText write ;
    private RecyclerView rebe ;
    private ArrayList<String> GösterilecekMesajlar ;
    private MessageAdapter adapter ;
    private FirebaseUser currentUser ;
    private Toolbar barl ;
    private FirebaseAuth auth ;
    private int notificationId = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlesme);
        Intent intent = getIntent();
        String userkey = intent.getStringExtra("ol");
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        barl = findViewById(R.id.barl);
        setSupportActionBar(barl);
        getSupportActionBar().setTitle("Geri Dön");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*toolbar.inflateMenu(R.menu.toolbar_menu);*/
        barl.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent il = new Intent(chatlesme.this , etut_merkezi.class);
                startActivity(il);
                finish();
            }
        });
        write = findViewById(R.id.write);
        send = findViewById(R.id.send);

        rebe = (RecyclerView)findViewById(R.id.rebe);
        rebe.setHasFixedSize(true);
        rebe.setLayoutManager(new LinearLayoutManager(this));
        GösterilecekMesajlar = new ArrayList<>();
        GösterilecekMesajlar.add("selam ");
        GösterilecekMesajlar.add("merhaba ");
        adapter = new MessageAdapter(this ,GösterilecekMesajlar);
        rebe.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat").child(kullaniciId).child(userkey);
        DatabaseReference yourRef = database.getReference("Chat").child(userkey).child(kullaniciId);

        send.setOnClickListener(new View.OnClickListener() {
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
                        if (write.getText().toString().matches("")) {
                            Toast.makeText(chatlesme.this, "Mesaj Boş Olamaz !", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Messages mesaj = new Messages(user.getNickName()+" : "+write.getText().toString() ,"" );
                            myRef.push().setValue(mesaj);
                            yourRef.push().setValue(mesaj);
                            write.setText("");


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

    }

    private void push(){

        // Intent
        Intent intent = new Intent(chatlesme.this, AlarmReceiver.class);

        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", "yeni mesaj");

        // PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                chatlesme.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR));
        startTime.set(Calendar.MINUTE,startTime.get(Calendar.MINUTE));
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();

        // Set Alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
    }
}
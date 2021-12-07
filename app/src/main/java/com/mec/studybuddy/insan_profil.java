package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class insan_profil extends AppCompatActivity {
    private ImageView insan_profil ;
    private adap adap ;
    private ArrayList<String> GösterilecekMesajlar ;
    private TextView insan_nick , durum , coffe  ;
    private RecyclerView reve ;
    private Context acon ;
    private ArrayList<Integer> coffeler = new ArrayList<Integer>();
    private  Toolbar tull ;
    private Button chatsend , follow ;
    private TextView takibci  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insan_profil);
        Intent intent = getIntent();
        String userkey = intent.getStringExtra("muz");
        chatsend = findViewById(R.id.chatsend);
        coffe = findViewById(R.id.coffe);
        durum = findViewById(R.id.durum);
        follow = findViewById(R.id.follow);
        chatsend.setVisibility(View.INVISIBLE);
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        DatabaseReference oda = FirebaseDatabase.getInstance().getReference("Üyeler").child(userkey);
        oda.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici_kayit a = snapshot.getValue(Kullanici_kayit.class);
                DatabaseReference oba = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(kullaniciId);
                oba.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot br) {
                        if (br.hasChild(a.getNickName())) {
                            // run some code

                            chatsend.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici_kayit rop = snapshot.getValue(Kullanici_kayit.class);
                DatabaseReference ahmet = FirebaseDatabase.getInstance().getReference("istekler").child(userkey);
                ahmet.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(rop.getNickName())) {
                            // run some code
                            follow.setVisibility(View.INVISIBLE);
                        }
                        else {
                            DatabaseReference yolumyol = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                            yolumyol.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Kullanici_kayit rop = snapshot.getValue(Kullanici_kayit.class);
                                    DatabaseReference ahmet = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(userkey);
                                    ahmet.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(rop.getNickName())) {
                                                // run some code
                                                follow.setVisibility(View.INVISIBLE);
                                            }
                                            else {
                                                follow.setVisibility(View.VISIBLE);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        chatsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent il = new Intent(insan_profil.this , chatlesme.class);
                il.putExtra("ol" , userkey);

                startActivity(il);
                finish();

            }
        });
        reve = findViewById(R.id.reve);
        reve.setHasFixedSize(true);
        reve.setLayoutManager(new LinearLayoutManager(acon));
        GösterilecekMesajlar = new ArrayList<>();

        adap = new adap(acon ,GösterilecekMesajlar);

        reve.setAdapter(adap);


        tull = findViewById(R.id.tull);
        setSupportActionBar(tull);
        getSupportActionBar().setTitle("Geri Dön");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*toolbar.inflateMenu(R.menu.toolbar_menu);*/
        tull.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent il = new Intent(insan_profil.this , etut_merkezi.class);
                startActivity(il);
                finish();
            }
        });
        insan_profil =findViewById(R.id.insan_profil);

        takibci =findViewById(R.id.takibci);
        insan_nick =findViewById(R.id.insan_nick);

        DatabaseReference war = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(userkey);
        war.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int size = (int) snapshot.getChildrenCount();

                    takibci.setText(size+" Arkadaş");

                }
                else {
                    takibci.setText("0 Arkadaş");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        DatabaseReference enes = FirebaseDatabase.getInstance().getReference("etutKayitlar").child(userkey);
        enes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GösterilecekMesajlar.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String ttt = child.getKey();
                    GösterilecekMesajlar.add(ttt);
                    adap.notifyDataSetChanged();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference road = FirebaseDatabase.getInstance().getReference("Üyeler").child(userkey);
        road.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici_kayit user =snapshot.getValue(Kullanici_kayit.class);
                Glide.with(getApplicationContext()).load(user.getBio()).into(insan_profil);
                insan_nick.setText(user.getNickName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                kullaniciYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanici_kayit rop = snapshot.getValue(Kullanici_kayit.class);
                        DatabaseReference istek = FirebaseDatabase.getInstance().getReference("istekler").child(userkey);
                        takip eleman = new takip(kullaniciId ,rop.getNickName() );
                        istek.child(rop.getNickName()).setValue(eleman);

                        Toast.makeText(getApplicationContext(), "İstek gönderildi!", Toast.LENGTH_SHORT).show();
                        follow.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });
        DatabaseReference neok = FirebaseDatabase.getInstance().getReference("suanda").child(userkey);
        neok.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("evet")) {
                    // run some code
                    durum.setVisibility(View.VISIBLE);
                }
                else {

                    durum.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference merkez = FirebaseDatabase.getInstance().getReference("kahvecore").child(userkey);
        merkez.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coffeler.clear();
                for(DataSnapshot d : snapshot.getChildren()){

                    kahve_kayit cof = d.getValue(kahve_kayit.class);

                    coffeler.add(Integer.parseInt(cof.getKahve()));



                }

                if(coffeler.isEmpty()){
                    coffe.setText("0");


                }
                else {

                    int sum = 0;
                    for(Integer d : coffeler){
                        sum += d;


                    }

                    coffe.setText(sum+"");

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }
}
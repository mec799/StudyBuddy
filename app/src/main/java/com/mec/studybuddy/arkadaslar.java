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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class arkadaslar extends AppCompatActivity {
    private arkadasapater adapter ;
    private ArrayList<takip> GösterilecekMesajlar ;
    private Context contra ;
    private Toolbar toolbarO ;

    private RecyclerView bere ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arkadaslar);


        toolbarO = findViewById(R.id.toolbarO);
        setSupportActionBar(toolbarO);
        getSupportActionBar().setTitle("Geri Dön");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*toolbar.inflateMenu(R.menu.toolbar_menu);*/
        toolbarO.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent il = new Intent(arkadaslar.this , etut_merkezi.class);
                startActivity(il);
                finish();
            }
        });

        bere = findViewById(R.id.bere);

        bere.setHasFixedSize(true);
        bere.setLayoutManager(new LinearLayoutManager(contra));
        GösterilecekMesajlar = new ArrayList<>();

        adapter = new arkadasapater(contra ,GösterilecekMesajlar);

        bere.setAdapter(adapter);
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;

        DatabaseReference ened = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(kullaniciId);
        ened.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GösterilecekMesajlar.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    takip el = child.getValue(takip.class);

                    GösterilecekMesajlar.add(el);
                    adapter.notifyDataSetChanged();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.mec.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.Random;

public class Fragment_Profil_Genel extends Fragment {
    private bildirimadapter adapter ;
    private ArrayList<takip> GösterilecekMesajlar ;

    private RecyclerView rw ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil_genel_layout , container ,false );
        rw = rootView.findViewById(R.id.rw);

        rw.setHasFixedSize(true);
        rw.setLayoutManager(new LinearLayoutManager(getActivity()));
        GösterilecekMesajlar = new ArrayList<>();

        adapter = new bildirimadapter(getActivity() ,GösterilecekMesajlar);

        rw.setAdapter(adapter);
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;

        DatabaseReference ened = FirebaseDatabase.getInstance().getReference("istekler").child(kullaniciId);
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







        return rootView;


    }
}

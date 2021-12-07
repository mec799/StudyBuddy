package com.mec.studybuddy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class fragment_kesfet_insan extends Fragment implements android.widget.SearchView.OnQueryTextListener{
    private kesfetAdapter adapter ;
    private ArrayList<Kullanici_kayit> GösterilecekMesajlar ;
    private TextView insan_ara ;
    private RecyclerView rv_insan ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kesfet_insan , container ,false );


        rv_insan = rootView.findViewById(R.id.rv_insan);
        insan_ara = rootView.findViewById(R.id.insan_ara);
        rv_insan.setHasFixedSize(true);
        rv_insan.setLayoutManager(new LinearLayoutManager(getActivity()));
        GösterilecekMesajlar = new ArrayList<>();

        adapter = new kesfetAdapter(getActivity() ,GösterilecekMesajlar);

        rv_insan.setAdapter(adapter);
        insan_ara.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                arama(s.toString());

            }
        });
        DatabaseReference incereis = FirebaseDatabase.getInstance().getReference("Üyeler");
        incereis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GösterilecekMesajlar.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Kullanici_kayit uzer = child.getValue(Kullanici_kayit.class);

                    GösterilecekMesajlar.add(uzer);
                    adapter.notifyDataSetChanged();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return rootView;

    }
    public void arama (String aramaKelime){
        DatabaseReference incereis = FirebaseDatabase.getInstance().getReference("Üyeler");
        incereis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GösterilecekMesajlar.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Kullanici_kayit uzer = child.getValue(Kullanici_kayit.class);
                    if(uzer.getNickName().contains(aramaKelime)){
                        GösterilecekMesajlar.add(uzer);

                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        arama(newText);
        return false;
    }
}

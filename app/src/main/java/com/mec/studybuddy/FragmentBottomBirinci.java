package com.mec.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

public class FragmentBottomBirinci extends Fragment   {

    private RecyclerView rv_etut;
    private ArrayList<Etut_Olustur> etutList ;
    private BasitRvAdapterEtut adapter ;
    private DatabaseReference veriYolu ;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bottom_layout_birinci , container ,false );

        rv_etut =rootView.findViewById(R.id.rv);
        rv_etut.setHasFixedSize(true);
        rv_etut.setLayoutManager(new LinearLayoutManager(getActivity() ));



        etutList = new ArrayList<>();



        adapter = new BasitRvAdapterEtut(getActivity() , etutList , veriYolu);
        rv_etut.setAdapter(adapter);
        tumKisileriAl();



        return rootView;
    }


    public void tumKisileriAl (){
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etutList.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    Etut_Olustur etut = d.getValue(Etut_Olustur.class);
                    etutList.add(etut);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void arama (String aramaKelime){
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
        veriYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                etutList.clear();
                for(DataSnapshot d : snapshot.getChildren()){
                    Etut_Olustur etut = d.getValue(Etut_Olustur.class);
                    if(etut.getEtut_baslik().contains(aramaKelime)){
                        etutList.add(etut);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



}

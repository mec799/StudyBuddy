package com.mec.studybuddy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_kesfet_etut extends Fragment implements android.widget.SearchView.OnQueryTextListener{
    private RecyclerView rv_etut;
    private ArrayList<Etut_Olustur> etutList ;
    private BasitRvAdapterEtut adapter ;
    private DatabaseReference veriYolu ;
    private Toolbar toolbar ;
    private EditText etut_ara ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kesfet_etut , container ,false );

        rv_etut =rootView.findViewById(R.id.rv_etut);
        etut_ara =rootView.findViewById(R.id.etut_ara);
        rv_etut.setHasFixedSize(true);
        rv_etut.setLayoutManager(new LinearLayoutManager(getActivity() ));
        etutList = new ArrayList<>();



        adapter = new BasitRvAdapterEtut(getActivity() , etutList , veriYolu);
        rv_etut.setAdapter(adapter);
        etut_ara.addTextChangedListener(new TextWatcher() {
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
        tumKisileriAl();


        return rootView ;
    }

    private void tumKisileriAl() {
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

package com.mec.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class Fragment_Profil_Diger  extends Fragment {
    private adap adap ;
    private ArrayList<String> GösterilecekMesajlar ;
    private RecyclerView rvetut ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profil_diger_layout , container ,false );
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        rvetut = rootView.findViewById(R.id.rvetut);
        rvetut.setHasFixedSize(true);
        rvetut.setLayoutManager(new LinearLayoutManager(getActivity()));
        GösterilecekMesajlar = new ArrayList<>();

        adap = new adap(getActivity() ,GösterilecekMesajlar);

        rvetut.setAdapter(adap);

        DatabaseReference enes = FirebaseDatabase.getInstance().getReference("etutKayitlar").child(kullaniciId);
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






        return rootView;


    }
}

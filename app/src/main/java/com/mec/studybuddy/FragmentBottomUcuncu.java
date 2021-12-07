package com.mec.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentBottomUcuncu extends Fragment {
    private TabLayout tab_layout ;
    private ViewPager2 viewpager2 ;
    private ImageView profilFoto ;
    private Integer sum ;

    private TextView nickisim ,takipci , kahve ;
    private ArrayList<Fragment> fragmentListesi = new ArrayList<>();
    private ArrayList<String> fragmentBaslikListesi = new ArrayList<>();
    private ArrayList<Integer> kahveler = new ArrayList<Integer>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_bottom_layout_ucuncu , container ,false );
        tab_layout = rootView.findViewById(R.id.tab_layout);
        viewpager2 = rootView.findViewById(R.id.viewpager2);
        takipci = rootView.findViewById(R.id.takipci);

        profilFoto = rootView.findViewById(R.id.profilFoto);
        kahve = rootView.findViewById(R.id.kahve);
        nickisim =rootView.findViewById(R.id.nickisim);
        Button düzenle = rootView.findViewById(R.id.düzenle);
        fragmentListesi.add(new Fragment_Profil_Genel());
        fragmentListesi.add(new Fragment_Profil_Diger());
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity());
        viewpager2.setAdapter(adapter);
        fragmentBaslikListesi.add("Arkadaşlık İstekleri");
        fragmentBaslikListesi.add("Etüt Geçmişi");
        new TabLayoutMediator(tab_layout , viewpager2 , (tab , position )->tab.setText(fragmentBaslikListesi.get(position))).attach();
       profilFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getActivity(), PhotoFireBase.class));
           }
       });


        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        DatabaseReference merkez = FirebaseDatabase.getInstance().getReference("kahvecore").child(kullaniciId);
        merkez.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                kahveler.clear();
                for(DataSnapshot d : snapshot.getChildren()){

                    kahve_kayit cof = d.getValue(kahve_kayit.class);

                    kahveler.add(Integer.parseInt(cof.getKahve()));



                }

                if(kahveler.isEmpty()){
                    kahve.setText("0");


                }
                else {

                    int sum = 0;
                    for(Integer d : kahveler){
                        sum += d;


                    }

                    kahve.setText(sum+"");

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });











        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(getContext() == null ){

                    return;
                }
                Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                nickisim.setText(user.getNickName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference resimChange = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
        resimChange.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext() == null ){

                    return;
                }
                Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                Glide.with(getContext()).load(user.getBio()).into(profilFoto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference war = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(kullaniciId);
        war.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int size = (int) snapshot.getChildrenCount();

                    takipci.setText(size+" Arkadaş");

                }
                else {
                    takipci.setText("0 Arkadaş");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        takipci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sezen = new Intent(getActivity() , arkadaslar.class);
                startActivity(sezen);
            }
        });

        return rootView;
    }


    private class MyViewPagerAdapter extends FragmentStateAdapter {
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentListesi.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentListesi.size();
        }
    }
}

package com.mec.studybuddy;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import static java.security.AccessController.getContext;

public class FragmentBottomIkinci extends Fragment {

    private FloatingActionButton faba ;
    private RecyclerView r ;
    private ArrayList<Etut_Olustur>list ;
    private RvAdapter adap ;
    private Context bContext ;
    private NotificationCompat.Builder builder ;
    private SwipeRefreshLayout swipeRefreshLayout ;








    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_bottom_layout_ikinci , container ,false );
        r = rootView.findViewById(R.id.r);
        r.setHasFixedSize(true);
        r.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adap = new RvAdapter(getActivity() , list );
        r.setAdapter(adap);
        verileriYukle();




        faba = rootView.findViewById(R.id.faba);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                verileriYukle();
                Toast.makeText(getActivity(), "Yenileme Başarılı", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);

            }
        });




        faba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity() , AddEtut.class));

            }
        });
        return rootView;
    }

    private void verileriYukle() {
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        ArrayList<String> insanlar = new ArrayList<String>();


        DatabaseReference insan = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
        insan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getContext() == null ){
                    return;
                }
                Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                String ad = user.getNickName();
                DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
                veriYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot d : snapshot.getChildren()){
                            if(d.child("Katılan Kullanıcılar").hasChild(ad)){
                                Etut_Olustur etut = d.getValue(Etut_Olustur.class);
                                list.add(etut) ;
                            }
                        }
                        adap.notifyDataSetChanged();
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

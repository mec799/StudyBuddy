package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

public class Kesfet extends AppCompatActivity  {
    private RecyclerView rv_etut;
    private ArrayList<Etut_Olustur> etutList ;
    private BasitRvAdapterEtut adapter ;
    private DatabaseReference veriYolu ;
    private Toolbar toolbar ;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private TabLayout tabkesfet ;
    private ViewPager2 viewpagerkesfet ;
    private ArrayList<Fragment> izimler = new ArrayList<>();
    private ArrayList<String> asilizimler = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kesfet);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keşfet");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*toolbar.inflateMenu(R.menu.toolbar_menu);*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                Intent il = new Intent(Kesfet.this , etut_merkezi.class);
                startActivity(il);
            }
        });

        tabkesfet = findViewById(R.id.tabkesfet);
        viewpagerkesfet = findViewById(R.id.viewpagerkesfet);
        izimler.add(new fragment_kesfet_etut());
        izimler.add(new fragment_kesfet_insan());
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(this);
        viewpagerkesfet.setAdapter(adapter);
        asilizimler.add("Etütler");
        asilizimler.add("Kullanıcılar");
        new TabLayoutMediator(tabkesfet , viewpagerkesfet ,
                ((tab, position) -> tab.setText(asilizimler.get(position)))).attach();








    }
    private class MyViewPagerAdapter extends FragmentStateAdapter{
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return izimler.get(position);
        }

        @Override
        public int getItemCount() {
            return izimler.size();
        }
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem  = menu.findItem(R.id.action_Ara);


        return super.onCreateOptionsMenu(menu);
    }*/
}
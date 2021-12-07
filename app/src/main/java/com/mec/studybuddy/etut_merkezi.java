package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class etut_merkezi extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private Button out ;
    private FirebaseAuth auth ;
    private FirebaseUser currentUser ;
    private NavigationView navigation ;
    private DrawerLayout drawer ;
    private Toolbar toolbar4 ;
    private Fragment fragment ;
    private BottomNavigationView bottom_navigation ;

    private TextView kullanici_nick ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etut_merkezi);
        bottom_navigation = findViewById(R.id.bottom_navigation);

        navigation = findViewById(R.id.navigation);


        drawer = findViewById(R.id.drawer);
        toolbar4 =findViewById(R.id.toolbar4);

        setSupportActionBar(toolbar4);
        fragment =new fragment_ikinci();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_tutucu, new FragmentBottomIkinci()).commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawer , toolbar4,0 , 0 );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View baslik = navigation.inflateHeaderView(R.layout.navigation_baslik);
        TextView kullanici_nick = (TextView) baslik.findViewById(R.id.kullanici_nick);
        navigation.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        currentUser =auth.getCurrentUser();
        String kullaniciId = currentUser.getUid() ;
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getApplicationContext() == null ){

                    return;
                }
                Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                kullanici_nick.setText(user.getNickName());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.action_home){
                    fragment = new FragmentBottomIkinci();


                }
                if(item.getItemId() == R.id.action_kesfet){
                    Intent intent = new Intent(getApplicationContext(), Kesfet.class);
                    startActivity(intent);


                }
                if(item.getItemId() == R.id.action_profil){
                    fragment = new FragmentBottomUcuncu();


                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu, fragment).commit();
                return true;
            }
        });








    }



    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){

            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            Intent mi = new Intent(Intent.ACTION_MAIN);
            mi.addCategory(Intent.CATEGORY_HOME);
            mi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mi);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.alarm :
                startActivity(new Intent(etut_merkezi.this, Hatirlatici.class));
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_item_birinci){
            fragment =new fragment_birinci();


        }
        if(item.getItemId() == R.id.nav_item_ikinci){
            /*fragment =new fragment_ikinci();*/
            Intent af = new Intent(etut_merkezi.this , note.class);
            startActivity(af);



        }
        if(item.getItemId() == R.id.nav_item_ucuncu){
            fragment =new fragment_ucuncu();


        }
        if(item.getItemId() == R.id.cikis){
            auth.signOut();
            Toast.makeText(getApplicationContext(),"Başarılı şekilde çıkış yapıldı !",Toast.LENGTH_LONG).show();
            Intent mi = new Intent(etut_merkezi.this , MainActivity.class);
            startActivity(mi);
            finish();





        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_tutucu,fragment).commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
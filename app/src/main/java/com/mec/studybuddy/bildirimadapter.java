package com.mec.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class bildirimadapter extends RecyclerView.Adapter<bildirimadapter.CardViewTasarimNesneleriTutucu>{

    private Context mContext ;
    private List<takip>kullanicidisardangelenlist;

    public bildirimadapter(Context mContext, List<takip>kullanicidisardangelenlist) {
        this.mContext = mContext;
        this.kullanicidisardangelenlist = kullanicidisardangelenlist;
    }
    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public ImageView pepe ;
        public TextView nicik ;
        public Button accept ;
        public  CardView card_view ;

        public CardViewTasarimNesneleriTutucu(View view){
            super(view);
            pepe = view.findViewById(R.id.pepe);
            nicik = view.findViewById(R.id.nicik);
            accept = view.findViewById(R.id.accept);


            card_view = view.findViewById(R.id.card_viewP);


        }




    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext() ).inflate(R.layout.card_bildirim ,parent , false);;


        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        takip user = kullanicidisardangelenlist.get(position);

        DatabaseReference vfd = FirebaseDatabase.getInstance().getReference("Üyeler").child(user.getId());
        vfd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici_kayit yuser = snapshot.getValue(Kullanici_kayit.class);
                Glide.with(mContext).load(yuser.getBio()).into(holder.pepe);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi = new Intent(mContext, insan_profil.class);
                mi.putExtra("muz" , user.getId());
                v.getContext().startActivity(mi);

            }
        });
        holder.nicik.setText(user.getNick());


        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;



        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                kullaniciYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanici_kayit rop = snapshot.getValue(Kullanici_kayit.class);
                        DatabaseReference istek = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(kullaniciId);
                        istek.child(user.getNick()).setValue(user);

                        DatabaseReference oba = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(user.getId());
                        takip bizimki = new takip(rop.getId() , rop.getNickName());
                        oba.child(bizimki.getNick()).setValue(bizimki);

                        DatabaseReference ened = FirebaseDatabase.getInstance().getReference("istekler").child(kullaniciId);
                        ened.child(user.getNick()).removeValue();
                        Toast.makeText(mContext, user.getNick()+" ile arkadaşsınız!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





            }
        });



    }



    @Override
    public int getItemCount() {
        return kullanicidisardangelenlist.size();
    }



}
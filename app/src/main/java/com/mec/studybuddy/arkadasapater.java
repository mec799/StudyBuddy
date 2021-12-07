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

public class arkadasapater extends RecyclerView.Adapter<arkadasapater.CardViewTasarimNesneleriTutucu>{

    private Context context;
    private List<takip>kullanicidisardangelenlist;

    public arkadasapater(Context context, List<takip>kullanicidisardangelenlist) {
        this.context = context;
        this.kullanicidisardangelenlist = kullanicidisardangelenlist;
    }
    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public ImageView pepez ;
        public TextView nicika ;
        public Button birak ;
        public  CardView card_view ;

        public CardViewTasarimNesneleriTutucu(View view){
            super(view);
            pepez = view.findViewById(R.id.pepez);

            nicika = view.findViewById(R.id.nicika);
            birak = view.findViewById(R.id.birak);


            card_view = view.findViewById(R.id.card_viewD);


        }




    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext() ).inflate(R.layout.card_arkadas ,parent , false);
        context = parent.getContext();


        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        takip user = kullanicidisardangelenlist.get(position);

        holder.nicika.setText(user.getNick());

        String id = user.getId();
        DatabaseReference ened = FirebaseDatabase.getInstance().getReference("Üyeler").child(id);
        ened.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici_kayit yuj = snapshot.getValue(Kullanici_kayit.class);
                if (context == null) {
                    return;
                }
                Glide.with(context).load(yuj.getBio()).into(holder.pepez);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi = new Intent(context, insan_profil.class);
                mi.putExtra("muz" , user.getId());
                v.getContext().startActivity(mi);

            }
        });
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;


        holder.birak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                kullaniciYolu.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanici_kayit rop = snapshot.getValue(Kullanici_kayit.class);
                        DatabaseReference istek = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(kullaniciId);
                        istek.child(user.getNick()).removeValue();

                        DatabaseReference oba = FirebaseDatabase.getInstance().getReference("arkadaşlar").child(user.getId());
                        takip bizimki = new takip(rop.getId() , rop.getNickName());
                        oba.child(bizimki.getNick()).removeValue();
                        Toast.makeText(context, user.getNick()+" Arkadaşlıktan Çıkarıldı", Toast.LENGTH_SHORT).show();



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
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

public class kesfetAdapter extends RecyclerView.Adapter<kesfetAdapter.CardViewTasarimNesneleriTutucu>{

    private Context mContext ;
    private List<Kullanici_kayit>kullanicidisardangelenlist;

    public kesfetAdapter(Context mContext, List<Kullanici_kayit>kullanicidisardangelenlist) {
        this.mContext = mContext;
        this.kullanicidisardangelenlist = kullanicidisardangelenlist;
    }
    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public ImageView pepe ;
        public TextView nicik , durum ;
        public Button follow  ;
        public  CardView card_view ;

        public CardViewTasarimNesneleriTutucu(View view){
            super(view);
            pepe = view.findViewById(R.id.pepe);

            nicik = view.findViewById(R.id.nicik);



            card_view = view.findViewById(R.id.card_viewZ);


        }




    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext() ).inflate(R.layout.card_kesfet_insan ,parent , false);;


        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        Kullanici_kayit user = kullanicidisardangelenlist.get(position);

        Glide.with(mContext).load(user.getBio()).into(holder.pepe);
        holder.nicik.setText(user.getNickName());
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mi = new Intent(mContext, insan_profil.class);
                mi.putExtra("muz" , user.getId());
                v.getContext().startActivity(mi);

            }
        });
        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;



    }



    @Override
    public int getItemCount() {
        return kullanicidisardangelenlist.size();
    }



}
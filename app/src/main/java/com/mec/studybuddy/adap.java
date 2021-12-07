package com.mec.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class adap extends RecyclerView.Adapter<adap.CardViewTasarimNesneleriTutucu> {

    private Context mContext ;
    private List<String> MesajlarDisaridanGelenList;
    public adap(Context mContext, List<String> mesajlarDisaridanGelenList) {
        this.mContext = mContext;
        MesajlarDisaridanGelenList = mesajlarDisaridanGelenList;
    }
    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView kayitlar ;
        public CardView card_viewA ;

        public CardViewTasarimNesneleriTutucu(View view){
            super(view);
            kayitlar = view.findViewById(R.id.kayitlar);
            card_viewA = view.findViewById(R.id.card_viewA);


        }




    }


    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext() ).inflate(R.layout.adapetuta ,parent , false);


        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        String mesaj = MesajlarDisaridanGelenList.get(position);
        holder.kayitlar.setText(mesaj);






    }

    @Override
    public int getItemCount() {
        return MesajlarDisaridanGelenList.size();
    }
}

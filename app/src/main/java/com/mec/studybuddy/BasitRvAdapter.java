package com.mec.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasitRvAdapter extends RecyclerView.Adapter<BasitRvAdapter.CardViewTasarimNesneleriTutucu>{

    private Context mContext ;
    private List<String>MesajlarDisaridanGelenList;

    public BasitRvAdapter(Context mContext, List<String> mesajlarDisaridanGelenList) {
        this.mContext = mContext;
        MesajlarDisaridanGelenList = mesajlarDisaridanGelenList;
    }
    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView mesaj_txt ;
        public  CardView card_view ;

        public CardViewTasarimNesneleriTutucu(View view){
            super(view);
            mesaj_txt = view.findViewById(R.id.mesaj_txt);
            card_view = view.findViewById(R.id.card_viewB);


        }




    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext() ).inflate(R.layout.chat_sinif_card ,parent , false);;


        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        String mesaj = MesajlarDisaridanGelenList.get(position);




    }

    @Override
    public int getItemCount() {
        return MesajlarDisaridanGelenList.size();
    }



}
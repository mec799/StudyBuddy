package com.mec.studybuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class BasitRvAdapterEtut extends RecyclerView.Adapter<BasitRvAdapterEtut.CardViewNesneleriniTutucu>{

    private Context mcontext ;
    private List<Etut_Olustur>EtutDisaridanGelenVeri ;
    private DatabaseReference veriYolu ;
    private AlertDialog dialog ;


    public BasitRvAdapterEtut(Context mcontext, List<Etut_Olustur> etutDisaridanGelenVeri, DatabaseReference veriYolu) {
        this.mcontext = mcontext;
        EtutDisaridanGelenVeri = etutDisaridanGelenVeri;
        this.veriYolu = veriYolu;
    }

    public class CardViewNesneleriniTutucu extends RecyclerView.ViewHolder{

        public TextView baslik_etut , yapan_etut , tarih_etut , katilan_kullanici_sayisi , bilgi ;
        public CardView satir_card_view ;
        public Button katil_buton  ;
        public ImageView etut_pp , duzenle , alert_foto , var ;
        public CardViewNesneleriniTutucu(View view ){


            super(view );
            baslik_etut=view.findViewById(R.id.baslik_etut);
            tarih_etut=view.findViewById(R.id.tarih_etut);




            var=view.findViewById(R.id.var);


            yapan_etut=view.findViewById(R.id.yapan_etut);
            katil_buton =view.findViewById(R.id.katil_buton);
            alert_foto =view.findViewById(R.id.alert_foto);
            katilan_kullanici_sayisi =view.findViewById(R.id.katilan_kullanici_sayisi);
            duzenle=view.findViewById(R.id.duzenle);

            etut_pp=view.findViewById(R.id.etut_pp);

            satir_card_view=view.findViewById(R.id.satir_card_view);

        }
    }

    @NonNull
    @Override
    public CardViewNesneleriniTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_etut_tasarim , parent,false);
        return new CardViewNesneleriniTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewNesneleriniTutucu holder, int position) {



        Etut_Olustur etut = EtutDisaridanGelenVeri.get(position);
        holder.baslik_etut.setText(etut.getEtut_baslik());
        holder.tarih_etut.setText(etut.getEtut_tarih()+" & "+etut.getEtut_saat());

        holder.yapan_etut.setText(etut.getEtut_olusturan());
        Glide.with(mcontext).load(etut.getEtut_pp()).into(holder.etut_pp);
        holder.satir_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(mcontext);
                ad.setTitle(etut.getEtut_baslik()+" Etütünün Ayrıntıları :");
                ad.setMessage("Etüt:"+etut.getEtut_dakika()+" Dakika"+"\nTenefüs:"+etut.getTenefus_dakika()+" Dakika"+"\nTekrar:"+etut.getEtut_tekrar());
                ad.setNegativeButton("KAPAT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                ad.create().show();

            }
        });


        FirebaseAuth yetki = FirebaseAuth.getInstance();
        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
        String kullaniciId = firebaseKullanici.getUid() ;
        if(etut.getEtut_olusturan_id().equals(kullaniciId)){
            holder.katil_buton.setVisibility(View.INVISIBLE);
            holder.duzenle.setVisibility(View.VISIBLE);
            holder.var.setVisibility(View.INVISIBLE);



            holder.duzenle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mcontext , holder.duzenle);
                    popupMenu.getMenuInflater().inflate(R.menu.sil_menu , popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){


                                case R.id.sil :
                                    Snackbar.make(holder.duzenle , "Etütü Silmek İstiyor Musunuz ? ", Snackbar.LENGTH_SHORT)
                                            .setAction("SİL", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
                                                    veriYolu.child(etut.getEtut_key()).removeValue();


                                                }
                                            })
                                            .show();
                                    return true ;
                                default:
                                    return false ;
                            }
                        }
                    });
                    popupMenu.show();

                }
            });



        }
        else {

            holder.duzenle.setVisibility(View.INVISIBLE);




            DatabaseReference insan = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
            insan.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(getContext() == null ){
                        return;
                    }
                    Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                    String ad = user.getNickName();
                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key());
                    veriYolu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                                if(snapshot.child("Katılan Kullanıcılar").hasChild(ad)){
                                    holder.katil_buton.setVisibility(View.INVISIBLE);
                                    holder.var.setVisibility(View.VISIBLE);

                                }
                                else {
                                    holder.katil_buton.setVisibility(View.VISIBLE);
                                    holder.var.setVisibility(View.INVISIBLE);

                                    holder.katil_buton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                                            AlertDialog dialog = builder.create();
                                            View myView = LayoutInflater.from(mcontext).inflate(R.layout.katil_alert, null);
                                            dialog.setView(myView);
                                            ImageView close = myView.findViewById(R.id.alert_close_sifre);
                                            TextView text = myView.findViewById(R.id.alert_textview);
                                            Button button = myView.findViewById(R.id.alert_gonder);
                                            text.setText(etut.getEtut_baslik()+"' e Katılacaksın");
                                            button.setText("Katıl");


                                            dialog.show();



                                            close.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Toast.makeText(mcontext, "İptal Edildi !", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            button.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                   dialog.dismiss();

                                                    DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                                                    kullaniciYolu.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if(getContext() == null ){

                                                                return;
                                                            }
                                                            Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                                                            etut_katilan etut_katilan = new etut_katilan(user.getId() , user.getNickName());
                                                            DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key());
                                                            veriYolu.child("Katılan Kullanıcılar").child(user.getNickName()).setValue(etut_katilan);
                                                            holder.katil_buton.setVisibility(View.INVISIBLE);
                                                            holder.var.setVisibility(View.VISIBLE);





                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                                    Toast.makeText(mcontext, etut.getEtut_baslik()+"' e Başarı İle Katıldınız", Toast.LENGTH_SHORT).show();
                                                }
                                            });






                                        }
                                    });



                                }


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
        ArrayList<String> insanlar = new ArrayList<String>();
        DatabaseReference way = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key()).child("Katılan Kullanıcılar");
        way.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                insanlar.clear();

                for(DataSnapshot d : snapshot.getChildren()) {
                    String insan = d.getKey();

                    insanlar.add(insan);



                }

                holder.alert_foto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder sb = new StringBuilder();

                        for(String insan:insanlar)
                        {
                            sb.append(insan+"\n");
                        }
                        AlertDialog.Builder ad = new AlertDialog.Builder(mcontext);
                        ad.setTitle(etut.getEtut_baslik());
                        ad.setMessage(sb);
                        ad.setNegativeButton("KAPAT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                        ad.create().show();
                    }
                });






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        DatabaseReference sayiYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key()).child("Katılan Kullanıcılar");
        sayiYolu.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   Integer count = Math.toIntExact(snapshot.getChildrenCount());
                    holder.katilan_kullanici_sayisi.setText(Integer.toString(count));



                }
                else {
                    holder.katilan_kullanici_sayisi.setText("0");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }


    @Override
    public int getItemCount() {
        return EtutDisaridanGelenVeri.size();
    }



}

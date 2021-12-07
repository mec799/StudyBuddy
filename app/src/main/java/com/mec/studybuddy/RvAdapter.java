package com.mec.studybuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.security.AccessController.getContext;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.benimEtutlerimCardTutucu> {
    private Context aContext ;
    private List<Etut_Olustur>benimEtutlerim ;


    public RvAdapter(Context aContext, List<Etut_Olustur> benimEtutlerim) {
        this.aContext = aContext;
        this.benimEtutlerim = benimEtutlerim;
    }

    public class benimEtutlerimCardTutucu extends RecyclerView.ViewHolder {
        public TextView baslik_etut , yapan_etut , tarih_etut , katilan_kullanici_sayisi , bilgi2;
        public CardView satir_card_view ;
        public Button  ayril_buton , basla_buton ;
        public ImageView etut_pp , duzenle , alert_foto;
        public benimEtutlerimCardTutucu(View view ){

            super(view);
            baslik_etut=view.findViewById(R.id.baslik_etut2);
            basla_buton=view.findViewById(R.id.basla_buton);
            tarih_etut=view.findViewById(R.id.tarih_etut2);
            ayril_buton=view.findViewById(R.id.ayril_buton);


            yapan_etut=view.findViewById(R.id.yapan_etut2);

            alert_foto =view.findViewById(R.id.alert_foto2);
            katilan_kullanici_sayisi =view.findViewById(R.id.katilan_kullanici_sayisi2);
            duzenle=view.findViewById(R.id.duzenle2);

            etut_pp=view.findViewById(R.id.etut_pp2);

            satir_card_view=view.findViewById(R.id.satir_card_view);


        }




    }

    @NonNull
    @Override
    public benimEtutlerimCardTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_etut_tasarim2 , parent , false);

        return new benimEtutlerimCardTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull benimEtutlerimCardTutucu holder, int position) {

        Etut_Olustur etut = benimEtutlerim.get(position);
        holder.baslik_etut.setText(etut.getEtut_baslik());

        holder.tarih_etut.setText(etut.getEtut_tarih()+" & "+etut.getEtut_saat());
        holder.yapan_etut.setText(etut.getEtut_olusturan());
        Glide.with(aContext).load(etut.getEtut_pp()).into(holder.etut_pp);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(calendar.getTime());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(c.getTime());

        if(time.equals(etut.getEtut_saat()) && date.equals(etut.getEtut_tarih())){

            holder.basla_buton.setVisibility(View.VISIBLE);

            holder.basla_buton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mi = new Intent(aContext, sinif.class);
                    mi.putExtra("qey" , etut.getEtut_key());
                    mi.putExtra("etut_dakika" , etut.getEtut_dakika());
                    mi.putExtra("tenefus_dakika" , etut.getTenefus_dakika());
                    mi.putExtra("tekrar" , etut.getEtut_tekrar());


                    v.getContext().startActivity(mi);

                }
            });
        }
        else {
            holder.basla_buton.setVisibility(View.INVISIBLE);

        }

        DatabaseReference sayiYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key()).child("Katılan Kullanıcılar");
        sayiYolu.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Integer count = Math.toIntExact(snapshot.getChildrenCount());
                    holder.katilan_kullanici_sayisi.setText(Integer.toString(count));

                    FirebaseAuth yetki = FirebaseAuth.getInstance();
                    FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                    String kullaniciId = firebaseKullanici.getUid() ;

                    if (count <= 1 && etut.getEtut_olusturan_id().equals(kullaniciId) ) {

                        holder.duzenle.setVisibility(View.VISIBLE);

                    }
                    else {
                        holder.duzenle.setVisibility(View.INVISIBLE);



                    }



                }
                else {
                    holder.katilan_kullanici_sayisi.setText("0");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
















        holder.satir_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(aContext);
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

            holder.duzenle.setVisibility(View.VISIBLE);
            holder.ayril_buton.setVisibility(View.INVISIBLE);


            holder.duzenle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(aContext , holder.duzenle);
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
                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
                    veriYolu.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot d : snapshot.getChildren()){
                                if(d.child("Katılan Kullanıcılar").hasChild(ad)){
                                    holder.ayril_buton.setVisibility(View.VISIBLE);



                                    holder.ayril_buton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(aContext);
                                            AlertDialog dialog = builder.create();
                                            View myView = LayoutInflater.from(aContext).inflate(R.layout.ayril_alert, null);
                                            dialog.setView(myView);
                                            ImageView close = myView.findViewById(R.id.alert_close_sifre);
                                            TextView text = myView.findViewById(R.id.alert_textview);
                                            Button button = myView.findViewById(R.id.alert_gonder);
                                            text.setText(etut.getEtut_baslik()+"' den Ayrılacaksın");
                                            button.setText("AYRIL");


                                            dialog.show();



                                            close.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Toast.makeText(aContext, "İptal Edildi !", Toast.LENGTH_SHORT).show();
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
                                                            DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key());
                                                            veriYolu.child("Katılan Kullanıcılar").child(user.getNickName()).removeValue();

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                    Toast.makeText(aContext, etut.getEtut_baslik()+"' den Başarı İle Ayrıldınız", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                        }
                                    });

                                }
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
                        AlertDialog.Builder ad = new AlertDialog.Builder(aContext);
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














    }












    @Override
    public int getItemCount() {
        return benimEtutlerim.size();
    }

                                       //                                 _______
    //                           _,,ad8888888888bba,_
    //                        ,ad88888I888888888888888ba,
    //                      ,88888888I88888888888888888888a,
    //                    ,d888888888I8888888888888888888888b,
    //                   d88888PP"""" ""YY88888888888888888888b,
    //                 ,d88"'__,,--------,,,,.;ZZZY8888888888888,
    //                ,8IIl'"                ;;l"ZZZIII8888888888,
    //               ,I88l;'                  ;lZZZZZ888III8888888,
    //             ,II88Zl;.                  ;llZZZZZ888888I888888,
    //            ,II888Zl;.                .;;;;;lllZZZ888888I8888b
    //           ,II8888Z;;                 `;;;;;''llZZ8888888I8888,
    //           II88888Z;'                        .;lZZZ8888888I888b
    //           II88888Z; _,aaa,      .,aaaaa,__.l;llZZZ88888888I888
    //           II88888IZZZZZZZZZ,  .ZZZZZZZZZZZZZZ;llZZ88888888I888,
    //           II88888IZZ<'(@@>Z|  |ZZZ<'(@@>ZZZZ;;llZZ888888888I88I
    //          ,II88888;   `""" ;|  |ZZ; `"""     ;;llZ8888888888I888
    //          II888888l            `;;          .;llZZ8888888888I888,
    //         ,II888888Z;           ;;;        .;;llZZZ8888888888I888I
    //         III888888Zl;    ..,   `;;       ,;;lllZZZ88888888888I888
    //         II88888888Z;;...;(_    _)      ,;;;llZZZZ88888888888I888,
    //         II88888888Zl;;;;;' `--'Z;.   .,;;;;llZZZZ88888888888I888b
    //         ]I888888888Z;;;;'   ";llllll;..;;;lllZZZZ88888888888I8888,
    //         II888888888Zl.;;"Y88bd888P";;,..;lllZZZZZ88888888888I8888I
    //         II8888888888Zl;.; `"PPP";;;,..;lllZZZZZZZ88888888888I88888
    //         II888888888888Zl;;. `;;;l;;;;lllZZZZZZZZW88888888888I88888
    //         `II8888888888888Zl;.    ,;;lllZZZZZZZZWMZ88888888888I88888
    //          II8888888888888888ZbaalllZZZZZZZZZWWMZZZ8888888888I888888,
    //          `II88888888888888888b"WWZZZZZWWWMMZZZZZZI888888888I888888b
    //           `II88888888888888888;ZZMMMMMMZZZZZZZZllI888888888I8888888
    //            `II8888888888888888 `;lZZZZZZZZZZZlllll888888888I8888888,
    //             II8888888888888888, `;lllZZZZllllll;;.Y88888888I8888888b,
    //            ,II8888888888888888b   .;;lllllll;;;.;..88888888I88888888b,
    //            II888888888888888PZI;.  .`;;;.;;;..; ...88888888I8888888888,
    //            II888888888888PZ;;';;.   ;. .;.  .;. .. Y8888888I88888888888b,
    //           ,II888888888PZ;;'                        `8888888I8888888888888b,
    //           II888888888'                              888888I8888888888888888b
    //          ,II888888888                              ,888888I88888888888888888
    //         ,d88888888888                              d888888I8888888888ZZZZZZZ
    //      ,ad888888888888I                              8888888I8888ZZZZZZZZZZZZZ
    //    ,d888888888888888'                              888888IZZZZZZZZZZZZZZZZZZ
    //  ,d888888888888P'8P'                               Y888ZZZZZZZZZZZZZZZZZZZZZ
    // ,8888888888888,  "                                 ,ZZZZZZZZZZZZZZZZZZZZZZZZ
    //d888888888888888,                                ,ZZZZZZZZZZZZZZZZZZZZZZZZZZZ
    //888888888888888888a,      _                    ,ZZZZZZZZZZZZZZZZZZZZ888888888
    //888888888888888888888ba,_d'                  ,ZZZZZZZZZZZZZZZZZ88888888888888
    //8888888888888888888888888888bbbaaa,,,______,ZZZZZZZZZZZZZZZ888888888888888888
    //88888888888888888888888888888888888888888ZZZZZZZZZZZZZZZ888888888888888888888
    //8888888888888888888888888888888888888888ZZZZZZZZZZZZZZ88888888888888888888888
    //888888888888888888888888888888888888888ZZZZZZZZZZZZZZ888888888888888888888888
    //8888888888888888888888888888888888888ZZZZZZZZZZZZZZ88888888888888888888888888
    //88888888888888888888888888888888888ZZZZZZZZZZZZZZ8888888888888888888888888888
    //8888888888888888888888888888888888ZZZZZZZZZZZZZZ88888888888888888 Normand  88
    //88888888888888888888888888888888ZZZZZZZZZZZZZZ8888888888888888888 Veilleux 88
    //8888888888888888888888888888888ZZZZZZZZZZZZZZ88888888888888888888888888888888

}

package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Calendar;

import static java.security.AccessController.getContext;

public class AddEtut extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    private ImageView etut_pp ;
    private EditText etut_isim ;
    private EditText etut_saati , etut_dakika , tenefus_dakika , etut_tekrar  ,etut_time , etut_tarihi;
    private Button etut_add_button ;
    private FirebaseDatabase database ;
    private DatabaseReference myRef ;
    private Uri resimUri ;
    private String benimUrim ;
    private StorageTask yuklemeGorevi ;
    private Context cu ;
    private StorageReference resimYukleYolu ;
    private FirebaseStorage storage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etut);
        etut_pp = findViewById(R.id.etut_pp);

        etut_dakika = findViewById(R.id.etut_dakika);
        tenefus_dakika = findViewById(R.id.tenefus_dakika);
        etut_tarihi = findViewById(R.id.etut_tarihi);

        etut_tekrar = findViewById(R.id.etut_tekrar);
        etut_isim = findViewById(R.id.etut_isim);
        etut_time = findViewById(R.id.etut_time);

        etut_add_button = findViewById(R.id.etut_add_button);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Etütler");
        resimYukleYolu = FirebaseStorage.getInstance().getReference("etutresimler");
        etut_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");

            }
        });






        etut_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        etut_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etut_tekrar.toString().isEmpty() ||  etut_time.toString().isEmpty() ||

                        tenefus_dakika.toString().isEmpty() || etut_dakika.toString().isEmpty()
                        ||  etut_isim.toString().isEmpty()){
                    Toast.makeText(AddEtut.this, "Tüm Alanları Doldurduğunuzdan Emin Olun.", Toast.LENGTH_SHORT).show();


                }
                else {
                    String dakika = etut_dakika.getText().toString();
                    String tenefus = tenefus_dakika.getText().toString();
                    if(dakika.equals(tenefus)){
                        Toast.makeText(AddEtut.this, "Etüt ve Tenefüs Dakikaları Eşit Olamaz", Toast.LENGTH_SHORT).show();


                    }
                    else {
                        resimYukle();
                    }





                }





            }
        });

        CropImage.activity().setAspectRatio(1,1).start(AddEtut.this);





    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        etut_time.setText(String.format("%02d:%02d", hourOfDay, minute));
    }

    private String dosyaUzantisiAl (Uri uri ){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime   =  MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void resimYukle() {
            ProgressDialog progressDialog = new ProgressDialog(AddEtut.this);
        progressDialog.setMessage("Etüt Oluşturuluyor...");
        progressDialog.show();
        if(resimUri != null ){
            StorageReference dosyaYolu = resimYukleYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantisiAl(resimUri));
            yuklemeGorevi = dosyaYolu.putFile(resimUri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){

                        throw task.getException();
                    }
                    return dosyaYolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){



                        FirebaseAuth yetki = FirebaseAuth.getInstance();
                        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                        String kullaniciId = firebaseKullanici.getUid() ;
                        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                        kullaniciYolu.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(getContext() == null ){

                                    return;
                                }

                                Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                                Uri indirmeUrisi  = task.getResult();
                                String benimUrim = indirmeUrisi.toString();
                                String baslik = etut_isim.getText().toString();

                                String dakika = etut_dakika.getText().toString();
                                String tenefus = tenefus_dakika.getText().toString();
                                String etut_tarih = etut_tarihi.getText().toString();

                                String timee = etut_time.getText().toString();

                                String tekrar = etut_tekrar.getText().toString();

                                FirebaseAuth yetki = FirebaseAuth.getInstance();
                                FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                                String kullaniciId = firebaseKullanici.getUid() ;
                                DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
                                DatabaseReference pushedPostRef = veriYolu.push();
                                String postId = pushedPostRef.getKey();

                               if(etut_tekrar.toString().isEmpty() ||  etut_time.toString().isEmpty() ||

                                   tenefus_dakika.toString().isEmpty() || etut_dakika.toString().isEmpty()
                                       ||  etut_isim.toString().isEmpty()){



                               }
                               else {
                                   if(Integer.parseInt(dakika) > 120 || Integer.parseInt(dakika) < 25 ){

                                   }
                                   if(Integer.parseInt(tenefus) > 60 || Integer.parseInt(tenefus) < 1){



                                   }
                                   if(Integer.parseInt(tekrar) < 1 || Integer.parseInt(tekrar) > 60){



                                   }
                                   else {

                                       Etut_Olustur etut = new Etut_Olustur(baslik , benimUrim , timee, user.getNickName(),kullaniciId,postId ,dakika , tenefus , tekrar , etut_tarih );
                                       veriYolu.child(postId).setValue(etut);
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
                                               Intent mi = new Intent(AddEtut.this, etut_merkezi.class);
                                               finish();



                                               if (progressDialog != null && progressDialog.isShowing()) {
                                                   progressDialog.dismiss();
                                               }




                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

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
                    else
                    {

                        Toast.makeText(AddEtut.this, "Başarısız", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddEtut.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
        else {
            FirebaseAuth yetki = FirebaseAuth.getInstance();
            FirebaseUser firebaseKullanici = yetki.getCurrentUser();
            String kullaniciId = firebaseKullanici.getUid() ;
            DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
            kullaniciYolu.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(getContext() == null ){

                        return;
                    }

                    Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);


                    String baslik = etut_isim.getText().toString();

                    String dakika = etut_dakika.getText().toString();
                    String tenefus = tenefus_dakika.getText().toString();
                    String etut_tarih = etut_tarihi.getText().toString();

                    String timee = etut_time.getText().toString();

                    String tekrar = etut_tekrar.getText().toString();

                    FirebaseAuth yetki = FirebaseAuth.getInstance();
                    FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                    String kullaniciId = firebaseKullanici.getUid() ;
                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add");
                    DatabaseReference pushedPostRef = veriYolu.push();
                    String postId = pushedPostRef.getKey();

                    if(etut_tekrar.toString().isEmpty() ||  etut_time.toString().isEmpty() ||

                            tenefus_dakika.toString().isEmpty() || etut_dakika.toString().isEmpty()
                            ||  etut_isim.toString().isEmpty()){


                    }
                    else {
                        if(Integer.parseInt(dakika) > 120 || Integer.parseInt(dakika) < 25 ){

                        }
                        if(Integer.parseInt(tenefus) > 60 || Integer.parseInt(tenefus) < 1){



                        }
                        if(Integer.parseInt(tekrar) < 1 || Integer.parseInt(tekrar) > 60){



                        }
                        else {

                            Etut_Olustur etut = new Etut_Olustur(baslik , "https://firebasestorage.googleapis.com/v0/b/studybuddy-6e755.appspot.com/o/yildizligece.jpg?alt=media&token=f09d328b-ec15-4dd7-9618-00668c0feffb" ,  timee, user.getNickName(),kullaniciId,postId ,dakika , tenefus , tekrar , etut_tarih);
                            veriYolu.child(postId).setValue(etut);
                            DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                            kullaniciYolu.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(getContext() == null ){

                                        return;
                                    }
                                    Kullanici_kayit user = snapshot.getValue(Kullanici_kayit.class);
                                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("add").child(etut.getEtut_key());
                                    veriYolu.child("Katılan Kullanıcılar").child(user.getNickName()).setValue(user);
                                    Intent mi = new Intent(AddEtut.this, etut_merkezi.class);
                                    finish();



                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

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
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data );
            resimUri = result.getUri();
            etut_pp.setImageURI(resimUri);
        }
        else {
            Toast.makeText(this, "Resim Seçilemedi Etüt Profil Resmi Default Olarak Ayarlandı", Toast.LENGTH_SHORT).show();

        }
    }

}
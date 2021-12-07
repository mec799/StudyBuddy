package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class PhotoFireBase extends AppCompatActivity {
    private Uri resimUri ;
    private String benimUrim ;
    private StorageTask yuklemeGorevi ;
    private StorageReference resimYukleYolu ;
    private ImageView resim ;
    private Button PhotoSend ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_fire_base);
        resim = findViewById(R.id.resim);
        PhotoSend = findViewById(R.id.PhotoSend);
        resimYukleYolu = FirebaseStorage.getInstance().getReference("gonderiler");
        PhotoSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle();
                startActivity(new Intent(PhotoFireBase.this , etut_merkezi.class));
            }
        });
        CropImage.activity().setAspectRatio(1,1).start(PhotoFireBase.this);


    }
    private String dosyaUzantisiAl ( Uri uri ){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime   =  MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void resimYukle() {
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
                        Uri indirmeUrisi  = task.getResult();
                        benimUrim = indirmeUrisi.toString();
                        FirebaseAuth yetki = FirebaseAuth.getInstance();
                        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                        String kullaniciId = firebaseKullanici.getUid() ;
                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Üyeler").child(kullaniciId);
                        HashMap<String , Object > hashMap  = new HashMap<>();
                        hashMap.put("bio",benimUrim);
                        veriYolu.updateChildren(hashMap);


                    }
                    else
                    {
                        Toast.makeText(PhotoFireBase.this, "Başarısız", Toast.LENGTH_SHORT).show();

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PhotoFireBase.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
        else {
            Toast.makeText(this, "Seçilen resim yok ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode ==RESULT_OK ){
            CropImage.ActivityResult result = CropImage.getActivityResult(data );
            resimUri = result.getUri();
            resim.setImageURI(resimUri);
        }
        else {
            Toast.makeText(getApplicationContext(),"Resim Seçilemedi",Toast.LENGTH_LONG).show();
        }
    }


}
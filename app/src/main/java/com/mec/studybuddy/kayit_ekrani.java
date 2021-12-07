package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class kayit_ekrani extends AppCompatActivity {
    private EditText kullanici_adi_edtxt_kayit_ekrani , email_edtxt_kayit_ekrani , parola_edtxt_kayit_ekrani , parola_tekrar_edtxt_kayit_ekrani ;
    private Button kaydol_buton_kayit_ekrani ;

    private FirebaseAuth yetki ;
   private FirebaseDatabase database ;
    private DatabaseReference yol ;
    private void baglayici() {
        kullanici_adi_edtxt_kayit_ekrani = (EditText) findViewById(R.id.kullanici_adi_edtxt_kayit_ekrani);
        email_edtxt_kayit_ekrani = (EditText) findViewById(R.id.email_edtxt_kayit_ekrani);
        parola_edtxt_kayit_ekrani = (EditText) findViewById(R.id.parola_edtxt_kayit_ekrani);
        parola_tekrar_edtxt_kayit_ekrani = (EditText) findViewById(R.id.parola_tekrar_edtxt_kayit_ekrani);
        kaydol_buton_kayit_ekrani = (Button) findViewById(R.id.kaydol_buton_kayit_ekrani);
        yetki = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);
        baglayici();
        kaydol_buton_kayit_ekrani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewAccount();

            }
        });

    }

    public  void createNewAccount() {
        ProgressDialog progressDialog = new ProgressDialog(kayit_ekrani.this);
        progressDialog.setMessage("Kayıt Yapılıyor...");
        progressDialog.show();
        String userName = kullanici_adi_edtxt_kayit_ekrani.getText().toString();
        String email = email_edtxt_kayit_ekrani.getText().toString();
        String parola = parola_edtxt_kayit_ekrani.getText().toString();
        String parolaTekrar = parola_tekrar_edtxt_kayit_ekrani.getText().toString();



        if(TextUtils.isEmpty(userName)){
            Toast.makeText(getApplicationContext(),"Kullanıcı adı kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();


        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Email kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();


        }
        if(TextUtils.isEmpty(parola)){
            Toast.makeText(getApplicationContext(),"Parola kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();


        }
        if(TextUtils.isEmpty(parolaTekrar)){
            Toast.makeText(getApplicationContext(),"Parola tekrar kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();


        }

        else {
            if(parola.equals(parolaTekrar)){
                if(parola.length() < 8 || parolaTekrar.length() < 8){
                    Toast.makeText(kayit_ekrani.this,"Parola en az 8 karakterli olmalı !",Toast.LENGTH_LONG).show();


                }
                else {
                    yetki.createUserWithEmailAndPassword(email , parola)
                            .addOnCompleteListener(kayit_ekrani.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser firebaseKullanici = yetki.getCurrentUser();
                                        String kullaniciId = firebaseKullanici.getUid() ;
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference yol  = database.getReference("Üyeler").child(kullaniciId);

                                        HashMap<String , Object > hashMap = new HashMap<>();
                                        hashMap.put("id" , kullaniciId);
                                        hashMap.put("nickName" , userName);
                                        hashMap.put("bio" , "https://firebasestorage.googleapis.com/v0/b/studybuddy-6e755.appspot.com/o/placeHolder.jpg?alt=media&token=44a6f00a-8321-4914-9c51-706802106215");
                                        yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    startActivity(new Intent( kayit_ekrani.this , etut_merkezi.class));
                                                    finish();
                                                    if (progressDialog != null && progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                    }

                                                }

                                            }
                                        });






                                    }







                                }
                            });


                }




            }
            else {
                Toast.makeText(kayit_ekrani.this,"Parolarınız eşleşmiyor!",Toast.LENGTH_LONG).show();

            }


        }
    }


}
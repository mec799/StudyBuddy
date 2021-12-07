package com.mec.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email_edtxt_main , parola_edtxt_main ;
    private Button  giris_buton_main , kaydol_buton_main ;
    private TextView sifremiunuttum_txt_main ;
    private FirebaseAuth auth ;


    public void baglayici(){

        email_edtxt_main = (EditText) findViewById(R.id.email_edtxt_main);
        parola_edtxt_main = (EditText) findViewById(R.id.parola_edtxt_main);
        giris_buton_main = (Button) findViewById(R.id.giris_buton_main);
        sifremiunuttum_txt_main = (TextView) findViewById(R.id.sifremiunuttum_txt_main);
        kaydol_buton_main = (Button) findViewById(R.id.kaydol_buton_main);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baglayici();
        sifremiunuttum_txt_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                AlertDialog dialog = builder.create();
                View myView = LayoutInflater.from(MainActivity.this).inflate(R.layout.sifre_alert, null);
                dialog.setView(myView);
                ImageView close = myView.findViewById(R.id.alert_close_sifre);
                TextView text = myView.findViewById(R.id.alert_textview);
                Button button = myView.findViewById(R.id.alert_gonder);
                EditText ed = myView.findViewById(R.id.email_sifre);
                text.setText("E-mail Adresinizi Giriniz");
                button.setText("GÖDNER");


                dialog.show();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "İptal Edildi !", Toast.LENGTH_SHORT).show();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress = ed.getText().toString();

                        auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Şifre Sıfırlama İsteği Gönderildi !", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, "Bir Hata Oluştu!", Toast.LENGTH_SHORT).show();


                            }
                        });
                    }
                });

            }
        });


        kaydol_buton_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(MainActivity.this, kayit_ekrani.class);
                startActivity(ıntent);
                finish();


            }
        });
        giris_buton_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();


            }
        });


    }

    private void loginUser() {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.show();
        String email = email_edtxt_main.getText().toString();
        String parola = parola_edtxt_main.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(),"Email  kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(parola)){
            Toast.makeText(getApplicationContext(),"Parola kısmı boş bırakılamaz !",Toast.LENGTH_LONG).show();
        }
        else {
            auth.signInWithEmailAndPassword(email , parola).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "Giriş Başarılı !", Toast.LENGTH_LONG).show();
                        Intent mi = new Intent(MainActivity.this, etut_merkezi.class);

                        startActivity(mi);
                        finish();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                    }
                    else {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(),"Bir hata oluştu !",Toast.LENGTH_LONG).show();


                    }
                }
            });


        }


    }
}
package com.studio.parseviewpaises;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class PhotoActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button button;
    private TextView textView;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        imageView = (ImageView) findViewById(R.id.imgImagem);
        button = (Button) findViewById(R.id.btnTirarFoto);
        textView = (TextView) findViewById(R.id.txtMensagem);

        sharedPreferences = getSharedPreferences("PHOTO", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(getIntent().getExtras() != null){
            nome = getIntent().getExtras().getString("Nome");
            textView.setText("A foto foi tirada " + sharedPreferences.getInt(nome,0) + " vezes");

            if( getIntent().getExtras().getBoolean("Camera")){
                AtivarCamera();
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AtivarCamera();
            }
        });
    }

    public void AtivarCamera(){

        Intent Intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent, CAMERA);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case CAMERA:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);

                    int cont = sharedPreferences.getInt(nome,0);

                    editor.putInt(nome,++cont);
                    editor.apply();

                    textView.setText("A foto foi tirada " + sharedPreferences.getInt(nome,0) + " vezes");
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(PhotoActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
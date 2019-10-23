package com.ratatouille;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImagenActivity extends AppCompatActivity {

    final int REQUEST_IMAGE_CAPTURE=1;
    final int IMAGE_PICKER_REQUEST=100;

    Button select;
    Button useCamera;
    ImageView imgShown;
    Button next;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        bundle=getIntent().getBundleExtra("bundle");
        imgShown=findViewById(R.id.imageSelected);
        useCamera=findViewById(R.id.buttonTakePhoto);
        next=findViewById(R.id.buttonSiguienteImagen);
        select=findViewById(R.id.buttonSelectImage);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission((Activity)v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE,"Para ver galería",IMAGE_PICKER_REQUEST);
                if(ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(v.getContext(),"Negado, no se puede hacer nada",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent pickImage=new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/");
                    startActivityForResult(pickImage,IMAGE_PICKER_REQUEST);
                }
            }
        });
        useCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission((Activity)v.getContext(), Manifest.permission.CAMERA,"Para ver galería",REQUEST_IMAGE_CAPTURE);
                if(ContextCompat.checkSelfPermission(v.getContext(),Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(v.getContext(),"Negado, no se puede hacer nada",Toast.LENGTH_SHORT).show();
                }
                else{
                    takePicture();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ImagenActivity.this,SetLocationDataActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case IMAGE_PICKER_REQUEST:
                if(resultCode==RESULT_OK){
                    try{
                        final Uri imageUri=data.getData();
                        final InputStream imageStream=getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage= BitmapFactory.decodeStream(imageStream);
                        imgShown.setImageBitmap(selectedImage);
                    }
                    catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode==RESULT_OK){
                    Bundle extras=data.getExtras();
                    Bitmap imageBitmap=(Bitmap)extras.get("data");
                    imgShown.setImageBitmap(imageBitmap);
                }
        }
    }


    private void requestPermission(Activity context, String permiso, String justificacion, int idCode){
        if(ContextCompat.checkSelfPermission(context, permiso)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)){
                Toast.makeText(context,justificacion,Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(context,new String[]{permiso},idCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){
        switch(requestCode){
            case IMAGE_PICKER_REQUEST:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Intent pickImage=new Intent(Intent.ACTION_PICK);
                    pickImage.setType("image/");
                    startActivityForResult(pickImage,IMAGE_PICKER_REQUEST);
                }else{
                    Toast.makeText(this,"Funcionalidad limitada",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case REQUEST_IMAGE_CAPTURE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    takePicture();
                }else{
                    Toast.makeText(this,"Funcionalidad limitada",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void takePicture(){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }


}

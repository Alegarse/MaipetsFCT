package com.example.maipetsfct.adapters;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.maipetsfct.models.mascota;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class GetDownUrl extends mascota {

    private String ruta;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    public GetDownUrl() {

        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance() ;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ruta = "";

        if (mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg") != null) {

            StorageReference profileImgPath = mStorageRef.child("images").child(mAuth.getCurrentUser().getUid()).child("ProfileImg.jpg");
            profileImgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri downloadUrl = uri;
                    ruta = downloadUrl.toString();
                }
            });
        }
    }
}

package com.example.inigo.aplicaciomusicainigoreal;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageButton fotoGalleryButton;
    private Button uploadButton;
    private Button listButton;
    private EditText nomText;
    private EditText autorText;
    private ImageView imageView;

    private Uri imageUri;

    private int PICK_IMAGE_REQUEST = 1;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageReference = FirebaseStorage.getInstance().getReference("elements");
        databaseReference = FirebaseDatabase.getInstance().getReference("elements");

        fotoGalleryButton = findViewById(R.id.galleryMain);
        uploadButton = findViewById(R.id.uploadMain);
        listButton = findViewById(R.id.listaMain);
        nomText = findViewById(R.id.nameMain);
        autorText = findViewById(R.id.autorMain);
        imageView = findViewById(R.id.fotoMain);
        fotoGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pujarElement();
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
    }
    private void pujarElement() {
        if(imageUri!=null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            fileReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String uploadId = databaseReference.push().getKey();
                        Random rand = new Random();
                        String song = getUrlSong(rand.nextInt((5 - 1) + 1) + 1);
                        UploadElement uploadElement = new UploadElement(uploadId,song,downloadUri.toString(), autorText.getText().toString(), nomText.getText().toString());
                        databaseReference.child(uploadId).setValue(uploadElement);

                    }
                }
            });
            Toast.makeText(this,"Correcte",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void openActivity(){
        Intent intent = new Intent(this,LlistaActivity.class);
        startActivity(intent);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    public String getUrlSong(int name){
        switch (name){
            case 1:
                return "https://firebasestorage.googleapis.com/v0/b/aplicaciomusicainigoreal.appspot.com/o/elements%2FGuns%20N'%20Roses%20-%20Live%20And%20Let%20Die.mp3?alt=media&token=d12c4a5c-2f60-4f2c-b3f0-da8c768c0d87";
            case 2:
                return "https://firebasestorage.googleapis.com/v0/b/aplicaciomusicainigoreal.appspot.com/o/elements%2FGuns%20N'%20Roses%20-%20Paradise%20City.mp3?alt=media&token=5bbc3e8d-f3fe-4028-a1c5-3f26458ef5fd";
            case 3:
                return "https://firebasestorage.googleapis.com/v0/b/aplicaciomusicainigoreal.appspot.com/o/elements%2FGuns%20N'%20Roses%20-%20Patience.mp3?alt=media&token=67f69be2-1564-41c9-bb6e-ab0a0d8f1ec7";
            case 4:
                return "https://firebasestorage.googleapis.com/v0/b/aplicaciomusicainigoreal.appspot.com/o/elements%2FGuns%20N'%20Roses%20-%20Sweet%20Child%20O'%20Mine.mp3?alt=media&token=a4193dcf-77ed-49cb-a2ac-0d6a5da66fff";
            default:
                return "https://firebasestorage.googleapis.com/v0/b/aplicaciomusicainigoreal.appspot.com/o/elements%2FGuns%20N'%20Roses%20-%20Welcome%20To%20The%20Jungle.mp3?alt=media&token=e5f23f38-ed88-411b-91c0-8296ac4e32aa";
        }
    }

    private void openFileChooser(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
}

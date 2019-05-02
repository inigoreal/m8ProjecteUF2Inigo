package com.example.inigo.aplicaciomusicainigoreal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class LlistaActivity extends AppCompatActivity {
    private RecyclerView recycledView;
    private LlistaAdapter adapter;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private List<UploadElement> elements;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista);

        adapter = new LlistaAdapter();

        recycledView = findViewById(R.id.recycledViewId);
        recycledView.setLayoutManager(new LinearLayoutManager(this));

        elements = new ArrayList<>();
        recycledView.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("elements").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {


                UploadElement uploadElement = dataSnapshot.getValue(UploadElement.class);
                elements.add(uploadElement);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        firebaseStorage = FirebaseStorage.getInstance();
    }

    public class LlistaAdapter extends RecyclerView.Adapter<LlistaAdapter.LlistaViewHolder> {

        @Override
        public LlistaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view =  getLayoutInflater().inflate(R.layout.item_list,viewGroup,false);

            return new LlistaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final LlistaViewHolder llistaViewHolder, int i) {
            final UploadElement uploadElement = elements.get(i);
            llistaViewHolder.nameImage.setText(uploadElement.getName());
            llistaViewHolder.autorImage.setText(uploadElement.getAutor());

            firebaseStorage.getReferenceFromUrl(uploadElement.getImageUrl())
                    .getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    llistaViewHolder.fotoImage.setImageBitmap(bitmap);
                }
            });
        }


        @Override
        public int getItemCount() {
            return elements.size();
        }

        public class LlistaViewHolder extends RecyclerView.ViewHolder{
            public TextView nameImage,autorImage;
            public ImageView fotoImage;

            public LlistaViewHolder(View itemView) {
                super(itemView);
                nameImage = itemView.findViewById(R.id.cancion_list);
                autorImage = itemView.findViewById(R.id.autor_list);
                fotoImage = itemView.findViewById(R.id.imageView_list);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),SongActivity.class);
                        intent.putExtra("nameSong",elements.get(getAdapterPosition()).getName());
                        intent.putExtra("autorSong",elements.get(getAdapterPosition()).getAutor());
                        intent.putExtra("imageUrl",elements.get(getAdapterPosition()).getImageUrl());
                        intent.putExtra("songUrl",elements.get(getAdapterPosition()).getSongUrl());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}

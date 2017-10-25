package com.scolari.scolari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.scolari.scolari.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String ARTIST_NODE = "Artist"; //esta variable es el nombre de mi tables
    private static final String TAG = "MainActivity"; // creo que esta es la variable donde se muestra
    private DatabaseReference databaseReference;
    private List<String> artistNames;   // esta variable es para asignar datos

    private ListView lstArtist;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        lstArtist = (ListView) findViewById(R.id.lstArtist);
        artistNames = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artistNames); // el primer parametro que me pide es donde estoy hubicado
        lstArtist.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);// Ejecuto la persistencia de firbase cuando esta sin internet
        databaseReference = FirebaseDatabase.getInstance().getReference(); // esta liniea es para obtener el nodo principal padre


        databaseReference.child(ARTIST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artistNames.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Artist artist = snapshot.getValue(Artist.class);
                        Log.w(TAG, "Artist name: " + artist.getName());
                        artistNames.add(artist.getName());
                        artistNames.add(artist.getGenre());
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void createArtist(View view){ // ESTE METODO RESPONDERA CUNADO LE DEN CLICK
        Artist artist = new Artist(databaseReference.push().getKey(), "Maluma baby", "Reggeton"); // PRIMER DATO SOLICITADO ES EL IDENTIFICADOR
        //.push().getKey() FUNCION DE FIRE PARA CREAR UN ID, HACE COMO QUE INSERTA ALGO ES EL IDENTIFICADOR
        databaseReference.child(ARTIST_NODE).child(artist.getId()).setValue(artist);// AQUI APUNTAMOS AL ARBOL DE LA BASE DE DATOS
    }
}

package com.mygdx.fuegopeligro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/*
* Final
*/

public class FireLister extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage imageDB;

    private TextView senderInfo;
    private TextView fireType;
    private TextView fireLoc;
    private ImageView fireImage;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_lister);
        startDB();

        senderInfo = findViewById(R.id.senderNumber);
        fireType = findViewById(R.id.fireType);
        fireLoc = findViewById(R.id.fireLocation);
        fireImage = findViewById(R.id.fireImages);
        Button back = findViewById(R.id.button8);
        final Button next = findViewById(R.id.button9);
        Button prev = findViewById(R.id.button10);

        final ArrayList<String> names = new ArrayList<String>();
        final ArrayList<String> numbers = new ArrayList<>();
        final ArrayList<String> fireTypes = new ArrayList<String>();
        final ArrayList<String> fireIDS = new ArrayList<>();
        final ArrayList<String> imgURLS = new ArrayList<String>();
        final ArrayList<Float> lat = new ArrayList<Float>();
        final ArrayList<Float> lon = new ArrayList<Float>();

        final Calendar currentTime = Calendar.getInstance();
        Date dates = currentTime.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "dd-MM-yyyy", Locale.getDefault());
        int hours = currentTime.get(Calendar.HOUR_OF_DAY);
        int minutes = currentTime.get(Calendar.MINUTE);
        int seconds = currentTime.get(Calendar.SECOND);
        String date = simpleDateFormat.format(dates);

        db.collection("FIRES")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                names.add(documentSnapshot.getString("senderName"));
                                numbers.add(documentSnapshot.getString("senderNumber"));
                                fireTypes.add(documentSnapshot.getString("fireType"));
                                fireIDS.add(documentSnapshot.getString("fire_id"));
                                imgURLS.add(documentSnapshot.getString("imageLink"));
                                lat.add(Float.parseFloat(
                                        Objects.requireNonNull(
                                                documentSnapshot.get("lattitude")).toString()));
                                lon.add(Float.parseFloat(
                                        Objects.requireNonNull(
                                                documentSnapshot.get("longitude")).toString()));
                            }
                        } else {
                            Toast.makeText(FireLister.this,
                                    "Check your internet connection, cannot find database",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxLimit = fireIDS.size() - 1;
                if (counter <= maxLimit) {
                    try {
                        senderInfo.setText(String.format("%s - %s", names.get(counter), numbers.get(counter)));
                    } catch (NullPointerException ex) {
                        Toast.makeText(FireLister.this,
                                "Fire info in the database has some errors, please contact developers.",
                                Toast.LENGTH_SHORT).show();
                    }

                    fireType.setText(fireTypes.get(counter));
                    fireLoc.setText(String.format("%s, %s", lat.get(counter), lon.get(counter)));

                    if (imgURLS.get(counter) != null) {
                        StorageReference imageRef = imageDB.getReferenceFromUrl(imgURLS.get(counter));
                        GlideApp.with(getApplicationContext())
                                .load(imageRef)
                                .into(fireImage);
                    }

                    counter = counter + 1;
                } else {
                    Toast.makeText(FireLister.this,
                            "You have reached the end of the list",
                            Toast.LENGTH_SHORT).show();
                    counter = maxLimit;
                }
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter >= 0) {
                    try {
                        senderInfo.setText(String.format("%s - %s", names.get(counter), numbers.get(counter)));
                    } catch (NullPointerException ex) {
                        Toast.makeText(FireLister.this,
                                "Fire info in the database has some errors, please contact developers.",
                                Toast.LENGTH_SHORT).show();
                    }

                    fireType.setText(fireTypes.get(counter));
                    fireLoc.setText(String.format("%s, %s", lat.get(counter), lon.get(counter)));

                    if (imgURLS.get(counter) != null) {
                        StorageReference imageRef = imageDB.getReferenceFromUrl(imgURLS.get(counter));
                        GlideApp.with(getApplicationContext())
                                .load(imageRef)
                                .into(fireImage);
                    }

                    counter = counter - 1;
                } else {
                    Toast.makeText(FireLister.this,
                            "You have reached the end of the list",
                            Toast.LENGTH_SHORT).show();
                    counter = 0;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            }
        });
    }

    private void startDB() {
        db = FirebaseFirestore.getInstance();
        imageDB = FirebaseStorage.getInstance();
    }
}

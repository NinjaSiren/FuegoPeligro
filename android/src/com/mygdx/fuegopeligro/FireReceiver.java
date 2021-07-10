package com.mygdx.fuegopeligro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class FireReceiver extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseStorage imageDB;

    private TextView senderInfo;
    private TextView fireType;
    private TextView fireLoc;
    private ImageView fireImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_receiver);
        startDB();

        senderInfo = findViewById(R.id.senderNumber);
        fireType = findViewById(R.id.fireType);
        fireLoc = findViewById(R.id.fireLocation);
        fireImage = findViewById(R.id.fireImages);
        Button back = findViewById(R.id.button8);
        Button refresh = findViewById(R.id.button9);
        Button call = findViewById(R.id.button10);

        refresh.setOnClickListener(new View.OnClickListener() {
            Calendar currentTime = Calendar.getInstance();
            Date dates = currentTime.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy", Locale.getDefault());
            int hours = currentTime.get(Calendar.HOUR_OF_DAY);
            int minutes = currentTime.get(Calendar.MINUTE);
            int seconds = currentTime.get(Calendar.SECOND);
            String date = simpleDateFormat.format(dates);

            @Override
            public void onClick(View v) {
                db.collection("FIRES")
                        .whereEqualTo("date", date)
                        .whereEqualTo("hour", hours)
                        .whereEqualTo("minute", minutes)
                        .whereLessThanOrEqualTo("second", seconds).limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                        String name = documentSnapshot.getString("senderName");
                                        String number = documentSnapshot.getString("senderNumber");
                                        String fireTypes = documentSnapshot.getString("fireType");
                                        String fireID = documentSnapshot.getString("fire_id");
                                        String imageURL = documentSnapshot.getString("imageLink");
                                        float latittude = Float.parseFloat(
                                                Objects.requireNonNull(
                                                        documentSnapshot.get("lattitude")).toString());
                                        float longitude = Float.parseFloat(
                                                Objects.requireNonNull(
                                                        documentSnapshot.get("longitude")).toString());

                                        try {
                                            senderInfo.setText(String.format("%s - %s", name, number));
                                        } catch (NullPointerException ex) {
                                            Toast.makeText(FireReceiver.this,
                                                    "Fire info in the database has some errors, please contact developers.",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                        fireType.setText(fireTypes);
                                        fireLoc.setText(String.format("%s, %s", latittude, longitude));

                                        if (imageURL != null) {
                                            StorageReference imageRef = imageDB.getReferenceFromUrl(imageURL);
                                            GlideApp.with(getApplicationContext())
                                                    .load(imageRef)
                                                    .into(fireImage);
                                        }
                                    }
                                } else {
                                    Toast.makeText(FireReceiver.this,
                                            "Check your internet connection, cannot find database",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            Calendar currentTime = Calendar.getInstance();
            Date dates = currentTime.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd-MM-yyyy", Locale.getDefault());
            int hours = currentTime.get(Calendar.HOUR_OF_DAY);
            int minutes = currentTime.get(Calendar.MINUTE);
            int seconds = currentTime.get(Calendar.SECOND);
            String date = simpleDateFormat.format(dates);

            @Override
            public void onClick(View v) {
                db.collection("FIRES")
                        .whereEqualTo("date", date)
                        .whereEqualTo("hour", hours)
                        .whereEqualTo("minute", minutes)
                        .whereLessThanOrEqualTo("second", seconds).limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:" +
                                                Integer.parseInt(
                                                Objects.requireNonNull(
                                                        documentSnapshot.get("number")).toString())));

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                // TODO: Consider calling
                                                //    Activity#requestPermissions
                                                // here to request the missing permissions, and then overriding
                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                //                                          int[] grantResults)
                                                // to handle the case where the user grants the permission. See the documentation
                                                // for Activity#requestPermissions for more details.
                                                return;
                                            }
                                        }
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(FireReceiver.this,
                                            "Nothing is inside the database, check your internet.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

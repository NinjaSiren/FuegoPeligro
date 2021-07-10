package com.mygdx.fuegopeligro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startDB();

        Button login = findViewById(R.id.button5);
        Button back = findViewById(R.id.button7);
        final EditText user = findViewById(R.id.editText);
        final EditText pass = findViewById(R.id.editText2);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText() == null) {
                    Toast.makeText(LoginActivity.this, "Username should not be blank."
                            , Toast.LENGTH_LONG).show();
                    if (pass.getText() == null) {
                        Toast.makeText(LoginActivity.this, "Password should not be blank."
                                , Toast.LENGTH_LONG).show();
                    }
                } else if(pass.getText() == null) {
                        Toast.makeText(LoginActivity.this, "Password should not be blank."
                                , Toast.LENGTH_LONG).show();
                    if(user.getText() == null) {
                        Toast.makeText(LoginActivity.this, "Username should not be blank."
                                , Toast.LENGTH_LONG).show();
                    }
                } else {
                    signIn(user.getText().toString(), pass.getText().toString());
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

    private void signIn(String user, String pass) {
        db.collection("ACCOUNTS")
                .whereEqualTo("username", user)
                .whereEqualTo("password", pass)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String acctType = documentSnapshot.getString("acct_type");
                                String name = documentSnapshot.getString("name");
                                String number = documentSnapshot.getString("number");
                                Toast.makeText(LoginActivity.this, "Welcome back, "
                                                + name, Toast.LENGTH_LONG).show();

                                Intent intent;
                                if(Objects.equals(acctType, "DEV")) {
                                    intent = new Intent(getApplicationContext(), SelectFire.class);
                                    startActivity(intent);
                                } else if(Objects.equals(acctType, "OFF")) {
                                    intent = new Intent(getApplicationContext(), FireLister.class);
                                    startActivity(intent);
                                } else if(Objects.equals(acctType, "OPE")) {
                                    intent = new Intent(getApplicationContext(), FireReceiver.class);
                                    startActivity(intent);
                                } else if(Objects.equals(acctType, "SEN")) {
                                    intent = new Intent(getBaseContext(), FireSender.class);
                                    intent.putExtra("senderName", name);
                                    intent.putExtra("senderNumber", number);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "Contact developer for fixing your account type.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Error getting documents: "
                                            + task.getException()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void startDB() {
        db = FirebaseFirestore.getInstance();
    }
}

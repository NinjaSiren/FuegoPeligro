package com.mygdx.fuegopeligro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

                    setContentView(R.layout.activity_menu);
                    Button buttonGame = findViewById(R.id.button3);
                    Button buttonApp = findViewById(R.id.button4);
                    Button buttonExit = findViewById(R.id.buttonExit);
                    Button buttonFire = findViewById(R.id.button6);

                    buttonGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), AndroidLauncher.class));
                        }
                    });

                    buttonApp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    });

                    buttonFire.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });

                    buttonExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finishAndRemoveTask();
                            finishAffinity();
                            finish();
                            System.exit(0);
                        }
                    });
    }
}

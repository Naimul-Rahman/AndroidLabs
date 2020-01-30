package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_lab3);

        Button button = findViewById(R.id.button);

        EditText editText = findViewById(R.id.emailAddress);

        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        editText.setText(prefs.getString("EMAIL", ""));



        button.setOnClickListener((v) -> {Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
            goToProfile.putExtra("EMAIL", editText.getText().toString());
            startActivity(goToProfile);});

    }

    @Override
    protected void onPause() {

        super.onPause();
        EditText editText = findViewById(R.id.emailAddress);
        prefs = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit =prefs.edit();
        edit.putString("EMAIL", editText.getText().toString());
        edit.commit();
    }


}

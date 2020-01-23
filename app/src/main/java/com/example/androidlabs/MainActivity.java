package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        //setContentView(R.layout.activity_main_relative);
        //setContentView(R.layout.activity_main_grid);
        CheckBox checkbox = findViewById(R.id.checkbox);
        Button button = findViewById(R.id.button);
        Switch onOffSwitch = findViewById(R.id.onOff);
        /*checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Here is more information", Toast.LENGTH_SHORT).show();
            }
        });*/
        
        button.setOnClickListener((v) -> {Toast.makeText(v.getContext(), getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();});
        //onOffSwitch.setChecked(false);
        onOffSwitch.setOnCheckedChangeListener((cb, isChecked) -> {
            if (isChecked) {
                Snackbar.make(onOffSwitch, getResources().getString(R.string.snackbar_message) + " on", Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), click -> onOffSwitch.setChecked(!isChecked)).show();
            } else {
                Snackbar.make(onOffSwitch, getResources().getString(R.string.snackbar_message) + " off", Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), click -> onOffSwitch.setChecked(!isChecked)).show();
            }
        });
        checkbox.setOnCheckedChangeListener((cb, isChecked) -> {
            if (isChecked) {
                Snackbar.make(checkbox, getResources().getString(R.string.snackbar_message) + " on", Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), click -> checkbox.setChecked(!isChecked)).show();
            } else {
                Snackbar.make(checkbox, getResources().getString(R.string.snackbar_message) + " off", Snackbar.LENGTH_LONG).setAction(getResources().getString(R.string.undo), click -> checkbox.setChecked(!isChecked)).show();
            }
        });


    }
}

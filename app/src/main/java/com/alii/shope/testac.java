package com.alii.shope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class testac extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testac);
       int f = (int) 1.4;
        int c = 36;
        Toast.makeText(this, String.valueOf(f), Toast.LENGTH_SHORT).show();
    }
}

package com.alii.shope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchActivity extends AppCompatActivity {

    DatabaseReference root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        root = FirebaseDatabase.getInstance().getReference().child("Users").child("Calls");
        String nameSe = getIntent().getExtras().getString("search");
        Query firebaseSearchQuery = root.orderByChild("code").startAt(nameSe).endAt(nameSe+ "\uf8ff");


    }
}

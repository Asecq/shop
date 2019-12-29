package com.alii.shope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fatoraplus extends AppCompatActivity {
private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatoraplus);

        listView = findViewById(R.id.listttt);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> ingredients = dataSnapshot.getValue(t);
                orders mobile;
                ArrayList<orders> mobiles = new ArrayList<orders>();
                for(int i = 0 ; i<ingredients.size() ; i++) {
                    mobile = new orders();
                    String[] mStringArray = new String[ingredients.size()];
                    mStringArray = ingredients.toArray(mStringArray);
                    //
                    mobile.setName(ingredients.toString());

                    mobiles.add(mobile);
                }
                listView.setAdapter(new okme(fatoraplus.this, mobiles));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

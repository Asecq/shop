package com.alii.shope;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class listviewtestActivity extends AppCompatActivity {
    private ListView listView;
    ArrayList<String> dateOfIssue = new ArrayList<>();
    ArrayList<String> magazineCover = new ArrayList<>();
    ArrayList<String> magazineName = new ArrayList<>();
    DatabaseReference reference;
    private ArrayList<String> keys = new ArrayList<>();
    accountingadapter mycloass;
    accountadapternew ss;
    Double result =0.0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listviewtest);
        final ArrayList<accountingadapter> mobiles = new ArrayList<>();
        DatabaseReference res  = FirebaseDatabase.getInstance().getReference().child("Result");

      final accountadapternew news =   new accountadapternew(listviewtestActivity.this,mobiles , true);
        reference = FirebaseDatabase.getInstance().getReference().child("ItemsAccEntry");
        listView = findViewById(R.id.list_accounting);
        Query query = reference.orderByChild("Account_ID").equalTo("183");

        try{

             reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                     mycloass = snapshot.getValue(accountingadapter.class);
                     mobiles.add(mycloass);
                     listView.setAdapter(news);
                 }


                 news.notifyDataSetChanged();
                 //listView.setSelection(listView.getAdapter().getCount());
             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });

        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
      //  Toast.makeText(this, String.valueOf(result), Toast.LENGTH_SHORT).show();

       listView.invalidate();
    }
}

package com.alii.shope;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
  ArrayAdapter<String> arrayAdapter;
  private DatabaseReference databaseReference;
  private ArrayList<String> keys = new ArrayList<>();
  /* access modifiers changed from: private */
  public ListView mListView;
  /* access modifiers changed from: private */
  public ArrayList<String> names = new ArrayList<>();

  /* access modifiers changed from: protected */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView((int) R.layout.activity_select);
    this.mListView = (ListView) findViewById(R.id.listview_select);
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 17367043, this.keys) {
      public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView=(TextView) view.findViewById(android.R.id.text1);

        /*YOUR CHOICE OF COLOR*/
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        mListView.setBackgroundColor(001621);
        return view;
      }
    };
    this.mListView.setAdapter(adapter);
    this.databaseReference = FirebaseDatabase.getInstance().getReference().child("Protucts");
    final List<String> keys2 = new ArrayList<>();
    this.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
      public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          String barcode = (String) snapshot.child("Descrption").getValue();
          String name = (String) snapshot.child("category").getValue();
          String exp = (String) snapshot.child("pid").getValue();
          String q = (String) snapshot.child("pname").getValue();
          ArrayList access$000 = SelectActivity.this.names;
          StringBuilder sb = new StringBuilder();
          sb.append("Product Name: ");
          sb.append(name);
          sb.append("\nExpiry Date: ");
          sb.append(exp);
          sb.append("\nQuantity: ");
          sb.append(q);
          sb.append("\nBarcode: ");
          sb.append(barcode);
          access$000.add(sb.toString());
          keys2.add(snapshot.getKey());
        }
        adapter.addAll(keys2);
        adapter.notifyDataSetChanged();
        SelectActivity.this.mListView.setBackgroundColor(-1);
        SelectActivity.this.mListView.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String myKey = (String) keys2.get(i);
            Intent putc = new Intent(SelectActivity.this, AdminAddActivity.class);
            putc.putExtra("department", myKey);
            SelectActivity.this.startActivity(putc);
          }
        });
      }

      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }
}

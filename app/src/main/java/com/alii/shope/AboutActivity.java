package com.alii.shope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    private ListView listView;

    View main;
    ImageView print;
    private TextView back , size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_about);
        main = findViewById(R.id.fatora_screen);
        size = findViewById(R.id.totalsizefatora);
        print = findViewById(R.id.prit_image);
        ///=---------------


        //------------

        back = findViewById(R.id.back_to_h);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent printInent = new Intent(AboutActivity.this,printer_fatora.class);
                    printInent.putExtra("key",getIntent().getExtras().get("key").toString());
                    startActivity(printInent);



                }catch (Exception e){
                    Toast.makeText(AboutActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("Calls").child(getIntent().getExtras().get("key").toString());
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //----------------
                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                ArrayList<String> ingredients = dataSnapshot.child("Names").getValue(t);
                ArrayList<String> cou = dataSnapshot.child("Counts").getValue(t);
                ArrayList<String> price = dataSnapshot.child("Prices").getValue(t);
                ArrayList<String> one = dataSnapshot.child("Ones").getValue(t);
                ArrayList<String> deps = dataSnapshot.child("Deps").getValue(t);
                final ArrayList<String> unites = dataSnapshot.child("Units").getValue(t);
                ArrayList<String> totales = dataSnapshot.child("Ptotal").getValue(t);

                listView = findViewById(R.id.listvi);
                Mobile mobile;
                ArrayList<Mobile> mobiles = new ArrayList<Mobile>();

               for(int i = 0 ; i<ingredients.size() ; i++) {
                   mobile = new Mobile();
                   String[] mStringArray = new String[ingredients.size()];
                   mStringArray = ingredients.toArray(mStringArray);
                   //
                   String[] pricess = new String[price.size()];
                   pricess = price.toArray(pricess);

                   //
                   String[] unite = new String[unites.size()];
                   unite = unites.toArray(unite);
                   //
                   String[] totel = new String[totales.size()];
                   totel = totales.toArray(totel);
                   //
                   String[] ones = new String[one.size()];
                   ones = one.toArray(ones);
                   //
                   String[] depa = new String[deps.size()];
                   depa = deps.toArray(depa);
                   //

                   String[] tStringArray = new String[cou.size()];
                   tStringArray = cou.toArray(tStringArray);
                   //
                   mobile.setNames(mStringArray);
                   mobile.setCounts(tStringArray);
                   mobile.setDeps(depa);
                   mobile.setOnes(ones);
                   mobile.setUnits(unite);
                   mobile.setPrices(pricess);
                   mobile.setPtotal(totel);
                   mobiles.add(mobile);
               }


                listView.setAdapter(new adapterlistview(AboutActivity.this, mobiles));

               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Toast.makeText(AboutActivity.this, String.valueOf(position + 1), Toast.LENGTH_SHORT).show();


                   }
               });
               TextView total = findViewById(R.id.totalorder);
               TextView number = findViewById(R.id.numberfatora);
               TextView sta = findViewById(R.id.static_fatora);
               ImageView img_static = findViewById(R.id.img_static);
                if(dataSnapshot.child("static").getValue().toString().equals("2")){
                    sta.setText("تم التجهيز");
                    sta.setTextColor(Color.rgb(0, 204, 0));
                    img_static.setImageResource(R.drawable.donns);
                }
                if(dataSnapshot.child("static").getValue().toString().equals("0")){
                    sta.setText("قيد الأرسال ...");
                    sta.setTextColor(Color.rgb(204, 255, 255));
                    img_static.setImageResource(R.drawable.sending);
                }
                if(dataSnapshot.child("static").getValue().toString().equals("3")){
                    sta.setText("تم تجميدها");
                    sta.setTextColor(Color.rgb(255, 0, 0));
                    img_static.setImageResource(R.drawable.stopp);
                }
                if(dataSnapshot.child("static").getValue().toString().equals("1")){
                    sta.setText("تم الأستلام");
                    sta.setTextColor(Color.rgb(0, 153, 255));
                    img_static.setImageResource(R.drawable.seen);
                }
               total.setText( dataSnapshot.child("Price").getValue().toString() + "$");
                number.setText( "رقم الفاتورة :  "+ dataSnapshot.child("number").getValue().toString() );
                TextView note = findViewById(R.id.note_fatora);
                note.setText("ملاحظات : " + dataSnapshot.child("note").getValue().toString());
                TextView time = findViewById(R.id.time_fatora);
                time.setText(dataSnapshot.child("time").getValue().toString());
                size.setText("عناصر الفاتورة : "  + String.valueOf(listView.getAdapter().getCount()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}

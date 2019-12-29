package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.alii.shope.R.layout.activity_cardme;
import static com.alii.shope.R.layout.support_simple_spinner_dropdown_item;
import static com.alii.shope.R.layout.t;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
private String title , location , image  , code , model, count , des   ;
private TextView text_title , text_price , text_price2  , text_price3 , text_price4 , text_des , text_code,text_count,text_model , text_local , saveimage  , text_total, text_number_order;
private int number_order;
private Button order , p_order , s_order;
private String pricefinaly;
private Boolean type_price,type_price1,type_price2 , type_price4;
private Boolean ok;
private  ArrayAdapter<String> spinnerArrayAdapter;
private  int to;
private String priceRelly;
private Spinner spinner;
private String Main = "";
    private List<String> plantsList ;
 ArrayList<String> str = new ArrayList<>(); ;
 private String dep = "";
private ProgressDialog mess ;
private TextView deps;
private String[] plants;
private ZoomInImageView  image_post;
private String price = "";
private String price2 ="";
private String price3 = "";
private double re;
private ListView show;
private String price4 = "";
private ArrayList<String> una = new ArrayList<>();
OutputStream outputStream;
private ViewGroup.LayoutParams lp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        show = findViewById(R.id.listview_show);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        saveimage  = findViewById(R.id.save_image);
        mess = new ProgressDialog(this);
        spinner  = findViewById(R.id.spinner2);
        image_post = findViewById(R.id.infopost_image);
        text_title = findViewById(R.id.title_post);
        text_total = findViewById(R.id.price_total);
        text_des = findViewById(R.id.infopost_des);
        order = findViewById(R.id.order_btn);
        p_order = findViewById(R.id.plus_number_order1);
        s_order = findViewById(R.id.salp_number_order1);
        text_number_order = findViewById(R.id.number_order1);
        number_order = 1;
        dep = getIntent().getExtras().getString("dep");
        lp = (ViewGroup.LayoutParams) show.getLayoutParams();

        //lp.height = 1100;
      //  show.setLayoutParams(lp);



        //--------
        String[] tes = {"قطعة","درزن","سيت","كارتونة" };
        spinner.setOnItemSelectedListener(this);

        //------------------------------------------------
        title = getIntent().getExtras().getString("title_post");
        price = getIntent().getExtras().getString("price");
        image = getIntent().getExtras().getString("img_post");
        code = getIntent().getExtras().getString("code_post");
        model = getIntent().getExtras().getString("model_post");
        price2 = getIntent().getExtras().getString("pp");
        price3 = getIntent().getExtras().getString("ppp");
        price4 = getIntent().getExtras().getString("pppp");
        count = getIntent().getExtras().getString("countt");
        des = getIntent().getExtras().getString("des_post");
        location = getIntent().getExtras().getString("local_post");
        text_des.setText(des);
        una.add("الرمز : "+ code);
        una.add("القسم : " +dep);
        una.add("الصنع : " + model);
        una.add("العدد داخل الكارتونة : " + count);
        text_title.setText(title);
        saveimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable draw = (BitmapDrawable) image_post.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/MainProgram");
                dir.mkdirs();
                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                try {
                    outStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(PostActivity.this, "تم حفظ الصورة", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(outFile));
                sendBroadcast(intent);

            }
        });
        Picasso.get().load(image).into(image_post);
        plants = new String[]{
                "قطعة",
               "درزن",
                "سيت",
                "كارتونة"
        };
        plantsList = new ArrayList<>(Arrays.asList(plants));
        Main = getIntent().getExtras().getString("maintype");
        if(price.length() > 0){
            str.add("قطعة");
            type_price = true;
            una.add("السعر بالقطعة : " + price  + " " + "$");
        }else {
            type_price = false;
        }
        if(price2.length() > 0){
            una.add("السعر بالدرزن : " + price2  + " " + "$");
            type_price1 = true;
            str.add("درزن");

        }else {
            type_price1= false;

        }
        if(price4.length() > 0) {
            una.add("السعر بالسيت : " + price4  + " " + "$");
            type_price4 = true;
            str.add("سيت");
        }else {
            type_price4 = false;
        }
        if(price3.length() > 0){
            una.add("السعر بالكارتونة : " + price3  + " " + "$");
            type_price2 = true;
            str.add("كارتونة");
        }else {
            type_price2 = false;
        }
        try{
            if(type_price == false) {
                plantsList.remove("قطعة");
            }
            if(type_price1 == false) {
                plantsList.remove("درزن");
            }
            if(type_price4 == false) {
                plantsList.remove("سيت");
            }
            if(type_price2 == false) {
                plantsList.remove("كارتونة");
            }
            String[] mStringArray = new String[una.size()];
            mStringArray = una.toArray(mStringArray);
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(PostActivity.this,
                    android.R.layout.simple_list_item_1, mStringArray){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view =super.getView(position, convertView, parent);

                    TextView textView=(TextView) view.findViewById(android.R.id.text1);

                    /*YOUR CHOICE OF COLOR*/
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(20);
                    show.setBackgroundColor(001621);

                    return view;
                }
            };
            //TextView textView1=(TextView)findViewById(android.R.id.text1);

           // textView1.setTextSize(20);
            show.setAdapter(listAdapter);
            if(show.getAdapter().getCount() == 8){
                lp.height = 1100;
                show.setLayoutParams(lp);
            }else if(show.getAdapter().getCount() == 7){
                lp.height = 930;
                show.setLayoutParams(lp);
            }else if(show.getAdapter().getCount() == 6){
                lp.height = 780;
                show.setLayoutParams(lp);
            }else if(show.getAdapter().getCount() == 5){
                lp.height = 650;
                show.setLayoutParams(lp);
            }


            spinnerArrayAdapter = new ArrayAdapter<String>(this,   support_simple_spinner_dropdown_item, plantsList);
            spinnerArrayAdapter.setDropDownViewResource(support_simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
        if (Main != null) {
            int spinnerPosition = spinnerArrayAdapter.getPosition(Main);
            spinner.setSelection(spinnerPosition);
        }


        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(number_order <= 0) {
                    Toast.makeText(PostActivity.this, "خطأ", Toast.LENGTH_SHORT).show();
                }else {
                    addCard();
                    mess.setTitle("ارسال");
                    mess.setMessage("الرجاء الأنتظار ...");
                    mess.show();
                }

            }
        });

    }


    private void addCard() {
        final HashMap<String ,Object> hashMap = new HashMap<>();
        Long tsLong = System.currentTimeMillis()/1000;
        final String codeR = tsLong.toString();
        hashMap.put("name",title);
        hashMap.put("dep",dep);
        hashMap.put("maintype",spinner.getSelectedItem().toString().trim());
        hashMap.put("num",code);
        hashMap.put("statices","yes".trim());
        hashMap.put("count",Integer.parseInt(text_number_order.getText().toString()));
        hashMap.put("img" , image);
        hashMap.put("pricepost",priceRelly);
        hashMap.put("user",Prevalent.currentOnlineUser.getUsername());
        String t_price = text_total.getText().toString();
        hashMap.put("price",t_price.replace("$","").trim());
        hashMap.put("userid",Prevalent.currentOnlineUser.getPhone());

        if(ok == true) {
            final DatabaseReference rootcheck = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
            rootcheck.child("checked").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(getIntent().getExtras().getString("idpost")).exists()){
                        final Dialog msgs = new Dialog(PostActivity.this);
                        msgs.setContentView(R.layout.new_ol);
                        msgs.setTitle("رسالة");
                        Button okmsg = msgs.findViewById(R.id.msg_ok);
                        msgs.show();
                        okmsg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                msgs.dismiss();
                            }
                        });
                        mess.dismiss();
                    } else {
                        DatabaseReference depc = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                        depc.child("checkdep").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(dataSnapshot.child(dep).exists()){
                                        HashMap<String , Object> checked = new HashMap<>();
                                        HashMap<String , Object> depcheck = new HashMap<>();
                                        depcheck.put("dep",dep);
                                        DatabaseReference depc = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                        depc.child("checkdep").child(dep).updateChildren(depcheck);
                                        checked.put("main",spinner.getSelectedItem().toString().trim());
                                        rootcheck.child("checked").child(getIntent().getExtras().getString("idpost")).updateChildren(checked);
                                        DatabaseReference sizedata = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                        sizedata.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");

                                                hashMap.put("code" ,codeR);
                                                hashMap.put("unites",str);
                                                hashMap.put("id",getIntent().getExtras().getString("idpost"));
                                                root.child(codeR).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mess.dismiss();


                                                        Toast.makeText(PostActivity.this, "تم الحفظ في المحفظة", Toast.LENGTH_SHORT).show();
                                       /* Intent intent = new Intent(PostActivity.this,HomeActivity.class);
                                        startActivity(intent);*/
                                                        onBackPressed();





                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }else {
                                        final Dialog msgs = new Dialog(PostActivity.this);
                                        msgs.setContentView(R.layout.old_dep);
                                        msgs.setTitle("رسالة");
                                        Button okmsg = msgs.findViewById(R.id.msg_ok);
                                        msgs.show();
                                        okmsg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                msgs.dismiss();
                                            }
                                        });
                                        mess.dismiss();

                                    }
                                }else {
                                    HashMap<String , Object> checked = new HashMap<>();
                                    HashMap<String , Object> depcheck = new HashMap<>();
                                    depcheck.put("dep",dep);
                                    DatabaseReference depc = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                    depc.child("checkdep").child(dep).updateChildren(depcheck);
                                    checked.put("main",spinner.getSelectedItem().toString().trim());
                                    rootcheck.child("checked").child(getIntent().getExtras().getString("idpost")).updateChildren(checked);
                                    DatabaseReference sizedata = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                    sizedata.child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");

                                            hashMap.put("code" ,codeR);
                                            hashMap.put("unites",str);
                                            hashMap.put("id",getIntent().getExtras().getString("idpost"));
                                            root.child(codeR).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    mess.dismiss();


                                                    Toast.makeText(PostActivity.this, "تم الحفظ في المحفظة", Toast.LENGTH_SHORT).show();
                                       /* Intent intent = new Intent(PostActivity.this,HomeActivity.class);
                                        startActivity(intent);*/
                                                    onBackPressed();





                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
           // rootcheck








        }else {
            Toast.makeText(PostActivity.this, "هناك خطأ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getItemAtPosition(i).toString() == "قطعة") {
            number_order = 1;
            text_number_order.setText("1");
            if(type_price == true) {
                ok = true;
                text_total.setText("1");
                checkN(price);
                priceRelly = price;
                if(number_order == 1){
                    s_order.setEnabled(false);
                }else {
                    s_order.setEnabled(true);
                }
            }
        }
        if(adapterView.getItemAtPosition(i).toString() == "درزن") {
            number_order = 1;
            text_number_order.setText("1");
            if(type_price1 == true) {
                ok = true;
                checkN(price2);
                ok = true;
                priceRelly = price2;
                if(number_order == 1){
                    s_order.setEnabled(false);
                }else {
                    s_order.setEnabled(true);
                }
            }
        }
        if(adapterView.getItemAtPosition(i).toString() == "سيت") {
            number_order = 1;
            text_number_order.setText("1");
            if(type_price4 == true) {
                ok = true;
                checkN(price4);
                ok = true;
                priceRelly = price4;
                if(number_order == 1){
                    s_order.setEnabled(false);
                }else {
                    s_order.setEnabled(true);
                }
            }
        }
        if(adapterView.getItemAtPosition(i).toString() == "كارتونة") {

            number_order = 1;
            text_number_order.setText("1");
            if(type_price2 == true) {
                priceRelly = price3;
                if(number_order == 1){
                    s_order.setEnabled(false);
                }else {
                    s_order.setEnabled(true);
                }
                ok = true;
               checkN(price3);
                ok = true;

            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void checkN(final String num) {
        if(num.contains(".")){
            re = Double.parseDouble(num);
            p_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    number_order +=1;
                    Double total = (Double) (re*number_order) ;
                    text_number_order.setText("" + number_order);
                    text_total.setText(""+total + " " +  "$");
                    if(number_order == 1){
                        s_order.setEnabled(false);
                    }else {
                        s_order.setEnabled(true);
                    }
                }
            });
            s_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    number_order -=1;
                    Double total = (Double) (re*number_order) ;
                    text_number_order.setText("" + number_order);
                    text_total.setText("" + total + " " +  "$");
                    if(number_order == 1){
                        s_order.setEnabled(false);
                    }else {
                        s_order.setEnabled(true);
                    }
                }
            });
        }else {
            to = Integer.parseInt(num);
            p_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    number_order +=1;
                    int total = (int) (to*number_order) ;
                    text_number_order.setText("" + number_order);
                    text_total.setText(""+total + " " +  "$");
                    if(number_order == 1){
                        s_order.setEnabled(false);
                    }else {
                        s_order.setEnabled(true);
                    }
                }
            });
            s_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    number_order -=1;
                    int total =  (to *number_order) ;
                    text_number_order.setText("" + number_order);
                    text_total.setText("" + total + " " +  "$");
                    if(number_order == 1){
                        s_order.setEnabled(false);
                        int totas = (int) (to*number_order) ;
                        text_number_order.setText("" + number_order);
                        text_total.setText("" + totas + " " +  "$");
                    }else {
                        s_order.setEnabled(true);
                    }
                }
            });
        }


        text_total.setText(""+num + " " +  "$");


    }

}

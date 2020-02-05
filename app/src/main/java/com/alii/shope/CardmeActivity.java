package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.layout.simple_spinner_item;

public class CardmeActivity extends AppCompatActivity {



    private ImageView donenote;
    private DatabaseReference root;
    private  String codeR;
    private RecyclerView recyclerView;
    private CheckBox checkedall;
    private ProgressDialog dialogload;
   private ArrayList<String> userInterests;
    private int number_order;
    DatabaseReference callmeover ;
    private FirebaseRecyclerAdapter<orders,holder_me> adapter;
    private FirebaseRecyclerOptions<orders> options;
    private List<CheckBox> intradayCheckboxsList = new ArrayList<>();
    private int numOrder;
    private CheckBox checkBox;
    ArrayList<String> check_name = new ArrayList<String>();
    ArrayList<String> check_price = new ArrayList<String>();
    ArrayList<String> allorders = new ArrayList<String>();
    ArrayList<String> check_number = new ArrayList<>();
    ArrayList<String> check_total_price = new ArrayList<String>();
    ArrayList<String> check_code = new ArrayList<String>();
    ArrayList<String> check_id = new ArrayList<String>();
    ArrayList<String> check_count = new ArrayList<String>();
    ArrayList<String> unselected = new ArrayList<String>();
    private ProgressDialog mess ;
    ArrayList<String> check_num = new ArrayList<String>();
   private String Acheck;
    ArrayList<String> check_dep = new ArrayList<String>();
    ArrayList<String> check_unit = new ArrayList<String>();
    ArrayList<String> okcheck = new ArrayList<String>();
    private ScrollView scrollcard;
    public String names_orders = "";
    private Boolean isSelectedAll;
    ArrayList<String> check_img = new ArrayList<String>();
    private CheckBox checkall;
    Boolean d;
    private Boolean okok;

    private DatabaseReference deleteone;
    private EditText note;
    private String totally;
    private GenericTypeIndicator<ArrayList<String>> t;
    private ProgressDialog dialog ;
    private Button sent;
    private Button allbtn;
    private double overtotal = 0.0;
    private TextView zpon;
    private int overtotal2 = 0;
    private android.widget.CheckBox chk;
    private String id_user , user_user;
    SharedPreferences myPrefs;
/*

for (String full : allorders) {
                                root.child(full).child("statices").setValue("yes");
                                finish();
                                startActivity(getIntent());
                            }
 */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardme);
        zpon = findViewById(R.id.select_zpon);
        if(Prevalent.currentOnlineUser.getType().equals("saller")){
            id_user = getIntent().getExtras().getString("id");
            user_user = getIntent().getExtras().getString("username");
        }
        if(!TextUtils.isEmpty(user_user)){
            zpon.setText(user_user);
        }
        t = new GenericTypeIndicator<ArrayList<String>>() {};
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
        checkBox  = findViewById(R.id.selectall_orders);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(okok == false){
                    if(checkBox.isChecked()) {
                        for (String full : allorders) {
                            root.child(full).child("statices").setValue("yes");
                            recyclerView.smoothScrollToPosition(1);
                            new CountDownTimer(1000, 1000) {

                                public void onTick(long millisUntilFinished) {

                                }

                                public void onFinish() {
                                    recyclerView.smoothScrollToPosition(adapter.getItemCount());
                                }

                            }.start();
                        }
                    }
                }else {
                    if(checkBox.isChecked() == false){
                        checkBox.setChecked(true);
                    }
                }
            }
        });

        mess = new ProgressDialog(this);
        deleteone = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("checked");
        dialogload = new ProgressDialog(this);
        dialogload.setMessage("تحميل المحفظة ...");
        dialogload.setTitle("تحميل");
        dialogload.show();

        callmeover = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        sent = findViewById(R.id.send_fatora);
        dialog = new ProgressDialog(this);
        dialog.setTitle("تحميل");
        dialog.setMessage("جاري تحميل المحفظة");
        //dialog.show();
        d = true;
        donenote = findViewById(R.id.donw_note);
        donenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(note.getWindowToken(), 0);
                donenote.setVisibility(View.INVISIBLE);
                note.setFocusable(false);
            }
        });
        scrollcard = findViewById(R.id.scrollcard);
        scrollcard.post(new Runnable() {
            @Override
            public void run() {
                scrollcard.fullScroll(View.FOCUS_DOWN);
            }
        });

        note = findViewById(R.id.note_order);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               note.setFocusableInTouchMode(true);
            }
        });
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                donenote.setVisibility(View.VISIBLE);
            }
        });

        TextView titne = findViewById(R.id.price_total_fatora);
        titne.setText("المبلغ الكلي : " + "0" + "$");


        recyclerView = findViewById(R.id.re_cardme1);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        final String postuser = Prevalent.currentOnlineUser.getPhone();
        root = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");


        options = new FirebaseRecyclerOptions.Builder<orders>().setQuery(root, orders.class).build();
        adapter = new FirebaseRecyclerAdapter<orders, holder_me>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final holder_me holder_me, final int i, @NonNull final orders cards) {
                holder_me.maintype.setText(cards.getMaintype());
               /* holder_me.maintype.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_unit.remove(cards.getMaintype());
                        final Dialog chang = new Dialog(CardmeActivity.this);
                        chang.setContentView(R.layout.listview_selectunit);
                        chang.setTitle("تعديل الوحدة");
                        chang.show();
                        reference.child(cards.getCode()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<String> deps = dataSnapshot.child("unites").getValue(t);
                                for(int i = 0 ; i<deps.size() ; i++) {
                                    String[] mStringArray = new String[deps.size()];
                                    mStringArray = deps.toArray(mStringArray);
                                    final ListView mylistview = chang.findViewById(R.id.listvi_change);
                                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(CardmeActivity.this,
                                            android.R.layout.simple_list_item_1, mStringArray);
                                    mylistview.setAdapter(listAdapter);
                                    mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            DatabaseReference change  = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                                            change.child("maintype").setValue((mylistview.getItemAtPosition(position)));
                                            chang.dismiss();
                                            Intent ns = new Intent(CardmeActivity.this,CardmeActivity.class);
                                            startActivity(ns);

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }); */
                Acheck = cards.getStatices();


                allorders.add(cards.getCode());
                holder_me.dep.setText(cards.getDep());

                holder_me.name.setText(cards.getName());

                final String mnumber = Integer.toString(cards.getCount());
                holder_me.price.setText("$" + cards.getPrice());

                if (cards.getCount() == 1) {
                    holder_me.slep.setEnabled(false);
                }
                Picasso.get().load(cards.getImg()).into(holder_me.img_card);


                //===================================================

                holder_me.img_card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(int s =0 ; s <= recyclerView.getAdapter().getItemCount() ; s++) {
                            recyclerView.smoothScrollToPosition(s);
                        }
                    }
                });
                //----------------------------------------------------------------------------------


/*
                     if(holder_me.checkBox.isChecked()) {
                         if(check_code.contains(cards.getCode())){
                             check_code.remove(cards.getCode());
                             check_name.remove(cards.getName());
                             check_price.remove(cards.getPricepost());
                             check_dep.remove(cards.getDep());
                             check_unit.remove(cards.getMaintype());
                             check_num.remove(cards.getNum());
                             check_count.remove(String.valueOf(cards.getCount()));
                             check_code.add(cards.getCode());
                             check_num.add(cards.getNum());
                             check_name.add(cards.getName());
                             check_price.add(cards.getPricepost());
                             check_dep.add(cards.getDep());
                             check_unit.add(cards.getMaintype());
                             check_count.add(String.valueOf(cards.getCount()));
                             sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                         }else {
                             check_code.add(cards.getCode());
                             check_num.add(cards.getNum());
                             check_name.add(cards.getName());
                             check_price.add(cards.getPricepost());
                             check_dep.add(cards.getDep());
                             check_unit.add(cards.getMaintype());
                             check_count.add(String.valueOf(cards.getCount()));
                             sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                         }
                     }else {
                         check_code.remove(cards.getCode());
                         check_name.remove(cards.getName());
                         check_price.remove(cards.getPricepost());
                         check_dep.remove(cards.getDep());
                         check_unit.remove(cards.getMaintype());
                         check_num.remove(cards.getNum());
                         check_count.remove(String.valueOf(cards.getCount()));
                         sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                     } */
/*

 holder_me.delete.setEnabled(false);
                         if(cards.getCount() == 1) {
                             holder_me.slep.setEnabled(false);
                         }else {
                             holder_me.slep.setEnabled(true);
                         }
 */
/*

 int oneType1 =  Integer.valueOf(cards.getPricepost()) * cards.getCount();
                         overtotal += oneType1;
                         holder_me.plus.setEnabled(false);
                         holder_me.slep.setEnabled(false);
                         TextView ttfatora = findViewById(R.id.price_total_fatora);
                         ttfatora.setText("المبلغ الكلي : " +String.valueOf(overtotal) + "$");
                         totally= ttfatora.getText().toString().trim();
 */
/*

holder_me.delete.setEnabled(true);
                         int oneType1 =  Integer.valueOf(cards.getPricepost()) * cards.getCount();
                         overtotal -= oneType1;
                         TextView ttfatora = findViewById(R.id.price_total_fatora);
                         ttfatora.setText("المبلغ الكلي : " +String.valueOf(overtotal) + "$");
                         totally= ttfatora.getText().toString().trim();
                         holder_me.plus.setEnabled(true);
                         sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                         holder_me.slep.setEnabled(true);
                         if(cards.getCount() == 1) {
                             holder_me.slep.setEnabled(false);
                         }
 */


                //=======================================================


             /*   holder_me.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (holder_me.checkBox.isChecked()) {
                            if (check_code.contains(cards.getCode())) {

                                check_code.remove(cards.getCode());
                                check_name.remove(cards.getName());
                                check_price.remove(cards.getPricepost());
                                check_dep.remove(cards.getDep());
                                check_unit.remove(cards.getMaintype());
                                check_num.remove(cards.getNum());
                                check_count.remove(String.valueOf(cards.getCount()));
                                sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                                holder_me.delete.setEnabled(true);
                                int oneType1 = Integer.valueOf(cards.getPricepost()) * cards.getCount();
                                overtotal -= oneType1;
                                TextView ttfatora = findViewById(R.id.price_total_fatora);
                                ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                                totally = ttfatora.getText().toString().trim();
                                holder_me.plus.setEnabled(true);
                                sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                                holder_me.slep.setEnabled(true);
                                if (cards.getCount() == 1) {
                                    holder_me.slep.setEnabled(false);
                                }
                            } else {
                                check_code.add(cards.getCode());
                                check_name.add(cards.getName());
                                check_price.add(cards.getPricepost());
                                check_dep.add(cards.getDep());
                                check_unit.add(cards.getMaintype());
                                check_num.add(cards.getNum());
                                check_count.add(String.valueOf(cards.getCount()));
                                sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                                holder_me.delete.setEnabled(false);
                                if (cards.getCount() == 1) {
                                    holder_me.slep.setEnabled(false);
                                } else {
                                    holder_me.slep.setEnabled(true);
                                }
                                int oneType1 = Integer.valueOf(cards.getPricepost()) * cards.getCount();
                                overtotal += oneType1;
                                holder_me.plus.setEnabled(false);
                                holder_me.slep.setEnabled(false);
                                TextView ttfatora = findViewById(R.id.price_total_fatora);
                                ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                                totally = ttfatora.getText().toString().trim();
                            }
                        } else {
                            check_code.remove(cards.getCode());
                            check_name.remove(cards.getName());
                            check_price.remove(cards.getPricepost());
                            check_dep.remove(cards.getDep());
                            check_unit.remove(cards.getMaintype());
                            check_num.remove(cards.getNum());
                            check_count.remove(String.valueOf(cards.getCount()));
                            sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() + ")");
                            holder_me.delete.setEnabled(false);
                            if (cards.getCount() == 1) {
                                holder_me.slep.setEnabled(false);
                            } else {
                                holder_me.slep.setEnabled(true);
                            }
                            int oneType1 = Integer.valueOf(cards.getPricepost()) * cards.getCount();
                            overtotal -= oneType1;
                            holder_me.plus.setEnabled(false);
                            holder_me.slep.setEnabled(false);
                            TextView ttfatora = findViewById(R.id.price_total_fatora);
                            ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                            totally = ttfatora.getText().toString().trim();
                        }

                    }
                }); */


                //==============


                holder_me.numberall.setText(Integer.toString(cards.getCount()));
                holder_me.price.setText("$" + cards.getPrice());
                number_order = Integer.parseInt(mnumber);
                holder_me.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (cards.getCount() == 1) {
                            holder_me.slep.setEnabled(false);
                        } else {
                            holder_me.slep.setEnabled(true);

                        }
                        number_order = Integer.parseInt(mnumber);
                        holder_me.numberall.setText(Integer.toString(cards.getCount()));
                        holder_me.price.setText("$" + cards.getPrice());
                        number_order += 1;
                        String num = Integer.toString(number_order);
                        holder_me.numberall.setText(num);

                       if(cards.getPricepost().contains(".")){
                           Double d = Double.parseDouble(cards.getPricepost());
                           Double tottal = (Double) (d * number_order);
                           holder_me.price.setText("$" + (tottal));
                           DatabaseReference rootsave = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                           HashMap<String, Object> hashMap = new HashMap<>();
                           hashMap.put("count", Integer.parseInt(holder_me.numberall.getText().toString()));
                           hashMap.put("price", holder_me.price.getText().toString().replace("$", ""));
                           rootsave.updateChildren(hashMap);
                       }else {
                           int tottal = (int) (Integer.parseInt(cards.getPricepost()) * number_order);
                           holder_me.price.setText("$" + (tottal));
                           DatabaseReference rootsave = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                           HashMap<String, Object> hashMap = new HashMap<>();
                           hashMap.put("count", Integer.parseInt(holder_me.numberall.getText().toString()));
                           hashMap.put("price", holder_me.price.getText().toString().replace("$", ""));
                           rootsave.updateChildren(hashMap);
                       }
                        //  Toast.makeText(CardmeActivity.this, String.valueOf(to), Toast.LENGTH_SHORT).show();


                    }
                });
                holder_me.slep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        holder_me.numberall.setText(Integer.toString(cards.getCount()));
                        holder_me.price.setText("$" + cards.getPrice());
                        number_order = Integer.parseInt(mnumber);
                        number_order -= 1;
                        String num = Integer.toString(number_order);
                        holder_me.numberall.setText(num);
                        if(cards.getPricepost().contains(".")){
                            Double d = Double.parseDouble(cards.getPricepost());
                            Double tottal = (Double) (d * number_order);
                            holder_me.price.setText("$" + (tottal));
                            DatabaseReference rootsave = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("count", Integer.parseInt(holder_me.numberall.getText().toString()));
                            hashMap.put("price", holder_me.price.getText().toString().replace("$", ""));
                            rootsave.updateChildren(hashMap);
                        }else {
                            int total = Integer.parseInt(cards.getPricepost()) * number_order;
                            holder_me.price.setText("$" + Integer.toString(total));
                            DatabaseReference rootsave = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("count", Integer.parseInt(holder_me.numberall.getText().toString()));
                            hashMap.put("price", holder_me.price.getText().toString().replace("$", ""));
                            rootsave.updateChildren(hashMap);
                        }
                        //   Toast.makeText(CardmeActivity.this, String.valueOf(to), Toast.LENGTH_SHORT).show();


                    }
                });


                //[[[[[

/*
                holder_me.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog edit = new Dialog(CardmeActivity.this);
                        edit.setContentView(R.layout.orderedit);
                        edit.setTitle("تعديل الطلب");
                        edit.show();

                        //=========



                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference rootsave =  FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders").child(cards.getCode());
                                HashMap<String ,Object> hashMap = new HashMap<>();
                                hashMap.put("count",Integer.parseInt(number.getText().toString()));
                                hashMap.put("price",pricetotal.getText().toString().replace("$",""));
                               rootsave.updateChildren(hashMap);
                                edit.dismiss();
                            }
                        });
                    }
                });
               /* holder_me.send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       final ProgressDialog loading = new ProgressDialog(CardmeActivity.this);
                        loading.setTitle("ارسال الطلب");
                        loading.setMessage("يتم الأن ارسال طلبك ...");
                        loading.show();
                        HashMap<String,Object> sent = new HashMap<>();
                        sent.put("user",cards.getUser().toString());
                        sent.put("phone",Prevalent.currentOnlineUser.getPhone());
                        sent.put("code",cards.getCode().toString());
                        sent.put("price",cards.getPrice().toString() + "$");
                        sent.put("count",Integer.toString(cards.getCount()));
                        DateFormat df = new SimpleDateFormat("dd MM yyyy");
                        String date = df.format(Calendar.getInstance().getTime());
                        sent.put("time",date);
                        DatabaseReference rootsent = FirebaseDatabase.getInstance().getReference();
                        rootsent.child("Calls").child(cards.getCode()).updateChildren(sent).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(CardmeActivity.this, "تم ارسال الطلب", Toast.LENGTH_SHORT).show();
                                root.child(cards.getCode()).removeValue();
                                loading.dismiss();

                            }
                        });

                    }
                }); */
                holder_me.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog delete = new Dialog(CardmeActivity.this);
                        delete.setTitle("حذف الطلب");
                        delete.setContentView(R.layout.makesure);
                        delete.show();
                        Button yes = delete.findViewById(R.id.dialog_ok);
                        Button no = delete.findViewById(R.id.dialog_cancel);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if(check_code.contains(cards.getCode())){
                                    check_code.remove(cards.getCode());
                                    check_id.remove(cards.getId());
                                }
                                deleteone.child(cards.getId()).removeValue();
                                root.child(cards.getCode()).removeValue();

                              //  Toast.makeText(CardmeActivity.this, cards.getId(), Toast.LENGTH_SHORT).show();
                                delete.dismiss();
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete.dismiss();
                            }
                        });

                    }
                });
                sent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            if (overtotal == 0) {
                                Toast.makeText(CardmeActivity.this, "لم تحدد طلب", Toast.LENGTH_SHORT).show();
                            }else if(check_id.size() > 20){
                                Toast.makeText(CardmeActivity.this, "عدد الطلبات المسموحة 20 طلب", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                final Dialog sendd = new Dialog(CardmeActivity.this);
                                sendd.setTitle("أرسال طلبية");
                                sendd.setContentView(R.layout.checkfaroraa);
                                sendd.show();
                                Button yes = sendd.findViewById(R.id.dialog_ok);
                                Button no = sendd.findViewById(R.id.dialog_cancel);
                                yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mess.setMessage("جاري ارسال الفاتورة");
                                        mess.setTitle("الأرسال");
                                        mess.show();
                                        final HashMap<String, Object> hashSend = new HashMap<>();
                                        final HashMap<String, Object> hashSend2 = new HashMap<>();

                                        hashSend.put("Names", check_name);
                                        hashSend.put("Counts", check_count);
                                        hashSend.put("Deps", check_dep);
                                        hashSend.put("Units", check_unit);
                                        hashSend.put("Prices", check_price);
                                        hashSend.put("Ones", check_num);
                                        hashSend.put("Sfatora","0");
                                        if(!TextUtils.isEmpty(user_user)){
                                            hashSend.put("saller","yes");
                                        }
                                        hashSend.put("dep",cards.getDep());
                                        hashSend.put("Ptotal",check_total_price);

                                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss" , Locale.ENGLISH);
                                        SimpleDateFormat sdf = new SimpleDateFormat("MM");
                                        final String month = sdf.format(new Date());
                                        final String now = df.format(new Date());
                                        hashSend.put("time", now);
                                        final HashMap<String , Object> callme = new HashMap<>();

                                        callme.put("time",now);
                                        String n = totally.replace("$", "");
                                        final String re = n.replace("المبلغ الكلي : ", "");
                                        hashSend.put("Price", re);
                                        hashSend.put("phone", Prevalent.currentOnlineUser.getPhone());
                                        hashSend.put("user", Prevalent.currentOnlineUser.getUsername());
                                        if (!TextUtils.isEmpty(note.getText().toString())) {
                                            hashSend.put("note", note.getText().toString().trim());
                                        }
                                        hashSend.put("note", note.getText().toString().trim());
                                        callme.put("note", note.getText().toString().trim());
                                        Long tsLong = System.currentTimeMillis() / 1000;
                                        codeR = tsLong.toString();
                                        hashSend.put("code", codeR);
                                        callme.put("code", codeR);
                                        //---------
                                        hashSend2.put("Names", check_name);
                                        hashSend2.put("Counts", check_count);
                                        hashSend2.put("Deps", check_dep);
                                        hashSend2.put("Units", check_unit);
                                        hashSend2.put("Prices", check_price);
                                        hashSend2.put("Ones", check_num);
                                        hashSend2.put("Sfatora","0");
                                        hashSend2.put("saller",Prevalent.currentOnlineUser.getUsername());
                                        hashSend2.put("dep",cards.getDep());
                                        hashSend2.put("Ptotal",check_total_price);

                                        //--
                                        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss" , Locale.ENGLISH);
                                        final String now2 = df.format(new Date());
                                        hashSend2.put("time",now2);
                                        final HashMap<String , Object> callme2 = new HashMap<>();

                                        callme2.put("time",now2);
                                        String n2 = totally.replace("$", "");
                                        final String re2 = n2.replace("المبلغ الكلي : ", "");
                                        hashSend2.put("Price", re2);
                                        hashSend2.put("phone", id_user);
                                        hashSend2.put("user", user_user);
                                        if (!TextUtils.isEmpty(note.getText().toString())) {
                                            hashSend2.put("note", note.getText().toString().trim());
                                        }
                                        hashSend2.put("note", note.getText().toString().trim());
                                        callme2.put("note", note.getText().toString().trim());
                                        Long tsLong2 = System.currentTimeMillis() / 1000;
                                        codeR = tsLong2.toString();
                                        hashSend2.put("code", codeR);
                                        callme2.put("code", codeR);

                                        //
                                        DatabaseReference sizedata = FirebaseDatabase.getInstance().getReference();
                                        sizedata.child("Calls").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(Prevalent.currentOnlineUser.getType().equals("saller")){
                                                    final DatabaseReference rootnew = FirebaseDatabase.getInstance().getReference().child("Calls");
                                                    DatabaseReference dnew = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("Calls");
                                                    hashSend2.put("number", (int) dataSnapshot.getChildrenCount() + 1);
                                                    callme2.put("phone",id_user);
                                                    hashSend2.put("saller",Prevalent.currentOnlineUser.getUsername());
                                                    callme2.put("user",user_user);
                                                    callme2.put("statics","0");
                                                    callme2.put("saller",Prevalent.currentOnlineUser.getUsername());
                                                    callme2.put("dep",cards.getDep());
                                                    callme2.put("number", String.valueOf( dataSnapshot.getChildrenCount() + 1));
                                                    DatabaseReference depssa = FirebaseDatabase.getInstance().getReference();
                                                    depssa.child("depsfatora").child(cards.getDep()).child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme2);
                                                    DatabaseReference newro = FirebaseDatabase.getInstance().getReference().child("callsover");
                                                    newro.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme2);
                                                    callmeover.child("callsover").child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme2);
                                                    DatabaseReference mandop = FirebaseDatabase.getInstance().getReference().child("Users").child(id_user);
                                                    mandop.child("Calls").child((String.valueOf((int) dataSnapshot.getChildrenCount() + 1))).updateChildren(hashSend2);
                                                    mandop.child("callsover").child((String.valueOf((int) dataSnapshot.getChildrenCount() + 1))).updateChildren(callme2);
                                                    dnew.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(hashSend2);
                                                    rootnew.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(hashSend2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            Toast.makeText(CardmeActivity.this, "تم ارسال الفاتورة بنجاح", Toast.LENGTH_SHORT).show();

                                                            try {
                                                                root.child(Prevalent.currentOnlineUser.getPhone()).child("orders");
                                                                for( final String str : check_code) {
                                                                    root.child(str).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            check_code.clear();
                                                                            check_count.clear();
                                                                            check_dep.clear();
                                                                            check_number.clear();
                                                                            check_name.clear();
                                                                            check_price.clear();
                                                                            check_unit.clear();
                                                                            check_total_price.clear();
                                                                            overtotal = 0;
                                                                            TextView ttfatora = findViewById(R.id.price_total_fatora);
                                                                            ttfatora.setText("المبلغ الكلي : " + overtotal + "$");
                                                                            sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                                                                            for(final String del : check_id){
                                                                                DatabaseReference deletes = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                                                                deletes.child("checked").child(del).removeValue();
                                                                            }
                                                                            mess.dismiss();
                                                                            check_id.clear();
                                                                            final DatabaseReference checkdep = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                                                            checkdep.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (!dataSnapshot.child("checked").exists()){
                                                                                        checkdep.child("checkdep").removeValue();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                            Intent goe = new Intent(CardmeActivity.this,HomeActivity.class);
                                                                            startActivity(goe);

                                                                        }
                                                                    });
                                                                }

                                                            }catch (Exception e) {
                                                                Toast.makeText(CardmeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }


                                                    });
                                                }else if(Prevalent.currentOnlineUser.getType().equals("user")){
                                                    final DatabaseReference rootnew = FirebaseDatabase.getInstance().getReference().child("Calls");
                                                    DatabaseReference dnew = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("Calls");
                                                    hashSend.put("number", (int) dataSnapshot.getChildrenCount() + 1);
                                                    callme.put("phone",Prevalent.currentOnlineUser.getPhone());
                                                    hashSend.put("saller","");
                                                    callme.put("user",Prevalent.currentOnlineUser.getUsername());
                                                    callme.put("statics","0");
                                                    callme.put("dep",cards.getDep());
                                                    callme.put("saller","");
                                                    callme.put("number", String.valueOf( dataSnapshot.getChildrenCount() + 1));
                                                    DatabaseReference depssa = FirebaseDatabase.getInstance().getReference();
                                                    depssa.child("depsfatora").child(cards.getDep()).child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme);
                                                    DatabaseReference newro = FirebaseDatabase.getInstance().getReference().child("callsover");
                                                    newro.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme);
                                                    callmeover.child("callsover").child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(callme);
                                                    dnew.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(hashSend);
                                                    rootnew.child(String.valueOf((int) dataSnapshot.getChildrenCount() + 1)).updateChildren(hashSend).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            Toast.makeText(CardmeActivity.this, "تم ارسال الفاتورة بنجاح", Toast.LENGTH_SHORT).show();

                                                            try {
                                                                root.child(Prevalent.currentOnlineUser.getPhone()).child("orders");
                                                                for( final String str : check_code) {
                                                                    root.child(str).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            check_code.clear();
                                                                            check_count.clear();
                                                                            check_dep.clear();
                                                                            check_number.clear();
                                                                            check_name.clear();
                                                                            check_price.clear();
                                                                            check_unit.clear();
                                                                            check_total_price.clear();
                                                                            overtotal = 0;
                                                                            TextView ttfatora = findViewById(R.id.price_total_fatora);
                                                                            ttfatora.setText("المبلغ الكلي : " + overtotal + "$");
                                                                            sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                                                                            for(final String del : check_id){
                                                                                DatabaseReference deletes = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                                                                deletes.child("checked").child(del).removeValue();
                                                                            }
                                                                            mess.dismiss();
                                                                            check_id.clear();
                                                                            final DatabaseReference checkdep = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
                                                                            checkdep.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    if (!dataSnapshot.child("checked").exists()){
                                                                                        checkdep.child("checkdep").removeValue();
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                            Intent goe = new Intent(CardmeActivity.this,HomeActivity.class);
                                                                            startActivity(goe);

                                                                        }
                                                                    });
                                                                }

                                                            }catch (Exception e) {
                                                                Toast.makeText(CardmeActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }


                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        sendd.dismiss();
                                    }
                                });
                                no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendd.dismiss();
                                    }
                                });
                                //-------

                                //-----------------------------------

                            }



                        } catch (Exception e) {

                            Toast.makeText(CardmeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });

               // holder_me.checkBox.setChecked(true);


                //-------------------


                holder_me.price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CardmeActivity.this,cards.price+ "$", Toast.LENGTH_SHORT).show();
                    }
                });

                String j = cards.getStatices();
                    if(j.equals("yes")) {
                        holder_me.check.setImageResource(R.drawable.ic_check);
                        holder_me.statcs.setText("محدد");
                        holder_me.slep.setEnabled(false);
                        holder_me.plus.setEnabled(false);
                        holder_me.delete.setEnabled(false);
                        if(check_code.contains(cards.getCode())){
                            check_code.remove(cards.getCode());
                            check_name.remove(cards.getName());
                            check_price.remove(cards.getPricepost());
                            check_total_price.remove(cards.getPrice());
                            check_dep.remove(cards.getDep());
                            check_unit.remove(cards.getMaintype());
                            check_id.remove(cards.getId());
                            check_num.remove(cards.getNum());
                            check_count.remove(String.valueOf(cards.getCount()));
                            check_code.add(cards.getCode());
                            check_name.add(cards.getName());
                            check_price.add(cards.getPricepost());
                            check_dep.add(cards.getDep());
                            check_unit.add(cards.getMaintype());
                            check_id.add(cards.getId());
                            check_total_price.add(cards.getPrice());
                            check_num.add(cards.getNum());
                            check_count.add(String.valueOf(cards.getCount()));
                            sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                        }else {
                            check_code.add(cards.getCode());
                            check_name.add(cards.getName());
                            check_price.add(cards.getPricepost());
                            check_dep.add(cards.getDep());
                            check_id.add(cards.getId());
                            check_total_price.add(cards.getPrice());
                            check_unit.add(cards.getMaintype());
                            check_num.add(cards.getNum());
                            check_count.add(String.valueOf(cards.getCount()));
                            if(cards.getPricepost().contains(".")){
                                Double d = Double.parseDouble(cards.getPricepost());
                                Double oneType1 = (Double) (d * cards.getCount());
                                overtotal += oneType1;
                                TextView ttfatora = findViewById(R.id.price_total_fatora);
                                ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                                totally = ttfatora.getText().toString().trim();
                                holder_me.slep.setEnabled(true);
                                if (cards.getCount() == 1) {
                                    holder_me.slep.setEnabled(false);
                                }
                                sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                            }else {
                                int oneType1 = Integer.valueOf(cards.getPricepost()) * cards.getCount();
                                overtotal += oneType1;
                                TextView ttfatora = findViewById(R.id.price_total_fatora);
                                ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                                totally = ttfatora.getText().toString().trim();
                                holder_me.slep.setEnabled(true);
                                if (cards.getCount() == 1) {
                                    holder_me.slep.setEnabled(false);
                                }
                                sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                            }


                        }

                    }else if(j.equals("no")) {
                        holder_me.check.setImageResource(R.drawable.ic_check_not);
                        holder_me.statcs.setText("غير محدد");
                        holder_me.slep.setEnabled(true);
                        holder_me.plus.setEnabled(true);
                        holder_me.delete.setEnabled(true);
                        if(check_code.contains(cards.getCode())) {
                            check_code.remove(cards.getCode());
                            check_name.remove(cards.getName());
                            check_price.remove(cards.getPricepost());
                            check_dep.remove(cards.getDep());
                            check_id.remove(cards.getId());
                            check_unit.remove(cards.getMaintype());
                            check_total_price.remove(cards.getPrice());
                            check_num.remove(cards.getNum());
                            check_count.remove(String.valueOf(cards.getCount()));
                           if(cards.getPricepost().contains(".")){
                               Double d = Double.parseDouble(cards.getPricepost());
                               Double oneType1 = (Double) (d * cards.getCount());
                               overtotal -= oneType1;
                               TextView ttfatora = findViewById(R.id.price_total_fatora);
                               ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                               totally = ttfatora.getText().toString().trim();
                               holder_me.plus.setEnabled(true);
                               sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                               holder_me.slep.setEnabled(true);
                               if (cards.getCount() == 1) {
                                   holder_me.slep.setEnabled(false);
                               }
                               sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                           }else {
                               int oneType1 = Integer.valueOf(cards.getPricepost()) * cards.getCount();
                               overtotal -= oneType1;
                               TextView ttfatora = findViewById(R.id.price_total_fatora);
                               ttfatora.setText("المبلغ الكلي : " + String.valueOf(overtotal) + "$");
                               totally = ttfatora.getText().toString().trim();
                               holder_me.plus.setEnabled(true);
                               sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                               holder_me.slep.setEnabled(true);
                               if (cards.getCount() == 1) {
                                   holder_me.slep.setEnabled(false);
                               }
                               sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                           }
                        }

                }



                holder_me.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String j = cards.getStatices();
                        if(j.equals("no")) {
                            holder_me.check.setImageResource(R.drawable.ic_check);
                            holder_me.statcs.setText("محدد");
                            holder_me.slep.setEnabled(false);
                            holder_me.plus.setEnabled(false);
                            holder_me.delete.setEnabled(false);
                            root.child(cards.getCode()).child("statices").setValue("yes");
                            if(check_code.contains(cards.getCode())){
                                check_code.remove(cards.getCode());
                                check_name.remove(cards.getName());
                                check_price.remove(cards.getPricepost());
                                check_dep.remove(cards.getDep());
                                check_id.remove(cards.getId());
                                check_unit.remove(cards.getMaintype());
                                check_num.remove(cards.getNum());
                                check_count.remove(String.valueOf(cards.getCount()));
                                check_total_price.remove(cards.getPrice());
                                check_code.add(cards.getCode());
                                check_name.add(cards.getName());
                                check_price.add(cards.getPricepost());
                                check_dep.add(cards.getDep());
                                check_total_price.add(cards.getPrice());
                                check_unit.add(cards.getMaintype());
                                check_id.add(cards.getId());
                                check_num.add(cards.getNum());
                                numOrder +=1;
                                check_number.add(String.valueOf(numOrder));
                                check_count.add(String.valueOf(cards.getCount()));


                            }else {
                                check_code.add(cards.getCode());
                                check_name.add(cards.getName());
                                check_price.add(cards.getPricepost());
                                check_dep.add(cards.getDep());
                                check_unit.add(cards.getMaintype());
                                check_num.add(cards.getNum());
                                check_id.add(cards.getId());
                                check_total_price.add(cards.getPrice());
                                check_count.add(String.valueOf(cards.getCount()));
                               if(cards.getPricepost().contains(".")){
                                   Double d = Double.parseDouble(cards.getPricepost());
                                   Double oneType1 = (Double) (d * cards.getCount());
                                   overtotal += oneType1;
                                   TextView ttfatora = findViewById(R.id.price_total_fatora);
                                   ttfatora.setText("المبلغ الكلي : " + (overtotal) + "$");
                                   totally = ttfatora.getText().toString().trim();
                                   sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                               }else {
                                   int oneType1 = (int) (Integer.parseInt(cards.getPricepost()) * cards.getCount());
                                   overtotal += oneType1;
                                   TextView ttfatora = findViewById(R.id.price_total_fatora);
                                   ttfatora.setText("المبلغ الكلي : " + (overtotal) + "$");
                                   totally = ttfatora.getText().toString().trim();
                                   sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");
                               }
                            }

                        }else if(j.equals("yes")) {
                            holder_me.check.setImageResource(R.drawable.ic_check_not);
                            holder_me.statcs.setText("غير محدد");
                            holder_me.slep.setEnabled(true);
                            holder_me.plus.setEnabled(true);
                            holder_me.delete.setEnabled(true);
                            root.child(cards.getCode()).child("statices").setValue("no");
                            if(check_code.contains(cards.getCode())){
                                check_code.remove(cards.getCode());
                                check_name.remove(cards.getName());
                                check_price.remove(cards.getPricepost());
                                check_id.remove(cards.getId());
                                check_dep.remove(cards.getDep());
                                check_unit.remove(cards.getMaintype());
                                check_num.remove(cards.getNum());
                                check_total_price.remove(cards.getPrice());
                                check_count.remove(String.valueOf(cards.getCount()));
                                   Double d = Double.parseDouble(cards.getPricepost());
                                   double oneType1 = (double) (d * cards.getCount());
                                   overtotal -= oneType1;
                                   TextView ttfatora = findViewById(R.id.price_total_fatora);
                                   ttfatora.setText("المبلغ الكلي : " + (overtotal) + "$");
                                   totally = ttfatora.getText().toString().trim();
                                   sent.setText("ارسال الفاتورة " + System.lineSeparator() + "(" + check_code.size() +"/" + String.valueOf(adapter.getItemCount()) +  ")");

                            }

                        }


                    }
                });


                    if(adapter.getItemCount() == check_code.size()){
                        okok = true;
                        checkBox.setChecked(true);
                    }else {
                        okok = false;
                        checkBox.setChecked(false);
                    }
            }

            @NonNull
            @Override
            public holder_me onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cardme, parent, false);
                return new holder_me(view);
            }
        };




    }

    public static  class  holder_me extends RecyclerView.ViewHolder{

        private ImageView delete   , img_card , check;
        private TextView name, price , statcs;

        private TextView plus , slep , numberall , maintype , dep;
        public holder_me(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.infocard_name);
            delete = itemView.findViewById(R.id.delete_card);
            price = itemView.findViewById(R.id.infocard_price);
            img_card = itemView.findViewById(R.id.img_cardme);
            plus = itemView.findViewById(R.id.pluse_new);
            slep = itemView.findViewById(R.id.salp_new);
            numberall = itemView.findViewById(R.id.new_number);
            maintype = itemView.findViewById(R.id.card_one);
            dep   = itemView.findViewById(R.id.dep_card);
            check = itemView.findViewById(R.id.selectstatic);
            statcs = itemView.findViewById(R.id.statcsok);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(adapter.getItemCount() != 0){
                    recyclerView.smoothScrollToPosition(adapter.getItemCount());
                    dialogload.dismiss();
                }else {
                    dialogload.dismiss();
                }
            }

        }.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent ba = new Intent(this,HomeActivity.class);
        startActivity(ba);
    }
}

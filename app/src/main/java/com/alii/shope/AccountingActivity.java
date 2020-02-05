package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AccountingActivity extends AppCompatActivity {

    private String idme;
    private ProgressDialog dialog ;
    private int total;
    private Double total_doubal = 0.0;
    private Boolean dones = true;
    private TextView total_madeen , total_daan , total_full;
    private int m , d  , full;
    private Double m_double  = 0.0 , d_double = 0.0;
    private DatabaseReference root;
    private String m_text , d_text;
    private FirebaseRecyclerOptions<accountingadapter> options;
    private FirebaseRecyclerAdapter<accountingadapter, d_holder> adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setTitle("تحميل");
        dialog.setMessage("جاري التحميل");
        dialog.show();
        setContentView(R.layout.activity_accounting);
        total_full = findViewById(R.id.ac_total_full);
        total_daan = findViewById(R.id.ac_total_daan);
        total_madeen = findViewById(R.id.ac_total_maden);
        TextView id = findViewById(R.id.accounting_id);
        TextView user = findViewById(R.id.accounting_name);
        id.setText("رقم الحساب : " + Prevalent.currentOnlineUser.getPhone());
        user.setText("الأسم : " + Prevalent.currentOnlineUser.getUsername());
        ImageView back = findViewById(R.id.back_accounting);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idme = Prevalent.currentOnlineUser.getPhone();
        root = FirebaseDatabase.getInstance().getReference().child("ItemsAccEntry");
        Query query = root.orderByChild("Account_ID").startAt(idme).endAt(idme + "\uf8ff");
        recyclerView = findViewById(R.id.re_accounting);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        options = new FirebaseRecyclerOptions.Builder<accountingadapter>().setQuery(query, accountingadapter.class).build();
        adapter = new FirebaseRecyclerAdapter<accountingadapter, d_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final d_holder d_holder, int i, @NonNull final accountingadapter accountingadapter) {
              DatabaseReference root2 = FirebaseDatabase.getInstance().getReference().child("AccountingEntry");
              root2.child(accountingadapter.getAccEntry_ID()).addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      d_holder.note.setText(dataSnapshot.child("AccEntry_Type").getValue().toString());
                      String string = dataSnapshot.child("AccEntry_Date").getValue().toString();
                      String[] parts = string.split("2020");
                      String part1 = parts[0]; // 004
                      String part2 = parts[1];
                      d_holder.time.setText(part1 + "2020");
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });

                  if(adapter.getItemCount() >0){
                      recyclerView.smoothScrollToPosition(0);
                      new CountDownTimer(2000, 1000) {


                          public void onTick(long millisUntilFinished) {

                          }

                          public void onFinish() {
                              int half = adapter.getItemCount() / 2;
                              recyclerView.smoothScrollToPosition(half);
                              new CountDownTimer(3000, 1000) {


                                  public void onTick(long millisUntilFinished) {

                                  }

                                  public void onFinish() {
                                      recyclerView.smoothScrollToPosition(adapter.getItemCount());
                                      dialog.dismiss();
                                      pluse();

                                      try{
                                          if(dones){
                                              if(TextUtils.isEmpty(accountingadapter.getDebit())){
                                                  Double ncridit = Double.parseDouble(accountingadapter.getCridet());
                                                  d_holder.price.setText(String.valueOf(total_doubal -= ncridit));
                                                  d_double += ncridit;

                                              }else if (TextUtils.isEmpty(accountingadapter.getCridet())){
                                                  Double nDipite = Double.parseDouble(accountingadapter.getDebit());
                                                  d_holder.price.setText(String.valueOf(total_doubal += nDipite));
                                                  m_double += nDipite;
                                              }
                                              total_full.setText(String.valueOf((m_double - d_double)));
                                              total_daan.setText(String.valueOf( d_double));
                                              total_madeen.setText(String.valueOf(m_double));
                                          }

                                      }catch (Exception e){
                                          Toast.makeText(AccountingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                      }
                                  }

                              }.start();

                          }

                      }.start();
                  }
                  recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                      @Override
                      public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                          return false;
                      }

                      @Override
                      public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                          dones = false;
                          Toast.makeText(AccountingActivity.this, "ok", Toast.LENGTH_SHORT).show();
                      }

                      @Override
                      public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                          dones = false;
                          Toast.makeText(AccountingActivity.this, "ok", Toast.LENGTH_SHORT).show();
                      }
                  });
                  /*

                if(TextUtils.isEmpty(accountingadapter.getDebit())){
                    Double ncridit = Double.parseDouble(accountingadapter.getCridet());
                    d_holder.price.setText(String.valueOf(total_doubal -= ncridit));
                    d_double += ncridit;

                }else if (TextUtils.isEmpty(accountingadapter.getCridet())){
                    Double nDipite = Double.parseDouble(accountingadapter.getDebit());
                    d_holder.price.setText(String.valueOf(total_doubal += nDipite));
                    m_double += nDipite;
                }
                total_full.setText(String.valueOf((double) m_double - d_double));
                total_daan.setText(String.valueOf((double) d_double));
                total_madeen.setText(String.valueOf((double) m_double)); */

                d_holder.maden.setText(accountingadapter.getDebit());
                d_holder.daan.setText(accountingadapter.getCridet());
            }
            @NonNull
            @Override
            public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_accounting,parent,false);
                return new d_holder(view);
            }
        };
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);




    }

    private  void pluse() {
                new CountDownTimer(2000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        dones = false;
                        Toast.makeText(AccountingActivity.this, "تم التحميل", Toast.LENGTH_SHORT).show();
                    }

                }.start();

            }

    private class d_holder extends RecyclerView.ViewHolder{

        private TextView time , maden , daan , price , note;
        public d_holder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.ac_time);
            maden = itemView.findViewById(R.id.ac_mden);
            daan = itemView.findViewById(R.id.ac_daan);
            price = itemView.findViewById(R.id.ac_price);
            note = itemView.findViewById(R.id.ac_paian);

        }
    }


}

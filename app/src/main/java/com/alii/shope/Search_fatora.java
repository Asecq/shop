package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Search_fatora extends AppCompatActivity {
    private DatabaseReference rootPro;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Calls> options;
    private FirebaseRecyclerAdapter<Calls, d_holder> adapter;
    private ProgressDialog dialog;
    private TextView res , back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fatora);
        res = findViewById(R.id.resuleno_title);
        back = findViewById(R.id.back_to_fatora);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Search_fatora.this,myorders.class);
                startActivity(intent);

            }
        });
        rootPro = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("callsover");
        recyclerView = findViewById(R.id.re_searchfatora);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setPadding(0,0,0,0);
        String text1  = getIntent().getExtras().getString("text").toString();
        search(text1);
        dialog = new ProgressDialog(this);
        dialog.setTitle("البحث");
        dialog.setMessage("جاري البحث ...");
        dialog.show();

    }
    void search(String text) {
        Query query = rootPro.orderByChild("number").equalTo(Integer.valueOf(text));
        options = new FirebaseRecyclerOptions.Builder<Calls>().setQuery(query, Calls.class).build();
        adapter = new FirebaseRecyclerAdapter<Calls, d_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final d_holder d_holder, final int i, @NonNull final Calls dons) {

                d_holder.code.setText(String.valueOf(dons.getNumber()));
                d_holder.note.setText(dons.getNote());
                d_holder.time.setText(dons.getTime());
                d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Search_fatora.this,AboutActivity.class);
                        intent.putExtra("key",String.valueOf(dons.getNumber()));
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fatora,parent,false);
                return new d_holder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(adapter.getItemCount() == 0) {
                    res.setText("حدث خطأ او ليس هناك نتائج مطابقة");
                    res.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }else {

                    dialog.dismiss();
                }

            }

        }.start();

    }
    public static class d_holder extends RecyclerView.ViewHolder {

        private TextView time  , code  , note;
        private CardView cardView;



        public d_holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_fattora);
            code = itemView.findViewById(R.id.num_fatoraorder);
            note = itemView.findViewById(R.id.note_fatoraorder);
            time = itemView.findViewById(R.id.data_fatoraorder);

        }
    }
}

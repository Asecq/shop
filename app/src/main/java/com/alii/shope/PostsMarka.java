package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class PostsMarka extends AppCompatActivity {
    private DatabaseReference rootPro;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Protucts> options;
    private FirebaseRecyclerAdapter<Protucts, d_holder> adapter;
    String key ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_marka);
        TextView depar_name = (TextView)findViewById(R.id.markes_name);
        String key = getIntent().getExtras().getString("key");
        depar_name.setText(key);
        rootPro = FirebaseDatabase.getInstance().getReference().child("MainPage");
        recyclerView = findViewById(R.id.re_items_marke);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setPadding(0,0,0,0);

        //=======
        Query query = rootPro.orderByChild("marke").startAt(key).endAt(key + "\uf99f");
        options = new FirebaseRecyclerOptions.Builder<Protucts>().setQuery(query,Protucts.class).build();
        adapter = new FirebaseRecyclerAdapter<Protucts, d_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final d_holder d_holder, int i, @NonNull final Protucts protucts) {

                d_holder.post_title.setText(protucts.getPname());
                if(protucts.getMaimtype().equals("قطعة")){
                    d_holder.post_price.setText(protucts.getPprice()+ " $");
                }
                if(protucts.getMaimtype().equals("كارتونة")){
                    d_holder.post_price.setText(protucts.getTesttow()+ " $");
                }
                if(protucts.getMaimtype().equals("درزن")){
                    d_holder.post_price.setText(protucts.getTestone()+ " $");
                }
                if(protucts.getMaimtype().equals("سيت")){
                    d_holder.post_price.setText(protucts.getTestthree()+ " $");
                }
                d_holder.main.setText(protucts.getMaimtype());
                Picasso.get().load(protucts.getImage()).into(d_holder.post_image);
                d_holder.post_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent getpost = new Intent( PostsMarka.this,PostActivity.class);
                        getpost.putExtra("title_post",protucts.getPname());
                        getpost.putExtra("price",protucts.getPprice());
                        getpost.putExtra("img_post",protucts.getImage());
                        getpost.putExtra("code_post",protucts.getPcode());
                        getpost.putExtra("model_post",protucts.getPmodel());
                        getpost.putExtra("des_post",protucts.getDescrption());
                        getpost.putExtra("local_post",protucts.getLoaction());
                        getpost.putExtra("countt" , protucts.getCount());
                        getpost.putExtra("dep",protucts.getCategory());
                        getpost.putExtra("dep_post",protucts.getCategory());
                        getpost.putExtra("data_post",protucts.getPdata());
                        getpost.putExtra("typeP",protucts.getTypeP());
                        getpost.putExtra("idpost",protucts.getPid());
                        getpost.putExtra("maintype" , protucts.getMaimtype());
                        String p1 = protucts.getTestone();
                        String p2 = protucts.getTesttow();
                        String p3 = protucts.getTestthree();
                        getpost.putExtra("pp",p1);
                        getpost.putExtra("ppp",p2);
                        getpost.putExtra("pppp",p3);
                        startActivity(getpost);
                    }
                });
                d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent getpost = new Intent(PostsMarka.this,PostActivity.class);
                                getpost.putExtra("title_post",protucts.getPname());
                                getpost.putExtra("price",protucts.getPprice());
                                getpost.putExtra("img_post",protucts.getImage());
                                getpost.putExtra("code_post",protucts.getPcode());
                                getpost.putExtra("model_post",protucts.getPmodel());
                                getpost.putExtra("des_post",protucts.getDescrption());
                                getpost.putExtra("local_post",protucts.getLoaction());
                                getpost.putExtra("countt" , protucts.getCount());
                                getpost.putExtra("dep_post",protucts.getCategory());
                                getpost.putExtra("data_post",protucts.getPdata());
                                getpost.putExtra("maintype" , protucts.getMaimtype());
                                getpost.putExtra("typeP",protucts.getTypeP());
                                getpost.putExtra("dep",protucts.getCategory());
                                getpost.putExtra("idpost",protucts.getPid());
                                String p1 = protucts.getTestone();
                                String p2 = protucts.getTesttow();
                                getpost.putExtra("price1",p1);
                                getpost.putExtra("price2",p2);
                                startActivity(getpost);
                            }
                        });
                    }
                });
            }

            @NonNull
            @Override
            public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);

                return new d_holder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    public static class d_holder extends RecyclerView.ViewHolder {

        private TextView post_title  , post_price , main ;
        private ImageView post_image;
        private CardView cardView;

        public d_holder(@NonNull View itemView) {
            super(itemView);

            post_title = itemView.findViewById(R.id.post_name);
            post_price = itemView.findViewById(R.id.post_price);
            post_image = itemView.findViewById(R.id.post_image);
            cardView = itemView.findViewById(R.id.cardview1);
            main = itemView.findViewById(R.id.postmaintype);

        }
    }
}


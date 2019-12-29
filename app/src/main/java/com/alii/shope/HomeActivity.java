package com.alii.shope;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.alii.shope.Prevalent.Prevalent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference rootPro;
    private RecyclerView recyclerView;
    private String typeuser;
    private TextView count_sala;
    ArrayList<String> ok = new ArrayList<>();
    private FirebaseRecyclerOptions<Protucts> options;
    private FirebaseRecyclerAdapter<Protucts,v_holder> adapter;
    private ListView mListView;
    ArrayList<String> searches = new ArrayList<>();
    private  FloatingActionButton cardbutton;
    private ProgressDialog mes;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private String offlinelike = "https://firebasestorage.googleapis.com/v0/b/shope-11637.appspot.com/o/online%2Fman-user.png?alt=media&token=a9fa66de-1a03-4188-a2d6-fcfed02cb172";
    private String onlinelike = "https://firebasestorage.googleapis.com/v0/b/shope-11637.appspot.com/o/online%2Fman-user%20(1).png?alt=media&token=8e4eb21c-4afa-4be0-8e4f-7ac2e7466e69";
    private DatabaseReference online;
    private DatabaseReference searchFirebase;
    private GridLayoutManager manager;
    MyRecyclerViewAdapter adapter2;
    private RelativeLayout lay_1 , lay_2 , lay_3,lay_4;
    private TextView overtext ;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        typeuser = Prevalent.currentOnlineUser.getType().toString();
        mes = new ProgressDialog(this);
        deps();
        count_sala = findViewById(R.id.sala_count);
        lay_1 = findViewById(R.id.lay_1);
        lay_2 = findViewById(R.id.lay_2);
        lay_3 = findViewById(R.id.lay_3);
        lay_4 = findViewById(R.id.lay_4);
        lay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typeuser.equals("user")){
                    Intent card = new Intent(HomeActivity.this,CardmeActivity.class);
                    startActivity(card);
                }else if (typeuser.equals("saller")){
                    Intent carda = new Intent(HomeActivity.this,usersforselectActivity.class);
                    startActivity(carda);
                }
            }
        });
        lay_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orders = new Intent(HomeActivity.this,myorders.class);
                startActivity(orders);
            }
        });
        lay_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orders = new Intent(HomeActivity.this,DepsActivity.class);
                startActivity(orders);
            }
        });
        lay_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog ds = new Dialog(HomeActivity.this);
                ds.setTitle("تبليغ");
                ds.setContentView(R.layout.senttoserver);
                ds.show();
                Button okbtn = ds.findViewById(R.id.dialog_okkk);
                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ds.dismiss();
                    }
                });

            }
        });
        searchFirebase = FirebaseDatabase.getInstance().getReference();
       /* cardbutton = findViewById(R.id.floatingCard);cardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent c = new Intent(HomeActivity.this,CardmeActivity.class);
                startActivity(c);
            }
        });
        */
     /*   overtext = findViewById(R.id.over_ordertext);
        DatabaseReference rootcount = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
        rootcount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                overtext.setText(String.valueOf(dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


// ========================================================

        rootPro = FirebaseDatabase.getInstance().getReference().child("MainPage");
        online =FirebaseDatabase.getInstance().getReference().child("Users");
        recyclerView = findViewById(R.id.ad_postt);
        recyclerView.setHasFixedSize(true);
        manager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setPadding(0,0,0,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Prevalent.currentOnlineUser.getUsername());
        Paper.init(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View holderView = navigationView.getHeaderView(0);
        TextView username_profile = holderView.findViewById(R.id.infouser_name);
        final TextView pass_profile = holderView.findViewById(R.id.infouser_pass);
        TextView phone_profile = holderView.findViewById(R.id.infouser_phone);
        CircleImageView image_profile = holderView.findViewById(R.id.profile_img);


        online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(onlinelike);

        options = new FirebaseRecyclerOptions.Builder<Protucts>().setQuery(rootPro,Protucts.class).build();
        adapter = new FirebaseRecyclerAdapter<Protucts, v_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull v_holder v_holder, int i, @NonNull final Protucts protucts) {
                DatabaseReference rootcount = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
                rootcount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getChildrenCount() == 0) {
                            count_sala.setText("");
                        }else
                        count_sala.setText(String.valueOf("(" + dataSnapshot.getChildrenCount() + ")"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                v_holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            Intent getpost = new Intent(HomeActivity.this,PostActivity.class);
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
                        }catch (Exception e){
                            Toast.makeText(HomeActivity.this, "Erorr", Toast.LENGTH_SHORT).show();
                        }



                    }
                });

                v_holder.booktitle.setText(protucts.getPname());
                Picasso.get().load(protucts.getImage()).into(v_holder.bookimag);
                Glide.with(HomeActivity.this).load(protucts.getImage()).thumbnail( 0.5f )
                        .override( 200, 200 )
                        .placeholder(R.drawable.giphy)
                        .diskCacheStrategy( DiskCacheStrategy.ALL )
                        .into(v_holder.bookimag);

            }

            @NonNull
            @Override
            public v_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_row,parent,false);

                return new v_holder(view);
            }
        };



        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {


            }
        });


    }


    @Override
    protected void onRestart() {
        DatabaseReference rootcount = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
        rootcount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0) {
                    count_sala.setText("");
                }else
                    count_sala.setText(String.valueOf("(" + dataSnapshot.getChildrenCount() + ")"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.searchhome);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.matches("")){
                    options = new FirebaseRecyclerOptions.Builder<Protucts>().setQuery(rootPro,Protucts.class).build();
                    adapter = new FirebaseRecyclerAdapter<Protucts, v_holder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final v_holder v_holder, int i, @NonNull final Protucts protucts) {
                            DatabaseReference rootcount = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
                            rootcount.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount() == 0) {
                                        count_sala.setText("");
                                    }else
                                        count_sala.setText(String.valueOf("(" + dataSnapshot.getChildrenCount() + ")"));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            v_holder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try{
                                        Intent getpost = new Intent(HomeActivity.this,PostActivity.class);
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
                                    }catch (Exception e){
                                        Toast.makeText(HomeActivity.this, "Erorr", Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                            v_holder.booktitle.setText(protucts.getPname());

                            Picasso.get().load(protucts.getImage()).into(v_holder.bookimag);
                            Glide.with(HomeActivity.this).load(protucts.getImage()).thumbnail( 0.5f )
                                    .override( 200, 200 )
                                    .placeholder(R.drawable.giphy)
                                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                                    .into(v_holder.bookimag);

                            searches.clear();
                        }

                        @NonNull
                        @Override
                        public v_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_row,parent,false);

                            return new v_holder(view);
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else {
                    fireSearch(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.matches("")){
                    options = new FirebaseRecyclerOptions.Builder<Protucts>().setQuery(rootPro,Protucts.class).build();
                    adapter = new FirebaseRecyclerAdapter<Protucts, v_holder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull v_holder v_holder, int i, @NonNull final Protucts protucts) {
                            DatabaseReference rootcount = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone()).child("orders");
                            rootcount.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getChildrenCount() == 0) {
                                        count_sala.setText("");
                                    }else
                                        count_sala.setText(String.valueOf("(" + dataSnapshot.getChildrenCount() + ")"));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            v_holder.cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try{
                                        Intent getpost = new Intent(HomeActivity.this,PostActivity.class);
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
                                    }catch (Exception e){
                                        Toast.makeText(HomeActivity.this, "Erorr", Toast.LENGTH_SHORT).show();
                                    }



                                }
                            });
                            v_holder.booktitle.setText(protucts.getPname());
                            Picasso.get().load(protucts.getImage()).into(v_holder.bookimag);
                            Glide.with(HomeActivity.this).load(protucts.getImage()).thumbnail( 0.5f )
                                    .override( 200, 200 )
                                    .placeholder(R.drawable.giphy)
                                    .diskCacheStrategy( DiskCacheStrategy.ALL )
                                    .into(v_holder.bookimag);

                            searches.clear();
                        }

                        @NonNull
                        @Override
                        public v_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_row,parent,false);

                            return new v_holder(view);
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }else {
                    fireSearch(newText);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Paper.book().destroy();
            Intent logintn = new Intent(HomeActivity.this,MainActivity.class);
            logintn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logintn);
            finish();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

@Override
protected void onStart() {
    super.onStart();
    online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(onlinelike);
    recyclerView.setAdapter(adapter);
    adapter.startListening();
    adapter.notifyDataSetChanged();


}



    @Override
    protected void onDestroy() {
        super.onDestroy();
        online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(offlinelike);
    }

    @Override
    protected void onStop() {
        super.onStop();
        online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(offlinelike);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
         if(id == R.id.nav_category){
             Intent to = new Intent(HomeActivity.this,DepsActivity.class);
             startActivity(to);
             online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(onlinelike);
        }else if(id == R.id.nav_account) {
            Intent intentse = new Intent(HomeActivity.this,userActivity.class);
            intentse.putExtra("username",Prevalent.currentOnlineUser.getUsername());
            intentse.putExtra("pass",Prevalent.currentOnlineUser.getPass());
            intentse.putExtra("phone",Prevalent.currentOnlineUser.getPhone());
            startActivity(intentse);
             online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(onlinelike);

        }else if(id == R.id.nav_logout){

            Paper.book().destroy();
            Intent logintn = new Intent(HomeActivity.this,MainActivity.class);
            logintn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logintn);
            finish();
        }else if (id == R.id.markates){
             Intent mar = new Intent(HomeActivity.this,MarkasActivity.class);
             startActivity(mar);
         }

         else if (id == R.id.nav_call) {
             String phone = "009647712710192";
             Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
             startActivity(intent);
         }
         else if(id == R.id.nav_about) {
             Intent about = new Intent(HomeActivity.this,AboutActivity.class);
             startActivity(about);
         }else if(id == R.id.cardmehome) {
            if(typeuser.equals("user")){
                Intent card = new Intent(HomeActivity.this,CardmeActivity.class);
                startActivity(card);
            }else if (typeuser.equals("saller")){
                Intent carda = new Intent(HomeActivity.this,usersforselectActivity.class);
                startActivity(carda);
            }

         }else if(id == R.id.ordermenav) {
             Intent nav = new Intent(HomeActivity.this,myorders.class);
             startActivity(nav);
         }


        return true;
    }
    public static class v_holder extends RecyclerView.ViewHolder {

        private TextView post_title , post_dis , post_price , post_time,post_data,post_lo;
        private ImageView post_image;
        private CardView cardView;
        private ImageView bookimag;
        private  TextView booktitle;

        public v_holder(@NonNull View itemView) {
            super(itemView);

           // post_title = itemView.findViewById(R.id.post_name);
           // post_dis = itemView.findViewById(R.id.post_des);
            post_price = itemView.findViewById(R.id.post_price);
           // post_image = itemView.findViewById(R.id.post_image);
            cardView = itemView.findViewById(R.id.card_itemrow);
           booktitle = itemView.findViewById(R.id.title_book);
           bookimag = itemView.findViewById(R.id.image_book);


        }
    }
    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<String> mData;
        private LayoutInflater mInflater;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.row_dep, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final String animal = mData.get(position);
            holder.myTextView.setText(animal);
            holder.myTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent depinten = new Intent(HomeActivity.this,DepitemActivity.class);
                    depinten.putExtra("dapName",animal);
                    startActivity(depinten);
                    online.child(Prevalent.currentOnlineUser.getPhone()).child("online").setValue(onlinelike);
                }
            });

        }


        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView myTextView;
            CardView card_row1;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.tvAnimalName);
                card_row1 = itemView.findViewById(R.id.card_itemrow);

            }
        }

    }
    public  void  deps(){
        final ArrayList<String> animalNames = new ArrayList<>();

        // set up the RecyclerView

        RecyclerView recyclerView1 = findViewById(R.id.re_deps);
        LinearLayoutManager manager2 = new LinearLayoutManager(this , RecyclerView.HORIZONTAL,false);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(manager2);
        recyclerView1.setNestedScrollingEnabled(true);
        adapter2 = new MyRecyclerViewAdapter(HomeActivity.this, animalNames);
        recyclerView1.setAdapter(adapter2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Protucts");
        final List<String> keys = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(final DataSnapshot snapshot : dataSnapshot.getChildren()){


                    keys.add(snapshot.getKey());


                }

                animalNames.addAll(keys);
                adapter2.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fireSearch(final String textSearch){
        String query = textSearch.toLowerCase();

                           Query firebasese = rootPro.orderByChild("pname").startAt(query).endAt(query + "\uf8ff");
                           options = new FirebaseRecyclerOptions.Builder<Protucts>().setQuery(firebasese,Protucts.class).build();
                           adapter = new FirebaseRecyclerAdapter<Protucts, v_holder>(options) {
                               @Override
                               protected void onBindViewHolder(@NonNull v_holder v_holder, int i, @NonNull final Protucts protucts) {
                                   v_holder.cardView.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           try{
                                               Intent getpost = new Intent(HomeActivity.this,PostActivity.class);
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
                                           }catch (Exception e){
                                               Toast.makeText(HomeActivity.this, "Erorr", Toast.LENGTH_SHORT).show();
                                           }



                                       }
                                   });
                                   v_holder.booktitle.setText(protucts.getPname());
                                   Picasso.get().load(protucts.getImage()).into(v_holder.bookimag);
                                   Glide.with(HomeActivity.this).load(protucts.getImage()).thumbnail( 0.5f )
                                           .override( 200, 200 )
                                           .placeholder(R.drawable.giphy)
                                           .diskCacheStrategy( DiskCacheStrategy.ALL )
                                           .into(v_holder.bookimag);


                               }

                               @NonNull
                               @Override
                               public v_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_row,parent,false);

                                   return new v_holder(view);
                               }
                           };

                           recyclerView.setAdapter(adapter);
                           adapter.startListening();



    }


}

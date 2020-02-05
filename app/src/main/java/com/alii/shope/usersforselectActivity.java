package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Locale;

public class usersforselectActivity extends AppCompatActivity {
    private DatabaseReference rootPro;
    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<testuser> options;
    private FirebaseRecyclerAdapter<testuser,d_holder> adapter;
    private EditText search;
    private Query query;
    private ImageButton btn_voice;
    private SpeechRecognizer mspeech;
    private Intent mSpeechIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersforselect);
        search = findViewById(R.id.search_users);
        btn_voice = findViewById(R.id.voice_btn);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},1);
        mspeech = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE , Locale.getDefault());

        mspeech.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                if(matches != null){
                    search.setText(matches.get(0));
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        recyclerView = findViewById(R.id.re_users);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        btn_voice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        search.setHint("البحث عن زبون ...");
                        mspeech.stopListening();
                        break;

                    case MotionEvent.ACTION_DOWN:
                        search.setText("");
                        search.setHint("تكلم الأن ...");
                        mspeech.startListening(mSpeechIntent);
                        break;
                }



                return false;
            }
        });
        rootPro = FirebaseDatabase.getInstance().getReference().child("zpoon");
        options = new FirebaseRecyclerOptions.Builder<testuser>().setQuery(rootPro,testuser.class).build();
        adapter = new FirebaseRecyclerAdapter<testuser, d_holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final d_holder d_holder, int i, @NonNull final testuser testuser) {
               d_holder.id.setText(testuser.getPhone());
               d_holder.user.setText(testuser.getUsername());

               d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                     Intent intent = new Intent(usersforselectActivity.this,CardmeActivity.class);
                     intent.putExtra("id",testuser.getPhone().toString());
                     intent.putExtra("username",testuser.getUsername().toString());
                     startActivity(intent);
                   }
               });

            }

            @NonNull
            @Override
            public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_users,parent,false);
                return new d_holder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(s.toString().matches("")){
                   options = new FirebaseRecyclerOptions.Builder<testuser>().setQuery(rootPro,testuser.class).build();
                   adapter = new FirebaseRecyclerAdapter<testuser, d_holder>(options) {
                       @Override
                       protected void onBindViewHolder(@NonNull d_holder d_holder, int i, @NonNull final testuser testuser) {
                           d_holder.id.setText(testuser.getPhone());
                           d_holder.user.setText(testuser.getUsername());

                           d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intent = new Intent(usersforselectActivity.this,CardmeActivity.class);
                                   intent.putExtra("id",testuser.getPhone().toString());
                                   intent.putExtra("username",testuser.getUsername().toString());
                                   startActivity(intent);
                               }
                           });

                       }

                       @NonNull
                       @Override
                       public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                           View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_users,parent,false);
                           return new d_holder(view);
                       }
                   };
                  recyclerView.setAdapter(adapter);
                  adapter.startListening();
               }else {

                   Query q1 = rootPro.orderByChild("username").startAt(s.toString()).endAt(s.toString() + "\uf99f");
                   options = new FirebaseRecyclerOptions.Builder<testuser>().setQuery(q1,testuser.class).build();
                   adapter = new FirebaseRecyclerAdapter<testuser, d_holder>(options) {
                       @Override
                       protected void onBindViewHolder(@NonNull d_holder d_holder, int i, @NonNull final testuser testuser) {
                           d_holder.id.setText(testuser.getPhone());
                           d_holder.user.setText(testuser.getUsername());

                           d_holder.cardView.setOnClickListener(new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   Intent intent = new Intent(usersforselectActivity.this,CardmeActivity.class);
                                   intent.putExtra("id",testuser.getPhone().toString());
                                   intent.putExtra("username",testuser.getUsername().toString());
                                   startActivity(intent);
                               }
                           });

                       }

                       @NonNull
                       @Override
                       public d_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                           View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.row_users,parent,false);
                           return new d_holder(view);
                       }
                   };
                   recyclerView.setAdapter(adapter);
                   adapter.startListening();
               }


            }
        });
    }
    private static class d_holder extends RecyclerView.ViewHolder{

        TextView user , id ;
        CardView cardView;
        public d_holder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_zpon);
            user = itemView.findViewById(R.id.user_zpon);
            cardView = itemView.findViewById(R.id.card_users);

        }
    }


}

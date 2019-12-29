package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Model.Users;
import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class welcomeActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;
    private LinearLayout dotsliner ;
    private TextView[] mDots;
    private TextView back , next , login , about;
    private ProgressDialog loading;
    private int mnumber ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewPager = findViewById(R.id.v_pager);
        dotsliner = findViewById(R.id.dotsliner);
        sliderAdapter = new SliderAdapter(this);
        loading = new ProgressDialog(this);
        Paper.init(this);
        viewPager.setAdapter(sliderAdapter);
        back = findViewById(R.id.btn_back_pager);
        next = findViewById(R.id.btn_next_pager);
        login = findViewById(R.id.go_to_login);
        about = findViewById(R.id.about_text);
        addDots(0);
        viewPager.addOnPageChangeListener(viewpagerr);
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                loading.setTitle("تسجيل دخول تلقائي ...");
                loading.setMessage("الرجاء الأنتظار .....");
                loading.setCanceledOnTouchOutside(false);
                loading.show();
            }
        }



    }
    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(phone).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone))
                    {
                        if (usersData.getPass().equals(password))
                        {
                            Toast.makeText(welcomeActivity.this, "الرجأء الأنتظار ...", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                            Intent intent = new Intent(welcomeActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(welcomeActivity.this, "كلمة السر غير صحيحة.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(welcomeActivity.this, "هذا الحساب " + phone + "غير موجود .", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
            String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);
            if (UserPhoneKey != "" && UserPasswordKey != "")
            {
                if (!TextUtils.isEmpty(UserPhoneKey)  &&  !TextUtils.isEmpty(UserPasswordKey))
                {
                    AllowAccess(UserPhoneKey, UserPasswordKey);
                    loading.setTitle("تسجيل دخول");
                    loading.setMessage("الرجاء الأنتظار .....");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();
                }
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public  void addDots( int position){
        mDots = new TextView[3];

        dotsliner.removeAllViews();
        for(int i = 0; i<mDots.length;i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));
            dotsliner.addView(mDots[i]);
        }
        if (mDots.length > 0 ){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(mnumber - 1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(mnumber + 1);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(welcomeActivity.this,LoginActivity.class);
                log.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(log);
            }
        });


    }


    ViewPager.OnPageChangeListener viewpagerr = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            mnumber = position;
            if (mnumber == 0) {
                next.setText("التالي");
                next.setEnabled(true);
                back.setEnabled(false);
                next.setVisibility(View.VISIBLE);
                back.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                login.setEnabled(false);
                about.setEnabled(true);
                about.setVisibility(View.VISIBLE);
                back.setText("");
            }else if(mnumber == 1) {
                next.setText("التالي");
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                back.setText("رجوع");
                about.setVisibility(View.INVISIBLE);
                login.setVisibility(View.INVISIBLE);
                about.setEnabled(false);
                login.setEnabled(false);
            }else if(mnumber == 2) {
                next.setText("");
                next.setEnabled(false);
                back.setEnabled(true);
                next.setVisibility(View.INVISIBLE);
                back.setText("رجوع");
                login.setEnabled(true);
                login.setVisibility(View.VISIBLE);
                about.setVisibility(View.INVISIBLE);
                about.setEnabled(false);

            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

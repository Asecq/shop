package com.alii.shope;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.alii.shope.Model.Users;
import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
  public static String FACEBOOK_PAGE_ID = "pagename";
  public static String FACEBOOK_URL = "https://www.facebook.com/pagename";
  private Button Join;
  private Button Login;
  ImageView facebook;
  ImageView instagram;
  /* access modifiers changed from: private */
  public ProgressDialog loading;
  ImageView telegram;

  /* access modifiers changed from: protected */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView((int) R.layout.activity_main);
    this.telegram = (ImageView) findViewById(R.id.telegram);
    this.instagram = (ImageView) findViewById(R.id.instagram);
    this.facebook = (ImageView) findViewById(R.id.facebook);
    this.instagram.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        String str = "http://instagram.com/deve_alii";
        String str2 = "android.intent.action.VIEW";
        Intent likeIng = new Intent(str2, Uri.parse(str));
        likeIng.setPackage("com.instagram.android");
        try {
          MainActivity.this.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
          MainActivity.this.startActivity(new Intent(str2, Uri.parse(str)));
        }
      }
    });
    this.telegram.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.telegram.me/tophollywoodmovieshd")));
      }
    });
    this.facebook.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        Intent facebookIntent = new Intent("android.intent.action.VIEW");
        MainActivity mainActivity = MainActivity.this;
        facebookIntent.setData(Uri.parse(mainActivity.getFacebookPageURL(mainActivity)));
        MainActivity.this.startActivity(facebookIntent);
      }
    });
    this.Login = (Button) findViewById(R.id.main_login);
    this.loading = new ProgressDialog(this);
    Paper.init(this);
    this.Login.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        MainActivity.this.startActivity(new Intent(MainActivity.this, LoginActivity.class));
      }
    });
    String UserPhoneKey = (String) Paper.book().read(Prevalent.UserPhoneKey);
    String UserPasswordKey = (String) Paper.book().read(Prevalent.UserPasswordKey);
    String str = "";
    if (UserPhoneKey != str && UserPasswordKey != str && !TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
      AllowAccess(UserPhoneKey, UserPasswordKey);
      this.loading.setTitle("Already Logged in");
      this.loading.setMessage("Please wait.....");
      this.loading.setCanceledOnTouchOutside(false);
      this.loading.show();
    }
  }

  private void AllowAccess(final String phone, final String password) {
    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String str = "Users";
        if (dataSnapshot.child(str).child(phone).exists()) {
          Users usersData = (Users) dataSnapshot.child(str).child(phone).getValue(Users.class);
          if (!usersData.getPhone().equals(phone)) {
            return;
          }
          if (usersData.getPass().equals(password)) {
            Toast.makeText(MainActivity.this, "Please wait, you are already logged in...",Toast.LENGTH_LONG).show();
            MainActivity.this.loading.dismiss();
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            Prevalent.currentOnlineUser = usersData;
            MainActivity.this.startActivity(intent);
            return;
          }
          MainActivity.this.loading.dismiss();
          Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_LONG).show();
          return;
        }
        MainActivity mainActivity = MainActivity.this;
        StringBuilder sb = new StringBuilder();
        sb.append("Account with this ");
        sb.append(phone);
        sb.append(" number do not exists.");
        Toast.makeText(mainActivity, sb.toString(), Toast.LENGTH_LONG).show();
        MainActivity.this.loading.dismiss();
      }

      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }

  public void onBackPressed() {
    super.onBackPressed();
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode != 4) {
      return super.onKeyDown(keyCode, event);
    }
    String UserPhoneKey = (String) Paper.book().read(Prevalent.UserPhoneKey);
    String UserPasswordKey = (String) Paper.book().read(Prevalent.UserPasswordKey);
    String str = "";
    if (UserPhoneKey != str && UserPasswordKey != str && !TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
      AllowAccess(UserPhoneKey, UserPasswordKey);
      this.loading.setTitle("Already Logged in");
      this.loading.setMessage("Please wait.....");
      this.loading.setCanceledOnTouchOutside(false);
      this.loading.show();
    }
    return true;
  }

  public String getFacebookPageURL(Context context) {
    try {
      if (context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode >= 3002850) {
        StringBuilder sb = new StringBuilder();
        sb.append("fb://facewebmodal/f?href=");
        sb.append(FACEBOOK_URL);
        return sb.toString();
      }
      StringBuilder sb2 = new StringBuilder();
      sb2.append("fb://page/");
      sb2.append(FACEBOOK_PAGE_ID);
      return sb2.toString();
    } catch (NameNotFoundException e) {
      return FACEBOOK_URL;
    }
  }
}

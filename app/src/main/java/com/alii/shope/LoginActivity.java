package com.alii.shope;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.alii.shope.Model.Users;
import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
  /* access modifiers changed from: private */
  public String ParentdbUser = "Users";
  /* access modifiers changed from: private */
  public TextView btn_admin;
  /* access modifiers changed from: private */
  public TextView btn_not_admin;
  private CheckBox checkBox;
  /* access modifiers changed from: private */
  public ProgressDialog loading;
  /* access modifiers changed from: private */
  public Button login_btn;
  private EditText pass_log;
  private EditText phone_log;

  /* access modifiers changed from: protected */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView((int) R.layout.activity_login);
    this.btn_admin = (TextView) findViewById(R.id.admin_login);
    this.btn_not_admin = (TextView) findViewById(R.id.not_admin_login);
    this.login_btn = (Button) findViewById(R.id.login_btn);
    this.phone_log = (EditText) findViewById(R.id.phone_login);
    this.pass_log = (EditText) findViewById(R.id.pass_login);
    this.loading = new ProgressDialog(this);
    this.checkBox = (CheckBox) findViewById(R.id.remimber_mee);
    Paper.init(this);
    this.btn_admin.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        LoginActivity.this.ParentdbUser = "Admins";
        LoginActivity.this.btn_not_admin.setVisibility(View.INVISIBLE);
        LoginActivity.this.login_btn.setText("�������� ��������");
      }
    });
    this.btn_not_admin.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        LoginActivity.this.ParentdbUser = "Users";
        LoginActivity.this.login_btn.setText("���������� ��������");
      }
    });
    this.login_btn.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        LoginActivity.this.loginAccount();
      }
    });
  }

  /* access modifiers changed from: private */
  public void loginAccount() {
    String phone = this.phone_log.getText().toString();
    String password = this.pass_log.getText().toString();
    if (TextUtils.isEmpty(phone)) {
      Toast.makeText(this, "�������� �������������� ...", Toast.LENGTH_LONG).show();
    } else if (TextUtils.isEmpty(password)) {
      Toast.makeText(this, "�������� �������� �������� ...", Toast.LENGTH_LONG).show();
    } else {
      this.loading.setTitle("Login");
      this.loading.setMessage("���������� ...");
      this.loading.setCanceledOnTouchOutside(false);
      this.loading.show();
      checkLogin(phone, password);
    }
  }

  private void checkLogin(final String phone, final String password) {
    if (this.checkBox.isChecked()) {
      Paper.book().write(Prevalent.UserPhoneKey, phone);
      Paper.book().write(Prevalent.UserPasswordKey, password);
    }
    FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.child(LoginActivity.this.ParentdbUser).child(phone).exists()) {
          final Users usersData = (Users) dataSnapshot.child(LoginActivity.this.ParentdbUser).child(phone).getValue(Users.class);
          if (!usersData.getPhone().equals(phone)) {
            return;
          }
          if (!usersData.getPass().equals(password)) {
            LoginActivity.this.loading.dismiss();
            Toast.makeText(LoginActivity.this, "كلمة السر غير صحيحة ...", Toast.LENGTH_LONG).show();
          } else if (LoginActivity.this.ParentdbUser == "Users") {
            LoginActivity.this.loading.dismiss();
            DatabaseReference checkuser = FirebaseDatabase.getInstance().getReference().child("Users");
            checkuser.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("type").getValue().equals("saller")){
                  Toast.makeText(LoginActivity.this, "تسجيل دخول مندوب", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                  intent.putExtra("type","saller");
                  Prevalent.currentOnlineUser = usersData;
                  LoginActivity.this.startActivity(intent);
                }else if(dataSnapshot.child("type").getValue().equals("user")){
                  Toast.makeText(LoginActivity.this, "تسجيل دخول زبون", Toast.LENGTH_LONG).show();
                  Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                  intent.putExtra("type","user");
                  Prevalent.currentOnlineUser = usersData;
                  LoginActivity.this.startActivity(intent);
                }

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
            });
          }
        } else {
          Toast.makeText(LoginActivity.this, "هناك خطأ في تسجيل الدخول", Toast.LENGTH_LONG).show();
          LoginActivity.this.loading.dismiss();
        }
      }

      public void onCancelled(@NonNull DatabaseError databaseError) {
      }
    });
  }
}

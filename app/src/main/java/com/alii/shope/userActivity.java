package com.alii.shope;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class userActivity extends AppCompatActivity {
  private Button btn;
  private EditText pass;
  private EditText phone;
  private EditText user;

  /* access modifiers changed from: protected */
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView((int) R.layout.activity_user);
    String username = getIntent().getExtras().getString("username");
    String password = getIntent().getExtras().getString("pass");
    String Phone = getIntent().getExtras().getString("phone");
    this.user = (EditText) findViewById(R.id.infouser_name);
    this.pass = (EditText) findViewById(R.id.infouser_pass);
    this.phone = (EditText) findViewById(R.id.infouser_phone);
    this.user.setFocusable(false);
    this.pass.setFocusable(false);
    this.phone.setFocusable(false);
    this.user.setText(username);
    this.pass.setText(password);
    this.phone.setText(Phone);
    this.btn = (Button) findViewById(R.id.back_home);
    this.btn.setOnClickListener(new OnClickListener() {
      public void onClick(View view) {
        userActivity.this.startActivity(new Intent(userActivity.this, HomeActivity.class));
      }
    });
  }
}

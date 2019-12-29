package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    //Vars for Register
    private Button btn_register;
    private EditText input_name , input_phone , input_pass;
    private ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_register = (Button)findViewById(R.id.register_btn);
        input_name = (EditText)findViewById(R.id.name_register);
        input_phone = (EditText)findViewById(R.id.phone_register);
        input_pass = (EditText)findViewById(R.id.pass_register);
        loading = new ProgressDialog(this);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creatAccount();
            }
        });
    }

    private void creatAccount() {
        String name = input_name.getText().toString();
        String phone = input_phone.getText().toString();
        String password = input_pass.getText().toString();
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "اكتب الاسم", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "اكتب رقم الهاتف", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "اكتب كلمة السر", Toast.LENGTH_SHORT).show();
        }else{
            loading.setTitle("انشاء حساب");
            loading.setCanceledOnTouchOutside(false);
            loading.setMessage("الرجاءالأنتظار ...");
            loading.show();
            VaidataPhoneNumber(name,phone,password);

        }


    }

    private void VaidataPhoneNumber(final String name, final String phone, final String password) {
    final DatabaseReference rootRf;
    rootRf = FirebaseDatabase.getInstance().getReference();
    rootRf.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(!dataSnapshot.child("Users").child(phone).exists()){

                HashMap<String ,Object> userdata = new HashMap<>();
                userdata.put("Name",name);
                userdata.put("Phone",phone);
                userdata.put("Password",password);
                rootRf.child("Users").child(phone).updateChildren(userdata)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "تم انشاء الحساب", Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                   // Intent intent = new Intent(RegisterActivity.this,CpanalActivity.class);
                                    //startActivity(intent);
                                }else {
                                    loading.dismiss();
                                    Toast.makeText(RegisterActivity.this, "خطأ في الشبكة حاول في وقت لاحق", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }else {
                loading.dismiss();
                Toast.makeText(RegisterActivity.this, "رقم الهاتف موجود ...", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}

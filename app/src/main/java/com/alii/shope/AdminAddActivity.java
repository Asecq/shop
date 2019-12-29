package com.alii.shope;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;
import java.util.logging.SimpleFormatter;

import javax.xml.transform.Result;

import static com.alii.shope.R.layout.support_simple_spinner_dropdown_item;

public class AdminAddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String CategoryName, Description, Price, Pname, saveCurrentDate, saveCurrentTime,Pcode , Pmodel , location , count , Price1,Price2;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName,  InputProductPrice,InputModel,InputCode , Inputdes , Inputcount , InputLocal , InputPrice1,InputPrice2;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    private String data_price1,data_price2,data_price3;
    private CheckBox checkBox , checkbox1 ,checkbox2,checkbox3;
    private Spinner spinner;
    private String type_price;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);
//<Spinner item>====================================================
        Spinner spinner = findViewById(R.id.sp_pirce);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dolars, support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.spiner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //-------------------------
        CategoryName = getIntent().getExtras().getString("department");
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Protucts");
        checkBox = findViewById(R.id.add_to_home);
        checkbox1 = findViewById(R.id.check_price1);
        checkbox2  = findViewById(R.id.check_price2);
        checkbox3 = findViewById(R.id.check_price3);
        AddNewProductButton = (Button) findViewById(R.id.btn_add_pro);
        InputProductImage = (ImageView) findViewById(R.id.image_pro);
        InputProductName = (EditText) findViewById(R.id.text_pro);
        InputProductPrice = (EditText) findViewById(R.id.text_price);
        InputPrice1 = (EditText)findViewById(R.id.text_price2);
        InputPrice2 = (EditText)findViewById(R.id.text_price3);
        InputCode = (EditText)findViewById(R.id.text_code);
        InputModel = (EditText)findViewById(R.id.text_model);
        InputLocal = (EditText)findViewById(R.id.text_local1);
        Inputcount = (EditText)findViewById(R.id.text_count1) ;
        Inputdes = (EditText)findViewById(R.id.text_des);
        loadingBar = new ProgressDialog(this);

        //=============
        InputPrice1.setFocusable(false);
        InputPrice2.setFocusable(false);

        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(checkbox1.isChecked()){
                InputProductPrice.setFocusableInTouchMode(true);

            }else {
                InputProductPrice.setFocusable(false);
            }
            }
        });
        checkbox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkbox2.isChecked()){
                    InputPrice1.setFocusableInTouchMode(true);

                }else {
                    InputPrice1.setFocusable(false);
                }
            }
        });
        checkbox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkbox3.isChecked()){
                    InputPrice2.setFocusableInTouchMode(true);

                }else {
                    InputPrice2.setFocusable(false);
                }
            }
        });

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                OpenGallery();
            }
        });


        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProductData();
            }
        });
    }


    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }


    private void ValidateProductData()
    {

        Pname = InputProductName.getText().toString();
        Pcode = InputCode.getText().toString();
        Pmodel = InputModel.getText().toString();
        Description = Inputdes.getText().toString();
        location = InputLocal.getText().toString();
        count = Inputcount.getText().toString();
        if(checkbox1.isChecked()){
            data_price1 = InputProductPrice.getText().toString().trim();
        }
        if(checkbox2.isChecked()) {
            data_price2 = InputPrice1.getText().toString().trim();
        }
        if(checkbox3.isChecked()){
            data_price3 = InputPrice2.getText().toString().trim();
        }


        if (ImageUri == null)
        {

            Toast.makeText(this, "ارفق صورة", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(data_price1) && TextUtils.isEmpty(data_price2) && TextUtils.isEmpty(data_price3)){
            Toast.makeText(this, "ادخل سعر واحد على الأقل", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(count)) {
            Toast.makeText(this, "اكتب عدد المنتجات", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "اكتب المحافظة", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "اكتب وصف للمنتج", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "اكتب اسم المنتج", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Pcode)){
            Toast.makeText(this, "ادخل الرمز", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(Pmodel)){
            Toast.makeText(this, "ادخل الصنع", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }



    private void StoreProductInformation()
    {
        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddActivity.this, "Erorr : " + message, Toast.LENGTH_LONG).show();
                loadingBar.dismiss();
                finish();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }



    private void SaveProductInfoToDatabase()
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("pdata", saveCurrentDate);
        productMap.put("ptime", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryName);
        productMap.put("pprice", data_price1);
        productMap.put("pname", Pname);
        productMap.put("Pcode",Pcode);
        productMap.put("Pmodel",Pmodel);
        productMap.put("Descrption",Description);
        productMap.put("loaction" , location);
        productMap.put("count" , count);
        productMap.put("price2",data_price2);
        productMap.put("price3" , data_price3);
        productMap.put("typeP",type_price);


        if(checkBox.isChecked()) {
            if(CategoryName != "الصفحة الرئيسية") {
                HashMap<String, Object> productMaphome = new HashMap<>();
                productMaphome.put("pid", productRandomKey);
                productMaphome.put("pdata", saveCurrentDate);
                productMaphome.put("ptime", saveCurrentTime);
                productMaphome.put("image", downloadImageUrl);
                productMaphome.put("category", CategoryName);
                productMaphome.put("pprice", data_price1);
                productMaphome.put("pname", Pname);
                productMaphome.put("Descrption",Description);
                productMaphome.put("Pcode",Pcode);
                productMaphome.put("Pmodel",Pmodel);
                productMaphome.put("loaction" , location);
                productMaphome.put("count" , count);
                productMap.put("price2",data_price2);
                productMap.put("price3" , data_price3);
                productMaphome.put("timestamp",ts);
                productMap.put("typeP",type_price);

                ProductsRef.child("الصفحة الرئيسية").child(productRandomKey).updateChildren(productMaphome);
            }

        }
        ProductsRef.child(CategoryName).child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddActivity.this, SelectActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddActivity.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Long text = adapterView.getItemIdAtPosition(i);

      
       if(text == 0){
          type_price = "$";
       }else {
           type_price = "IQD";
       }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

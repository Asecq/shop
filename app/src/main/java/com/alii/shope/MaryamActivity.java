package com.alii.shope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MaryamActivity extends AppCompatActivity {
    TextView number;
    int number_order ;
    Button plus , salep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maryam);

        int birth = 19;

        if(birth == 20) {
            System.out.println("كل عام وانتي بالف خير حبيبتي مريم");
        }
        Toast.makeText(this, "يارب نبقى نحتفل بعيد ميلادج للأبد", Toast.LENGTH_SHORT).show();
        for(int i = 0;i<1000;i++) {
            String out = "سنة واحنه سوه" + i;
            System.out.println(out);
            //الى احلى مبرمجة بالكون كله
            // ويارب عيد ميلاد سعيد وعقبال الف سنة واحنه سوه
        }

    }
}

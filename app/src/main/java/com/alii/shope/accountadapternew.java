package com.alii.shope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alii.shope.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class accountadapternew extends BaseAdapter {
    private Context context;
    private Double plus = 0.0;
    private Double slep = 0.0;
    private Double result = 0.0;
    private Boolean check;

    private ArrayList<accountingadapter> mobiles;
    public accountadapternew(Context context, ArrayList<accountingadapter> mobiles , Boolean check) {
        this.context = context;
        this.mobiles = mobiles;
        this.check = check;
    }


    @Override
    public int getCount() {
       return mobiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mobiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View) inflater.inflate(
                    R.layout.row_accounting, null);
        }
        final TextView time , maden , daan , price , note;
        time = convertView.findViewById(R.id.ac_time);
        maden = convertView.findViewById(R.id.ac_mden);
        daan = convertView.findViewById(R.id.ac_daan);
        price = convertView.findViewById(R.id.ac_price);
        note = convertView.findViewById(R.id.ac_paian);
        maden.setText(mobiles.get(position).getDebit());
        daan.setText(mobiles.get(position).getCridet());

       /* Double pluse  = Double.parseDouble(mobiles.get(position).getDebit());
        Double salp  = Double.parseDouble(mobiles.get(position).getCridet());
*/
       try{

           final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Results");
           final HashMap<String , Object> hashMap = new HashMap<>();


               reference.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       if(dataSnapshot.child("check").getValue().equals("ok")){
                           check = false;
                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

               if(check){
                   final String user = mobiles.get(position).getAccount_ID();
                   if(mobiles.get(position).getDebit().length() > 0){
                       Double nDipite = Double.parseDouble(mobiles.get(position).getDebit());
                       price.setText(String.valueOf(result += nDipite));

                   }else if(mobiles.get(position).getCridet().length() > 0){
                       Double nDipite = Double.parseDouble(mobiles.get(position).getCridet());
                       price.setText(String.valueOf(result -= nDipite));
                   }

               }

       }catch (Exception e){
           Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
       }

        return convertView;


    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

package com.alii.shope;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;


    SliderAdapter(Context context) {
        this.context = context;
    }


    //====== Images
    public int[] images = {
            R.drawable.tower,
            R.drawable.image3,
            R.drawable.image2
    };

    //====== String title
    public  String[] titles = {


            "سوق الشورجة الألكتروني" ,
            "التسويق",
            "ابدأ الأن"
    };

    //====== String des
    public  String[] dess = {
            "مرحبا بك في متجر سوق الشورجة الألكتروني",
            "أمتلك محفظة في حسابك لمنتجاتنا",
            "سجل دخول في التطبيق اذا كنت تمتلك حساب"
    };

    // ====== Colors Backgroungs


   public int[]colors = {


          Color.rgb(51, 0, 0),
           Color.rgb(100, 0, 0),
           Color.rgb(128, 0, 0)

   };




    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider,container,false);
        LinearLayout linearslider = view.findViewById(R.id.slider_liner);
        ImageView imageView = view.findViewById(R.id.slider_img) ;
        TextView titleText = view.findViewById(R.id.slider_title);
        TextView desText = view.findViewById(R.id.slider_des);
        linearslider.setBackgroundColor(colors[position]);
        imageView.setImageResource(images[position]);
        titleText.setText(titles[position]);
        titleText.setTextColor(Color.WHITE);
        desText.setTextColor(Color.rgb(230, 115, 0));
        desText.setText(dess[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((LinearLayout)object);
    }
}

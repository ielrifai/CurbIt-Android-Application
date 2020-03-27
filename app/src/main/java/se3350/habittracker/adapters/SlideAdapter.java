package se3350.habittracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import se3350.habittracker.R;

/**
 * Welcome Slide Show
**/
public class SlideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context context){
        this.context = context;
    }

    //slide images, headings, text/blurb
    public int[] slide_images={
            R.drawable.brain1,
            R.drawable.brain2,
            R.drawable.brain3
    };

    public String[] slide_headings={
            "ACHIEVE",
            "OVERCOME",
            "SUCCEED"
    };

    public String[] slide_texts={
            "Welcome to CurbIt! It's a revolutionary habit tracking app to help you catalogue, curtail and even curb all sorts of negative habits.",
            "Track your habit using the fool-proof '4 Step Process' developed by American Psychiatrist Dr.Jeffery Schwartz",
            "Kick your bad Habits to the Curb! Let's start by setting up a password."

    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    //set slide items 
    @Override
    public Object instantiateItem(ViewGroup container,int position){

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container,false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideText = (TextView) view.findViewById(R.id.slide_text);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideText.setText(slide_texts[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((ConstraintLayout)object);
    }
}

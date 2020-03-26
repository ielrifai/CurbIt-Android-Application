package se3350.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import se3350.habittracker.R;
import se3350.habittracker.adapters.SlideAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private Button mBackButton;
    private Button mNextButton;

    private TextView[] mDots;

    private SlideAdapter slideAdapter;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);

        mSlideViewPager = (ViewPager) findViewById(R.id.slideviewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        mBackButton = (Button) findViewById(R.id.backButton);
        mNextButton = (Button) findViewById(R.id.nextButton);


        slideAdapter = new SlideAdapter(this);
        mSlideViewPager.setAdapter(slideAdapter);

        //add dots to screen
        addDotsIndicator(0);
        //add listener to page
        mSlideViewPager.addOnPageChangeListener(viewListener);

        //button listeners
        mNextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage + 1);
            }


        });

        //on finish button selected go to create password activity
        mNextButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if(mCurrentPage == mDots.length - 1){
                        Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                return false;
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        //remove previous views - at each event
        mDotsLayout.removeAllViews();

        //set page dots to transparent white when not on page
        for(int i =0; i< mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226",24));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(ContextCompat.getColor(this,R.color.transparentWhite));

            mDotsLayout.addView(mDots[i]);
        }
        //set page dot to white to indicate position
        if(mDots.length > 0){
            mDots[position].setTextColor(ContextCompat.getColor(this,R.color.white));
        }
    }

    //viewpager listener - get slide position
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage = position;
            //enable/set buttons
            //first page
            if(position == 0){
                mNextButton.setEnabled(true);
                mBackButton.setEnabled(false);
                mBackButton.setVisibility(View.INVISIBLE);

                mNextButton.setText(R.string.next);
                mBackButton.setText("");

            }
            //last page
            else if(position == mDots.length - 1){
                mNextButton.setEnabled(true);
                mBackButton.setEnabled(true);
                mBackButton.setVisibility(View.VISIBLE);

                mNextButton.setText(R.string.finish);
                mBackButton.setText(R.string.back);

            }
            else{
                mNextButton.setEnabled(true);
                mBackButton.setEnabled(true);
                mBackButton.setVisibility(View.VISIBLE);

                mNextButton.setText(R.string.next);
                mBackButton.setText(R.string.back);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}

package com.example.cheng.combined_1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       final int[] res={R.drawable.d1,R.drawable.d2,R.drawable.d3,R.drawable.d4,R.drawable.d5,R.drawable.d6,R.drawable.d7,R.drawable.d8,R.drawable.d9,R.drawable.d10};

        MyDragView myDragView=(MyDragView)findViewById(R.id.myDragView);
        myDragView.setAdapter(new MyDragView.MyDragViewAdapter() {
            @Override
            public int getCount() {
                return res.length;
            }

            @Override
            public View getView(int i) {

                ImageView imageview=new ImageView(MainActivity.this);
                imageview.setImageResource(res[i]);
                imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageview.setBackgroundColor(Color.parseColor("#00ff00"));

                return imageview;
            }
        });
    }
}

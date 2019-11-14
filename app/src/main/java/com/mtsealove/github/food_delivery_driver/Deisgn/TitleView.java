package com.mtsealove.github.food_delivery_driver.Deisgn;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.mtsealove.github.food_delivery_driver.IpActivity;
import com.mtsealove.github.food_delivery_driver.R;

public class TitleView extends RelativeLayout {
    Context context;
    String tag = getClass().getSimpleName();
    ImageView  logoIv;

    public TitleView(Context context) {
        super(context);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(tag, String.valueOf(R.layout.view_title));

        View layout = inflater.inflate(R.layout.view_title, TitleView.this, false);
        logoIv=layout.findViewById(R.id.logoIv);
        logoIv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setIP();
                return false;
            }
        });
        addView(layout);
    }

    private void setIP() {
        Intent intent = new Intent(context, IpActivity.class);
        context.startActivity(intent);
    }
}

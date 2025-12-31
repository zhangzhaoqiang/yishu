package com.yishu.net;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.yishu.bluetooth.BltRecycleView;


public class BlueCheckActivity extends AppCompatActivity {

    private BltRecycleView blt_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_bluecheck);
        blt_view=findViewById(R.id.blt_view);
        blt_view.refreshBlt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
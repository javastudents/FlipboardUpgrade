package com.flipboard.upgrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.flipboard.upgrade.view.FlipUpgradeView;

public class MainActivity extends AppCompatActivity {
    private FlipUpgradeView mFlipUpgradeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlipUpgradeView = (FlipUpgradeView) findViewById(R.id.v_flip);
        mFlipUpgradeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlipUpgradeView.startAnimator();
            }
        });
    }
}

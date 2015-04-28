package com.dynamic.plugin.child;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dynamic.load.PluginActivity;

/**
 * Created by Andy Wing on 15-4-22.
 */
public class SecondActivity extends PluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.plugin_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(that, "com.dynamic.plugin.parent.MainActivity");
                startActivity(intent);
            }
        });
    }
}

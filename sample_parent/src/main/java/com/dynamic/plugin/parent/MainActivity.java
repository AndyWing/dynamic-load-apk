package com.dynamic.plugin.parent;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dynamic.load.PluginHostCallback;
import com.dynamic.load.PluginHostCallbackManager;
import com.dynamic.load.PluginManager;

/**
 * Created by Andy Wing on 15-4-22.
 */
public class MainActivity extends Activity {
    private Button mButton;

    private void assignViews() {
        mButton = (Button) findViewById(R.id.button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();

        final PluginManager pm = PluginManager.getInstance(MainActivity.this);
        // TODO change to your apk path
        final String pkg = pm.loadAPkTest();

        PluginHostCallbackManager.getInstance().registerPluginHostCallback(mPluginHostCallback);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pkg)) {
                    Toast.makeText(MainActivity.this, "No Target Apk File", Toast.LENGTH_SHORT).show();
                    return;
                }
                pm.startActivity(MainActivity.this, pkg, ".TargetActivity");

            }
        });
    }

    @Override
    protected void onDestroy() {
        PluginHostCallbackManager.getInstance().unregisterPluginHostCallback();
        super.onDestroy();
    }

    private PluginHostCallback mPluginHostCallback = new PluginHostCallback() {
        @Override
        public String trackSomething() {
            return " from parent";
        }

        @Override
        public void onClick() {
        }
    };
}

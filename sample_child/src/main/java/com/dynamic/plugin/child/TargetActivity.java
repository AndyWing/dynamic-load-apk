package com.dynamic.plugin.child;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dynamic.load.PluginActivity;

/**
 * Created by Andy Wing on 15-4-22.
 */
public class TargetActivity extends PluginActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        assignViews();
    }

    private void assignViews() {
        ImageButton normalView = (ImageButton) findViewById(R.id.normal_view);
        PluginView pluginView = (PluginView) findViewById(R.id.plugin_view);
        final TextView normalViewSet = (TextView) findViewById(R.id.normal_view_set);
        final TextView normalViewInvokeParent = (TextView) findViewById(R.id.normal_view_invoke_parent);

        normalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalViewSet.setText(R.string.set_in_plugin);
            }
        });

        normalViewInvokeParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPluginHostCallback != null) {
                    String parentReturn = mPluginHostCallback.trackSomething();
                    normalViewInvokeParent.setText("ParentReturn>>" + parentReturn);
                }
            }
        });

        pluginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), SecondActivity.class.getName());
                startGhostActivity(intent);
            }
        });

        PluginView pluginViewHost = (PluginView) findViewById(R.id.plugin_view_host);
        pluginViewHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName(that.getPackageName(), "com.dynamic.plugin.parent.MainActivity");
                startActivity(intent);
            }
        });
    }
}

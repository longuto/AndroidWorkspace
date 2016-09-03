package com.longuto.myswitch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {
	private MyswitchView mMyswitchView;	
	private Context context;	// 上下文

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        mMyswitchView = (MyswitchView) findViewById(R.id.msv_diyView);
        mMyswitchView.setOnMyChangedListen(new MyswitchView.OnMyChangedListen() {
			@Override
			public void myChanged(View v, boolean isOpen) {
				if(isOpen) {
					Toast.makeText(context, "开", 0).show();
				}else {					
					Toast.makeText(context, "关", 0).show();
				}
			}
		});
    }
}

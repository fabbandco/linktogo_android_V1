package com.fabbandco.android.link2go.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.android.link2go.R;
import com.fabbandco.common.PrivateFabbandcoActivity;

public class SettingLink2GoActivity extends PrivateFabbandcoActivity implements OnClickListener, OnItemClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	// Page setting
    	Log.d("Page Setting", "on");
    	 if (!PersistanceApplication.getInstance().isConnecte()){
	        	Intent i = new Intent(this,LoginLink2GoActivity.class);
	        	startActivity(i);
	        }else{
	        	majView();
	        }
    	 Log.d("Page Setting", "off");
    	 viewMessagesErrors();
    }
    
    public void callBackAsync() {
    }
   
    private void majView (){
    	setContentView(R.layout.activity_setting_link2_go);
    	Switch swi = (Switch)findViewById(R.id.switchIsConnected);
    	swi.setChecked(PersistanceApplication.getInstance().isConnecte());
    	swi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    		@Override
    		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    			Log.v("Switch State Connected=", ""+isChecked);
    		}       
    		
    	});
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}

	@Override
	public void onClick(View v) {
	}
	
}

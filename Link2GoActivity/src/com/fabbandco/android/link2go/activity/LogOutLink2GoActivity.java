package com.fabbandco.android.link2go.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.common.PrivateFabbandcoActivity;

public class LogOutLink2GoActivity extends PrivateFabbandcoActivity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("LogOutLink2GoActivity", "on");
    	PersistanceApplication.getInstance().setConnecte(false);
		PersistanceApplication.getInstance().setUser(null);
		Intent i =  new Intent(this,LoginLink2GoActivity.class);
		startActivity(i);
		Log.d("LogOutLink2GoActivity", "off");
    }
}

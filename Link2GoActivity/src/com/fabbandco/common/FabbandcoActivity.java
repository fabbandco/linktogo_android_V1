package com.fabbandco.common;

import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.fabbandco.android.application.Link2GoApplication;

public class FabbandcoActivity extends Activity {

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	      
	 }
	 
	  public void viewMessagesErrors(){
			Collection<String> colErreurs = ((Link2GoApplication)this.getApplication()).getColErrors();
			StringBuffer str = new StringBuffer();
			if (!colErreurs.isEmpty()){
				for (String strtemp : colErreurs) {
					str.append(strtemp+"\n");
				}
				Toast message = Toast.makeText(this.getApplicationContext(), str.toString(), Constante.duration_toast);
		    	message.show();
		    	((Link2GoApplication)this.getApplication()).removeErrors();
			}
			
	}
}

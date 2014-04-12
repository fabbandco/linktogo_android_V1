package com.fabbandco.android.link2go.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fabbandco.android.api.SmsReceiver;
import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.android.async.GetMessageAsync;
import com.fabbandco.android.async.ValidateMessageSynchronisationAsync;
import com.fabbandco.android.link2go.R;
import com.fabbandco.android.model.Message;
import com.fabbandco.android.model.Utilisateur;
import com.fabbandco.android.util.StringUtil;
import com.fabbandco.common.Constante;
import com.fabbandco.common.PrivateFabbandcoActivity;

public class Link2GoActivity extends PrivateFabbandcoActivity implements OnClickListener, OnItemClickListener{

	private ArrayList<String> smsList = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d("Link2GoActivity", "on");
    	  if (!PersistanceApplication.getInstance().isConnecte()){
    		  	Intent i = new Intent(this,LoginLink2GoActivity.class);
	        	startActivity(i);
	        }else{
	        	majView();
	        }
    	  viewMessagesErrors();
    	  Log.d("Link2GoActivity", "off");
    }
    
    public void callBackAsync(Utilisateur user) {
    	majView();
	}
   
    private boolean majView (){
    	setContentView(R.layout.activity_link2_go);
    	Toast to = Toast.makeText(this.getApplicationContext(), "Telephone : " + PersistanceApplication.getInstance().getUser().getPhone(), Constante.duration_toast);
		to.show();
    	IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(new SmsReceiver(), filter);
        return true;
    }
    
    public void goTest (View view){
    	Intent i = new Intent(this, LoginLink2GoActivity.class);
    	startActivity(i);
    }
    
    public void onItemClick( AdapterView<?> parent, View view, int pos, long id ) 
	{
		try 
		{
		    String[] splitted = smsList.get( pos ).split("\n"); 
			String sender = splitted[0];
			String encryptedData = "";
			for ( int i = 1; i < splitted.length; ++i )
			{
			    encryptedData += splitted[i];
			}
			String data = sender + "\n" + StringUtil.decrypt(new String(SmsReceiver.PASSWORD), encryptedData );
			Toast.makeText( this, data, Toast.LENGTH_SHORT ).show();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public void onClick(final View v ) 
	{
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query( Uri.parse( "content://sms/inbox" ), null, null, null, null);

		int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
		int indexPerson = cursor.getColumnIndex( SmsReceiver.PERSON );
		int indexDate = cursor.getColumnIndex( SmsReceiver.DATE );
		int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );
		
		if ( indexBody < 0 || !cursor.moveToFirst() ) return;
		
		smsList.clear();
		
		do
		{
			String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
			smsList.add( str );
		}
		while( cursor.moveToNext() );

		
		ListView smsListView = (ListView) findViewById( R.id.SMSList );
		smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
		smsListView.setOnItemClickListener( this );
	}
	
	public void onClickOutbox(final View v ) 
	{
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query( Uri.parse( "content://sms/sent" ), null, null, null, null);

		int indexBody = cursor.getColumnIndex( SmsReceiver.BODY );
		int indexPerson = cursor.getColumnIndex( SmsReceiver.PERSON );
		int indexDate = cursor.getColumnIndex( SmsReceiver.DATE );
		int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );
		
		if ( indexBody < 0 || !cursor.moveToFirst() ) return;
		
		smsList.clear();
		
		do
		{
			String str = "Sent : " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody );
			smsList.add( str );
		}
		while( cursor.moveToNext() );

		
		ListView smsListView = (ListView) findViewById( R.id.SMSList );
		smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsList) );
		smsListView.setOnItemClickListener( this );
	}
	
	public void onClickForceSynchro (final View v){
		GetMessageAsync getAsynchro = new GetMessageAsync(this);
		getAsynchro.execute("");
	}
	
	public void callBackSynchro (final Collection<Message> colMessage){
		Toast.makeText(this.getApplicationContext(),colMessage.size() +" Sms r�cup�r�(s)" , Constante.duration_toast).show();
		
		// On parcours la collection de message � envoy�
		SmsManager manager = SmsManager.getDefault();
		
		for (Iterator iterator = colMessage.iterator(); iterator.hasNext();) {
			Message message = (Message) iterator.next();
			manager.sendTextMessage(message.getNumero(), null, message.getMessage(), null, null);
			ValidateMessageSynchronisationAsync valiAsync = new ValidateMessageSynchronisationAsync(this);
			valiAsync.execute(message.getId()+"");
		}	
	}
	
	
	public void callBackSynchro (final Boolean _isok){
		if(_isok.booleanValue()){
			Toast.makeText(this.getApplicationContext(),"Statut sms mis � jour", Constante.duration_toast).show();
		}else{
			Toast.makeText(this.getApplicationContext(),"Statut sms non mis � jour", Constante.duration_toast).show();
		}
	}
	
	@SuppressLint("NewApi")
	public boolean makeNotification(String titre ,String message) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.cloud_notif)
		.setContentTitle(titre)
		.setContentText(message);
		Intent resultIntent = new Intent(this, Link2GoActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Link2GoActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		mNotificationManager.notify(mId, mBuilder.build());
		return true;
	}
}

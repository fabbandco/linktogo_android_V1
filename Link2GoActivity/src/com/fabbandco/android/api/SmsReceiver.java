package com.fabbandco.android.api;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.android.async.AddMessageAsync;
import com.fabbandco.android.link2go.R;
import com.fabbandco.android.model.SmsMessageCrypt;
import com.fabbandco.android.util.DateUtil;
import com.fabbandco.android.util.MD5Hash;
import com.fabbandco.common.Constante;

public class SmsReceiver extends BroadcastReceiver 
{
	
	private Collection<SmsMessageCrypt> colSmsMessageCrypts = new ArrayList<SmsMessageCrypt>();
	
	/****************************************************************************************
	 * 
	 ***************************************************************************************/
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;
	
    // Change the password here or give a user possibility to change it
    public static final byte[] PASSWORD = new byte[]{ 0x20, 0x32, 0x34, 0x47, (byte) 0x84, 0x33, 0x58 };
    
	public void onReceive(Context context, Intent intent ) 
	{
		// Get SMS map from Intent
        Bundle extras = intent.getExtras();
        Date now = new Date ();
        String pass = MD5Hash.md5Java(now.getTime()+"");
        if (PersistanceApplication.getInstance().isConnecte() ){
        	pass = MD5Hash.md5Java(PersistanceApplication.getInstance().getUser().getMdp()+DateUtil.formatDateWithSecond(now));
        }
        
        if ( extras != null )
        {
        	Collection <SmsMessageCrypt> colSmsAtraiter = new ArrayList<SmsMessageCrypt> ();
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            ContentResolver contentResolver = context.getContentResolver();
            Date date_envoi = now;
            for ( int i = 0; i < smsExtra.length; ++i )
            {
            	// Mise en place du cryptage pour chaque message
            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
            	SmsMessageCrypt smsCrypt = new SmsMessageCrypt(sms, false, sms.getOriginatingAddress(),pass ,sms.getMessageBody().toString(), contentResolver,date_envoi);
            	// Ajout pour la liste à traiter
            	colSmsAtraiter.add(smsCrypt);
            }
            
            // Envoi des Messages 
            if (PersistanceApplication.getInstance().isConnecte()){
        		for (SmsMessageCrypt object : colSmsAtraiter) {
        			// Envoi des messages à traiter reçu maintenant
            		if (!object.isOk() && !object.isActif()){
	            		AddMessageAsync addMess = new AddMessageAsync(this);
	            		object.setActif(true);
	            		addMess.execute(URLEncoder.encode(object.getStrPass()+""),object.getStrCrypte(),URLEncoder.encode(object.getSms().getOriginatingAddress()+""),URLEncoder.encode(object.getSms().getOriginatingAddress()+""), DateUtil.formatDateWithSecond(object.getDate_envoi()));
            		}
				}
        		for (SmsMessageCrypt object : getColSmsMessageCrypts()) {
            		if (!object.isOk() && !object.isActif()){
            			// Envoi des messages à traiter en attente de retraitement
	            		AddMessageAsync addMess = new AddMessageAsync(this);
	            		object.setActif(true);
	            		addMess.execute(URLEncoder.encode(object.getStrPass()+""),object.getStrCrypte(),URLEncoder.encode(object.getNumero()+""),URLEncoder.encode(object.getNumero()+""), DateUtil.formatDateWithSecond(object.getDate_envoi()));
            		}
				}
        		
        	}else{
        		// S'il l'application n'est pas connecté, on l'ajoute à la liste en reserve
        		getColSmsMessageCrypts().addAll(colSmsAtraiter);
        	}
         }
	}
	
	
	
	public void callBackAsync (final SmsMessageCrypt _smsCrypte){
			Toast to = null;
			if (_smsCrypte.isOk()){
				majColSmsMessageCrypts(_smsCrypte.getDate_envoi().getTime());
				to = Toast.makeText(PersistanceApplication.getInstance().getCurrentApplication(), R.string.label_message_save, Constante.duration_toast);
			}else{
				majColSmsMessageCrypts(_smsCrypte.getDate_envoi().getTime());
				// Mise en place dans la liste de retraitement si application non connecté ou sms en erreur
				getColSmsMessageCrypts().add(_smsCrypte);
				to = Toast.makeText(PersistanceApplication.getInstance().getCurrentApplication(), getColSmsMessageCrypts() + " à traiter", Constante.duration_toast);
			}
			to.show();
	}
	
	 /***********************************************************************************************
	  * Private
	  **********************************************************************************************/
	private synchronized Collection<SmsMessageCrypt> getColSmsMessageCrypts() {
		return colSmsMessageCrypts;
	}
	
	private synchronized void  setColSmsMessageCrypts(Collection<SmsMessageCrypt> newColSms) {
		this.colSmsMessageCrypts=newColSms;
	}
	
	private synchronized void majColSmsMessageCrypts (final long _key){
		Collection<SmsMessageCrypt> colOut = new ArrayList<SmsMessageCrypt>();
		
		for (SmsMessageCrypt smsMessageCrypt : this.getColSmsMessageCrypts()) {
			if (!(smsMessageCrypt.getDate_envoi().getTime()==_key)){
				colOut.add(smsMessageCrypt);
			}
		}
		
		this.setColSmsMessageCrypts(colOut);
	}
}

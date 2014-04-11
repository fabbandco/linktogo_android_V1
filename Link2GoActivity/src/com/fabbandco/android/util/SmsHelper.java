package com.fabbandco.android.util;

import com.fabbandco.android.model.SmsMessageCrypt;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsMessage;

public abstract class SmsHelper {

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
    
	
	private static void putSmsToDatabaseSend(final ContentResolver contentResolver, final SmsMessage sms, final String encryptedPassword )
	{
		ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getOriginatingAddress() );
        values.put( DATE, sms.getTimestampMillis());
        values.put( READ, MESSAGE_IS_NOT_READ ); 
        values.put( STATUS, sms.getStatus() );
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
       	values.put( BODY, encryptedPassword );
        contentResolver.insert(Uri.parse( SMS_URI ), values );
	}
	
	private static void putSmsToDatabaseSent(final SmsMessageCrypt sms)
	{
		ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getNumero() );
        values.put( DATE, sms.getDate_envoi().getTime());
        values.put( READ, MESSAGE_IS_NOT_READ ); 
//        values.put( STATUS, "");
        values.put( TYPE, MESSAGE_TYPE_SENT);
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
       	values.put( BODY, sms.getStrCrypte());
        sms.getContentResolver().insert(Uri.parse( SMS_URI ), values);
	}
}

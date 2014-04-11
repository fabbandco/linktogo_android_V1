package com.fabbandco.android.model;

import java.util.Date;

import android.content.ContentResolver;
import android.telephony.SmsMessage;

public class SmsMessageCrypt extends Entity {

		private SmsMessage sms;
		private boolean isOk = false;
		private String numero;
		
		private String strPass = "";
		private String strCrypte = "";
		private ContentResolver contentResolver;
		private Date date_envoi;
		
		
		public SmsMessageCrypt(SmsMessage sms, boolean isOk, String numero,String strPass, String strCrypte, ContentResolver contentResolver, Date date_envoi) {
			super();
			this.sms = sms;
			this.isOk = isOk;
			this.numero = numero;
			this.strPass = strPass;
			this.strCrypte = strCrypte;
			this.contentResolver = contentResolver;
			this.date_envoi = date_envoi;
		}


		/*****GETSETTER***********************************************************************/
		
		public SmsMessage getSms() {
			return sms;
		}


		public void setSms(SmsMessage sms) {
			this.sms = sms;
		}


		public boolean isOk() {
			return isOk;
		}


		public void setOk(boolean isOk) {
			this.isOk = isOk;
		}


		public String getStrCrypte() {
			return strCrypte;
		}


		public void setStrCrypte(String strCrypte) {
			this.strCrypte = strCrypte;
		}


		public ContentResolver getContentResolver() {
			return contentResolver;
		}
		
		public void setContentResolver(ContentResolver contentResolver) {
			this.contentResolver = contentResolver;
		}

		public String getStrPass() {
			return strPass;
		}

		public void setStrPass(String strPass) {
			this.strPass = strPass;
		}

		public Date getDate_envoi() {
			return date_envoi;
		}

		public void setDate_envoi(Date date_envoi) {
			this.date_envoi = date_envoi;
		}


		public String getNumero() {
			return numero;
		}


		public void setNumero(String numero) {
			this.numero = numero;
		}		
}

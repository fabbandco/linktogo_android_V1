package com.fabbandco.android.async;

import java.text.ParseException;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.fabbandco.android.api.CallRestWeb;
import com.fabbandco.android.api.SmsReceiver;
import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.android.model.SmsMessageCrypt;
import com.fabbandco.android.util.DateUtil;

public class AddMessageAsync extends AsyncTask<String, String, SmsMessageCrypt> {
	private SmsReceiver smsActivite;

	public AddMessageAsync(final SmsReceiver _smsReceiver) {
		this.smsActivite = _smsReceiver;
	}

	protected SmsMessageCrypt doInBackground(String... params) {
		String resultWebServ = CallRestWeb.callWebService(PersistanceApplication.getInstance().getCurrentApplication(), "?action=addmessage&login="+PersistanceApplication.getInstance().getUser().getEmail()+"&mdp="+PersistanceApplication.getInstance().getUser().getMdp()
				+"&messagekey=" + params[0]
				+"&message=" + params[1]
				+"&number="+ params[2]
				+"&name="+ params[3]
				+"&date="+ params[4]);
		
		SmsMessageCrypt smsCrypt = null; 
		try {
			smsCrypt = new SmsMessageCrypt(null, false, params[2], "", params[1], PersistanceApplication.getInstance().getCurrentApplication().getContentResolver(), DateUtil.formatDateWithSecond(params[4]+""));
		
			if ("ok".equalsIgnoreCase(resultWebServ)){
				if (smsCrypt!=null){
					smsCrypt.setOk(true);
					return smsCrypt;
				}
			}
		}catch (Exception e) {
			System.out.println("error : " + e.getMessage());
		}
		return smsCrypt;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
    protected void onPostExecute(final SmsMessageCrypt _isOk) {
		this.smsActivite.callBackAsync(_isOk);
    }

}

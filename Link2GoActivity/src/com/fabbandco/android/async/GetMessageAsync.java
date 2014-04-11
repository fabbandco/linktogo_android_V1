package com.fabbandco.android.async;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.fabbandco.android.api.CallRestWeb;
import com.fabbandco.android.application.PersistanceApplication;
import com.fabbandco.android.link2go.activity.Link2GoActivity;
import com.fabbandco.android.model.Message;
import com.fabbandco.android.util.JSONParser;

public class GetMessageAsync extends AsyncTask<String, String, Collection<Message>> {
	private Link2GoActivity actitviy;

	public GetMessageAsync(final Link2GoActivity _activity) {
		this.actitviy = _activity;
	}

	protected Collection<Message> doInBackground(String... params) {
		String resultWebServ = CallRestWeb.callWebService(PersistanceApplication.getInstance().getCurrentApplication(), "?action=getallmessage&login="+PersistanceApplication.getInstance().getUser().getEmail()+"&mdp="+PersistanceApplication.getInstance().getUser().getMdp());
		Collection <Message>colOut = new ArrayList<Message>();
		try {
			JSONArray jsoObj =  new JSONArray(resultWebServ); 
			if (null!=jsoObj){
				System.out.println(jsoObj);
				
				JSONObject jsTemp = null;
				for (int i = 0; i < jsoObj.length(); i++) {
					jsTemp = jsoObj.getJSONObject(i);
					if (jsTemp !=null){
						colOut.add(JSONParser.toMessage(jsTemp));
					}
				
				}
//				JSONObject jsoArrayObj =  new JSONObject(jsoObj.getString("results")); 	
				return colOut;
			}
		} catch (Exception e) {
			System.out.println("error : " + e.getMessage());
		}
		return colOut;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
    protected void onPostExecute(final Collection<Message> colMessages) {
		this.actitviy.callBackSynchro(colMessages);
    }

}

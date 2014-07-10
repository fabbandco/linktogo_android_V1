package com.fabbandco.android.application;

import com.fabbandco.android.model.Utilisateur;


public class PersistanceApplication {

	static private PersistanceApplication _persistance;
	private boolean isConnecte = false;
	private String email = "";
	private Utilisateur user = null;
	private Link2GoApplication currentApplication;
	private String registration_id  = "notSet";
	

	static public PersistanceApplication getInstance() {
		if (_persistance == null) {
			_persistance = new PersistanceApplication();
		}
		return _persistance;
	}


	// GET-SET
	
	public boolean isConnecte() {
		return isConnecte;
	}

	public void setConnecte(boolean isConnecte) {
		this.isConnecte = isConnecte;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Utilisateur getUser() {
		return user;
	}
	
	public void setUser(Utilisateur user) {
		this.user = user;
	}


	public Link2GoApplication getCurrentApplication() {
		return currentApplication;
	}


	public void setCurrentApplication(Link2GoApplication currentApplication) {
		this.currentApplication = currentApplication;
	}


	public String getRegistration_id() {
		return registration_id;
	}


	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
	
}

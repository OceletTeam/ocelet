package fr.ocelet.lang.jvmmodel;

import java.util.ArrayList;

public class Metadatastuff {
	private String modeldesc;
	private String webpage;
	private ArrayList<Parameterstuff> params;

	public Metadatastuff() {
		params = new ArrayList<Parameterstuff>();
		modeldesc = null;
		webpage = null;
	}

	public String getModeldesc() {
		return modeldesc;
	}

	public void setModeldesc(String modeldesc) {
		this.modeldesc = modeldesc;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public ArrayList<Parameterstuff> getParams() {
		return params;
	}

	public boolean hasParameters() {return !params.isEmpty();}
	
}

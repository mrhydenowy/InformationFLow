package com.pnowicki.pkpc.interfaces;

import org.codehaus.jettison.json.JSONArray;

public interface ViewDocumentsResource {
	public JSONArray getFiles();
	public JSONArray getBooleans();
	public JSONArray getInts();
}

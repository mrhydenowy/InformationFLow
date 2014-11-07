package com.pnowicki.pkpc.interfaces;

import org.codehaus.jettison.json.JSONArray;

public interface ViewDocumentsResource {
	public JSONArray getFilesName();
	public JSONArray isConsidered();
	public JSONArray getDocumentsId();
}

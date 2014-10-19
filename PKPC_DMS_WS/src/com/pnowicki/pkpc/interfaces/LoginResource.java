package com.pnowicki.pkpc.interfaces;

public interface LoginResource {
	public String tmpUsers(String userName,	String userPassword) throws Exception;
	public String getFinalPath();
}

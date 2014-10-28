package com.pnowicki.pkpc.interfaces;

import java.io.File;

import javax.ws.rs.core.Response;

public interface DataBaseInterface {
	public Response addDocument(String fileName, String fileSignature, String filePath, File file, String endFile) throws Exception;
	public String tmpUsers(String userName,	String userPassword) throws Exception;
	public String getFiles();
	public String getBooleans();
	public String getInts();
	public Response sendMail(
			String mailSubject,
			String mailContent,
			String mailsString,
			String fileDir,
			String fileIndex,
			String resultDocumentCheckBox,
			String host,
			String port,
			String userMail,
			String mailPassword);
}
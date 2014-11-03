package com.pnowicki.pkpc.interfaces;

import javax.ws.rs.core.Response;

public interface mailResource {
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

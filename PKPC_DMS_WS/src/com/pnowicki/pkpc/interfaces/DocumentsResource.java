package com.pnowicki.pkpc.interfaces;

import java.io.File;
import javax.ws.rs.core.Response;

public interface DocumentsResource {
	public Response addDocument(String fileName, String fileSignature, String filePath, File file, String endFile) throws Exception;
}
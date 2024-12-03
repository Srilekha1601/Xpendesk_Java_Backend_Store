package com.invoiceprocessing.invoiceprocessor.controller;

import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
//import java.util.Base64;
//import java.util.HashMap;

//import javax.swing.text.html.HTMLDocument.Iterator;
//
//import org.apache.catalina.connector.Response;
//import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.invoiceprocessing.invoiceprocessor.model.ResponseContainer;
import com.invoiceprocessing.invoiceprocessor.service.ImageRequestService;

@CrossOrigin(origins = "http://192.168.1.142:3000, http://192.168.1.143:3000, http://192.168.1.141:3000", allowCredentials = "true")
@RestController
@RequestMapping("/image")
public class ImageUploaderController {

	@Autowired
	ImageRequestService irequestsvr;

	// working API for upload image
	@PostMapping("/upload")
	public ResponseEntity<?> uploadImg(@RequestParam("file") MultipartFile mFile) throws IOException {
//		Map<String, String> fileNameMapper = new HashMap<String, String>();
//		fileNameMapper.put(mFile.getOriginalFilename(), mFile.getOriginalFilename());
		System.out.println(mFile.getOriginalFilename());
		return irequestsvr.uploadImageToDjango(mFile);
	}

	@PostMapping(value = "/uploadimg", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<VoucherDto>> imgUpload(@RequestParam("file") MultipartFile[] listOfFiles)
			throws IOException {
		return ResponseEntity.ok(irequestsvr.uploadMultipleImage(listOfFiles));
//		List<Map<String, Object>> convertedResponse = new ArrayList<>();
//		List<ResponseEntity<?>> responses = irequestsvr.uploadMultipleImage(listOfFiles);
//		System.out.println("responses"+responses.toString());
//		for(ResponseEntity<?> response : responses ) {
//			 byte[] decodedBytes = Base64Utils.decodeFromString(response.getBody().toString());
//	         String decodedBody = new String(decodedBytes, StandardCharsets.UTF_8);
//	         Map<String, Object> jsonObject = new HashMap<>();
//	         jsonObject.put("data", decodedBody);
//	         convertedResponse.add(jsonObject);
//		}
//		
//		return new ResponseEntity<>(convertedResponse, HttpStatus.OK);
	}
}

package com.invoiceprocessing.invoiceprocessor.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.invoiceprocessing.invoiceprocessor.payloadbody.UploadImage;
import com.invoiceprocessing.invoiceprocessor.payloadbody.UploadPdf;
import com.invoiceprocessing.invoiceprocessor.response.VoucherDto;

@Service
public class ImageRequestService {

	@Value("${django.api.upload.url}")
	private String djangoUploadUrl;

	private final RestTemplate restTemplate;

	public ImageRequestService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public ResponseEntity<?> uploadImageToDjango(MultipartFile mFileObj) throws IOException {
		if (isImage(mFileObj)) {
			byte[] byteArray = mFileObj.getBytes();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			HttpEntity<byte[]> requestEntity = new HttpEntity<>(byteArray, headers);

			return restTemplate.postForEntity(djangoUploadUrl, requestEntity, byte[].class);
		} else {
			Map<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("message",
					"Please upload only image file (supported file types: *.jpg or *.jpeg or *.png)");
			responseMap.put("status", Integer.parseInt("400"));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseMap);
		}
	}

	private static boolean isImage(MultipartFile file) {
		return (file != null && file.getOriginalFilename().toLowerCase().endsWith(".jpg")
				|| file != null && file.getOriginalFilename().toLowerCase().endsWith(".jpeg")
				|| file != null && file.getOriginalFilename().toLowerCase().endsWith(".png") ? true : false);
	}

	public List<VoucherDto> uploadMultipleImage(MultipartFile[] files) throws IOException {
		String fileType = "";
		List<VoucherDto> listOfObject = new ArrayList<VoucherDto>();
		ResponseEntity<byte[]> response = null;
		for (MultipartFile file : files) {

			byte[] convertByteArray = file.getBytes();
			HttpHeaders headers = new HttpHeaders();
			if (file != null && file.getOriginalFilename().toLowerCase().endsWith(".jpg")
					|| file != null && file.getOriginalFilename().toLowerCase().endsWith(".jpeg")
					|| file != null && file.getOriginalFilename().toLowerCase().endsWith(".png")) {
				UploadImage uploadImage = new UploadImage();
				headers.setContentType(MediaType.IMAGE_JPEG);
				fileType = file.getContentType().substring(0, 5);
				uploadImage.setImage(convertByteArray);
				uploadImage.setType(fileType);
				HttpEntity<byte[]> requestEntityofDjango = new HttpEntity<>(convertByteArray, headers);
				response = restTemplate.postForEntity(djangoUploadUrl, requestEntityofDjango, byte[].class);
			} else if (file != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
				UploadPdf uploadPdf = new UploadPdf();
				headers.setContentType(MediaType.APPLICATION_PDF);
				fileType = file.getContentType().substring(15 - 3, 15);
				uploadPdf.setPdf(convertByteArray);
				uploadPdf.setType(fileType);
				HttpEntity<byte[]> requestEntityofDjango = new HttpEntity<>(convertByteArray, headers);
				response = restTemplate.postForEntity(djangoUploadUrl, requestEntityofDjango, byte[].class);
			}

			ObjectMapper objectMapper = new ObjectMapper()
					.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
			System.out.println(response.getBody().clone());
			VoucherDto voucherDto = objectMapper.readValue(response.getBody(), VoucherDto.class);
			System.out.println(voucherDto);

			voucherDto.setFilename(file.getOriginalFilename());
			voucherDto.setImage(convertByteArray);
			voucherDto.setDeleteStatus("N");
			listOfObject.add(voucherDto);

		}
		return listOfObject;
	}

}

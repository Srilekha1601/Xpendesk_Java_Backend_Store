package com.invoiceprocessing.invoiceprocessor.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoiceprocessing.invoiceprocessor.model.Menu;
import com.invoiceprocessing.invoiceprocessor.service.MenuServiceImpl;

@RestController
@RequestMapping("/menus")
public class ShowMenusController {
	
	@Autowired MenuServiceImpl showMenus;
	
	@GetMapping("/showmenus")
	public ResponseEntity<Map<Object, Object>> showAllMenus() {
		return ResponseEntity.ok(showMenus.getMenus());
	}
	
	@GetMapping("/menu")
	public ResponseEntity<List<Menu>> showParent(){
		return ResponseEntity.ok(showMenus.getScreenAndMenus());
	}

}

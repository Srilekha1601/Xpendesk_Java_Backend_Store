package com.invoiceprocessing.invoiceprocessor.service;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.invoiceprocessing.invoiceprocessor.model.Menu;
import com.invoiceprocessing.invoiceprocessor.repository.MenuRepository;
import com.invoiceprocessing.invoiceprocessor.repository.ScreenRepository;
import com.invoiceprocessing.invoiceprocessor.servicerequirement.MenuService;


@Service
public class MenuServiceImpl implements MenuService{
	
	@Autowired MenuRepository menuRepository;
	@Autowired ScreenRepository screenRepository;

	@Override
	public Map<Object, Object> getMenus() {
		List<Menu> listOfMenus = menuRepository.findAll();
		
//		Map<String, Integer> chiMap = new HashMap<String, Integer>();
		Map<Object, Object> responseMap = new HashMap<Object, Object>(); 
		Map<Object, Object> screenMap = new HashMap<Object, Object>();
		listOfMenus.forEach((menu)->{
//			List<Screen> screens = screenRepository.getScreenFromMenu(menu.getMenuID());
//			for (Screen screen : screens) {
//				System.out.println(screen.getAction());
//				menu.getChildMenuID().forEach((child)->{
//					if(screen.getMenu().getMenuID() == child.getMenuID())
//						screenMap.put(screen.getMenu().getMenuName(), screen.getAction());
//				});
//				
//			}
//			System.out.println(menu.getSequence());
			if(menu.getParentMenuID() == null) {
				responseMap.put(menu.getMenuName(), menu.getChildMenuID());
				
//				menu.getChildMenuID().forEach((menuItem)->{
//					chiMap.put(menuItem.getMenuName(), menuItem.getParentManuID().getMenuID());
//				});
			}
		});
//		for(Map.Entry<String, Set<Menu>> response : responseMap.entrySet()) {
//			System.out.println(response.getKey());
//			response.getValue().forEach(child -> System.out.println("\t"+child.getMenuName()));
//		}
//		
//		Map<String, Object> responseMap = new HashMap<String, Object>();
//		for (Menu menu : listOfMenus) {
//			responseMap.put(menu.getMenuName(), menu.getChildMenuID());
////			System.out.println(menu.getChildMenuID().size());
//		}
		
		
//		List<Screen> screenList = screenRepository.findAll();
//		Map<Object, Object> responseScreen = new HashMap<Object, Object>();
//		for(Screen screen : screenList) {
////			responseScreen.put(screen.getScreenID(), screen.getAction());
//			responseScreen.put(screen.getScreenName(), screen.getAction());
//		}
//		responseMap.putAll(responseScreen);
		responseMap.putAll(screenMap);
		return responseMap;
	}

	@Override
	public List<Menu> getScreenAndMenus() {
		return menuRepository.fetchRootMenus();
	}

}

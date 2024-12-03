package com.invoiceprocessing.invoiceprocessor.servicerequirement;

import java.util.List;
import java.util.Map;

import com.invoiceprocessing.invoiceprocessor.model.Menu;

public interface MenuService {

	public Map<Object, Object> getMenus();
	
	public List<Menu> getScreenAndMenus();
}

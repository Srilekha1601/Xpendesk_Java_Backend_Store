package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	@Query("SELECT p FROM Menu p WHERE p.parentMenuID IS NULL ORDER BY p.sequence")
	public List<Menu> fetchRootMenus(); 

}

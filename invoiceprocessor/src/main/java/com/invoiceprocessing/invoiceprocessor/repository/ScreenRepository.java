package com.invoiceprocessing.invoiceprocessor.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer>{
	
//	@Query("SELECT e FROM Screen e WHERE e.menu IS NOT NULL")
//	List<Screen> findByMenuID();
	
	
	@Query("SELECT s FROM Screen s WHERE s.menu.menuID = :id")
	public Screen getScreenFromMenu(@Param("id") Integer id); 

}

package com.invoiceprocessing.invoiceprocessor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invoiceprocessing.invoiceprocessor.model.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

	@Query("SELECT s FROM State s WHERE s.state=?1")
	public State findByStateName(@Param("name") String stateName);

	public Optional<State> findByState(String stateName);
}

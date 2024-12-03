package com.invoiceprocessing.invoiceprocessor.securityconfiguration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.invoiceprocessing.invoiceprocessor.model.Employee;
import com.invoiceprocessing.invoiceprocessor.repository.EmployeeRepository;

@Component
public class UserDetailsInfoService implements UserDetailsService {

	@Autowired
	EmployeeRepository employeeRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Employee> employeeInfo = employeeRepository.findByUserName(username);
		return employeeInfo.map(XpendeskUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("This User is not present"));
	}

}

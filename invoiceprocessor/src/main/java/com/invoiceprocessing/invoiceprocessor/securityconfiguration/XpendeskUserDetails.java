package com.invoiceprocessing.invoiceprocessor.securityconfiguration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.invoiceprocessing.invoiceprocessor.model.Employee;

@SuppressWarnings("serial")
public class XpendeskUserDetails implements UserDetails {

	private String userName;
	private String password;
	private Integer employeeId;
	private String name;
	private List<GrantedAuthority> authorities;

	public XpendeskUserDetails(Employee employee) {
		userName = employee.getUserName();
		password = employee.getPassword();
		name = employee.getName();
		employeeId = employee.getEmployeeId();
		authorities = employee.getEmployeeRoleList().stream()
				.map((empRole) -> new SimpleGrantedAuthority(empRole.getRoleID().getRoleDescription()))
				.collect(Collectors.toList());

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}

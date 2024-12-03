package com.invoiceprocessing.invoiceprocessor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "MENU_ROLE_CROSS_REF")
public class MenuRoleCrossRef {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ROLE_ID")
	private Integer menuRoleID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MENU_ID")
	private Menu menuID;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_ID")
	private Role roleID;

	@Column(name = "ACCESS_TYPE", length = 1)
	private String accessType;

	public Integer getMenuRoleID() {
		return menuRoleID;
	}

	public void setMenuRoleID(Integer menuRoleID) {
		this.menuRoleID = menuRoleID;
	}

	public Menu getMenuID() {
		return menuID;
	}

	public void setMenuID(Menu menuID) {
		this.menuID = menuID;
	}

	public Role getRoleID() {
		return roleID;
	}

	public void setRoleID(Role roleID) {
		this.roleID = roleID;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

}

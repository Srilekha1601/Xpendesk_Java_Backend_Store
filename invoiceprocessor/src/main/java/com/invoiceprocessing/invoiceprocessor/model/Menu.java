package com.invoiceprocessing.invoiceprocessor.model;

//import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

//import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
//import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
//import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Menu")
public class Menu {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_ID")
	private Integer menuID;

	@Column(name = "MENU_NAME", nullable = false, length = 50)
	private String menuName;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_MENU_ID")
	private Menu parentMenuID;

	@OneToMany(mappedBy = "parentMenuID")
	private Set<Menu> childMenuID;

	@JsonIgnore
	@Column(name = "SEQUENCE")
	private Short sequence;
	
	@JsonIgnore
	@OneToOne(mappedBy = "menu")
	private Screen screen;
	
	
	private String screenName;

	public Integer getMenuID() {
		return menuID;
	}

	public void setMenuID(Integer menuID) {
		this.menuID = menuID;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Menu getParentMenuID() {
		return parentMenuID;
	}

	public void setParentMenuID(Menu parentMenuID) {
		this.parentMenuID = parentMenuID;
	}

	public Set<Menu> getChildMenuID() {
		return childMenuID;
	}

	public void setChildMenuID(Set<Menu> childMenuID) {
		this.childMenuID = childMenuID;
	}

	public Short getSequence() {
		return sequence;
	}

	public void setSequence(Short sequence) {
		this.sequence = sequence;
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	public String getScreenName() {
		return screen==null?StringUtils.EMPTY:screen.getAction();
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}

package com.rhem.seguridad.domain;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="cuentas")
public class Cuenta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private Boolean enabled = true;

	private String password;
	
	//estos atributos no estan en la base de datos
	@Transient
	private String typepassword;
	
	@Transient
	private String retypepassword;

	public Cuenta() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTypepassword() {
		return typepassword;
	}

	public void setTypepassword(String typepassword) {
		this.typepassword = typepassword;
	}

	public String getRetypepassword() {
		return retypepassword;
	}

	public void setRetypepassword(String retypepassword) {
		this.retypepassword = retypepassword;
	}

}
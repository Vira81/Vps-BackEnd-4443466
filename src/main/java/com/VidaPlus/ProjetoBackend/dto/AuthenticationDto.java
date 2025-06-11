package com.VidaPlus.ProjetoBackend.dto;

public class AuthenticationDto {

	/**
	 * Login com username e password
	 */
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}

package com.example.demo.payload;

import javax.validation.constraints.NotBlank;

public class  ChangePassword{
	
	@NotBlank
	private String email;
	
    @NotBlank
    private String currentPassword;

    @NotBlank
    private String NewPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return NewPassword;
	}

	public void setNewPassword(String newPassword) {
		NewPassword = newPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}    
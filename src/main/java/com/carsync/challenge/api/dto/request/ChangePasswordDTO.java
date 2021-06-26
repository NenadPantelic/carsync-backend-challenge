package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
	@Size(min = 6, max = 50, message = "Length of the password must be between 6 and 50.")
	@NotBlank(message = "You have to enter the current password!")
	private String _oldPassword;

	@Size(min = 6, max = 50, message = "Length of the password must be between 6 and 50.")
	@NotBlank(message = "You have to enter a new password!")
	private String _newPassword;

}

package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.Email;
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
public class LoginDTO {
	@NotBlank(message = "Email must be provided. Cannot be blank!")
	@Email(message = "Invalid email format.")
	private String _email;

	@Size(min = 6, max = 50, message = "Length of the password must be between 6 and 50.")
	@NotBlank(message = "Password cannot be blank!")
	private String _password;
}

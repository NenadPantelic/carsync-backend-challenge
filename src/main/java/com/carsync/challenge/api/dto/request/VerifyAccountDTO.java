package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccountDTO {

	@NotBlank(message = "Email must be provided. Cannot be blank!")
	@Email(message = "Invalid email format.")
	private String _email;

	@NotBlank(message = "Password cannot be blank!")
	private String _password;

	@NotBlank(message = "Verification token is required.")
	private String _verificationToken;

}

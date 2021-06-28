package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyAccountDTO  extends VerificationRequestDTO{

	@NotBlank(message = "Email must be provided. Cannot be blank!")
	@Email(message = "Invalid email format.")
	private String _email;

	@NotBlank(message = "Password cannot be blank!")
	@Size(min = 6, max = 50, message = "Length of the password must be between 6 and 50.")
	private String _password;


}

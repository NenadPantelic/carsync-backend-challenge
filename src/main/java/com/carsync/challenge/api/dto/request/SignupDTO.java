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
public class SignupDTO {

	@NotBlank(message = "Email cannot be blank. This is a required field.")
	@Email(message = "Invalid email format.")
	private String _email;

}

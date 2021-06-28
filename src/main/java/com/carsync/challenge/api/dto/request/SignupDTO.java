package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
public class SignupDTO extends AuthRequestDTO {

	@NotBlank(message = "Email cannot be blank. This is a required field.")
	@Email(message = "Invalid email format.")
	private String _email;

}

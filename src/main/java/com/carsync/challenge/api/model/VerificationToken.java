package com.carsync.challenge.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VerificationToken extends AuthToken{
	@NotBlank(message = "Email cannot be blank. This is a required field.")
	@Email(message = "Invalid email format.")
	@Column(name = "email", nullable = false)
	private String _email;

	@Column(name = "token", unique = true, length = 36)
	private String _token;

}

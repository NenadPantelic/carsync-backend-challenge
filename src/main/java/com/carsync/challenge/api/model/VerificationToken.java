package com.carsync.challenge.api.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class VerificationToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long _id;

	@NotBlank(message = "Email cannot be blank. This is a required field.")
	@Email(message = "Invalid email format.")
	@Column(name = "email", nullable = false)
	private String _email;

	@Column(name = "verification_token", unique = true, length = 36)
	private String _verificationToken;

	@Column(name = "expiration_time")
	private Instant _expirationTime;
}

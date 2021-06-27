package com.carsync.challenge.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class TwoFAToken extends AuthToken {

	@Column(name = "user_id", nullable = false)
	private Long _userId;

	@NotBlank(message = "Phone number cannot be blank. This is a required field.")
	@Column(name = "phone_no", nullable = false)
	private String _phoneNo;

	@Column(name = "token", unique = true, length = 6)
	private String _token;

}

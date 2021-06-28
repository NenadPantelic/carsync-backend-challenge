package com.carsync.challenge.api.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequestDTO {

	@NotBlank(message = "Verification token is required.")
	protected String _verificationToken;
}

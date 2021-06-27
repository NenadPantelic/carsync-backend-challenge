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
public class TwoFALoginDTO {
	@NotBlank(message = "2FA token is required.")
	private String _twoFAToken;
}

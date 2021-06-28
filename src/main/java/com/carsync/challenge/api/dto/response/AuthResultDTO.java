package com.carsync.challenge.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResultDTO {

	private String _token;
	@Builder.Default
	private Boolean _proceedWith2FA = false;

}

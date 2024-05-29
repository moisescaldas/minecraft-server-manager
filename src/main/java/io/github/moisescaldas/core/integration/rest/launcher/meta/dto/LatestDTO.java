package io.github.moisescaldas.core.integration.rest.launcher.meta.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestDTO implements Serializable {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 2057004180044498086L;
	
	private String release;
	private String snapshot;
}

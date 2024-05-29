package io.github.moisescaldas.core.integration.rest.launcher.meta.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionDTO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2514156008764293893L;
	
	private String id;
	private String type;
}

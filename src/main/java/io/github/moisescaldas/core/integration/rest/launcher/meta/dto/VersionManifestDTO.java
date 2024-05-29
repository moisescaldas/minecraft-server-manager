package io.github.moisescaldas.core.integration.rest.launcher.meta.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionManifestDTO implements Serializable {
	/**
	*
	*/
	private static final long serialVersionUID = 8990612860908404400L;

	private LatestDTO latest;
	
	private List<VersionDTO> versions;
}

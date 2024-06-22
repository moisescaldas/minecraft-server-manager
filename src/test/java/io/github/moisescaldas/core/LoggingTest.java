package io.github.moisescaldas.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import lombok.extern.log4j.Log4j2;

@Log4j2
class LoggingTest {

	@Test
	void loggingTest() {
		assertDoesNotThrow(() -> {
			log.info("TESTE INFO");
			log.debug("TESTE DEBUG");
			log.error("TESTE ERROR");
			log.warn("TESTE WARN");
			log.trace("TESTE Trace");

		});
	}
}

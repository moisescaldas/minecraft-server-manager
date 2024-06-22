package io.github.moisescaldas.core.service;

import java.io.IOException;
import java.util.Scanner;

import io.github.moisescaldas.core.exceptions.ApplicationServerException;
import io.github.moisescaldas.core.util.MessagesPropertiesUtil;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ApplicationScoped
public class TerminalService {
	
	@Resource(lookup = "concurrent/loggingService", type = ManagedExecutorService.class)
	private ManagedExecutorService loggingExecutorService;
	
    private static final String TERMINAL_INTERFACE = isWindows() ? "cmd.exe /c %s" : "/bin/sh -c %s";
    public static final String TASKKILL_COMMAND = isWindows() ? "taskkill /f /pid" : "kill -9";

    public Process execProcess(String... args) {
        try {
        	var process = Runtime.getRuntime().exec(String.format(TERMINAL_INTERFACE, String.join(" ", args)));
        	loggingExecutorService.submit(() -> {
        		try (var scanner = new Scanner(process.getInputStream())) {
        			while(scanner.hasNext()) log.info(scanner.nextLine());
        		}

        		try (var scanner = new Scanner(process.getErrorStream())) {
        			while(scanner.hasNext()) log.error(scanner.nextLine());
        		}
        	});
        	
            return process;
            
        } catch (IOException e) {
            throw new ApplicationServerException(MessagesPropertiesUtil.getString("E0004"));
        }
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }
}

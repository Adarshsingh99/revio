package com.revio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Revio Smart Study and Revision Tracker application.
 * 
 * This application provides:
 * - User authentication with JWT tokens
 * - Section and subsection management
 * - Topic tracking with completion status
 * - Smart revision scheduling system
 * - Daily revision reminders
 * 
 * @author Revio Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling // Enable scheduled tasks for daily revision system
public class RevioApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevioApplication.class, args);
		System.out.println("🚀 Revio Backend Started Successfully!");
		System.out.println("📚 Smart Study and Revision Tracker");
		System.out.println("🌐 Server running on http://localhost:8080/api");
	}

}

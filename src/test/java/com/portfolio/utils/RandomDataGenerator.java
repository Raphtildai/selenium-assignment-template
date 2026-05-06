package com.portfolio.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

/**
 * Generates random test data so tests don't rely on fixed strings
 * and can be run repeatedly without conflicts.
 */
public class RandomDataGenerator {

    private static final Random random = new Random();

    private static final String[] ADJECTIVES = {
        "Advanced", "Intelligent", "Dynamic", "Scalable", "Robust",
        "Distributed", "Reactive", "Secure", "Cloud-Native", "Serverless"
    };

    private static final String[] NOUNS = {
        "Dashboard", "Platform", "Pipeline", "Microservice", "API",
        "Portal", "Engine", "Framework", "System", "Toolkit"
    };

    private static final String[] TECH_STACKS = {
        "Java, Spring Boot, PostgreSQL",
        "Python, FastAPI, Redis",
        "React, Node.js, MongoDB",
        "Kotlin, Ktor, MySQL",
        "TypeScript, NestJS, GraphQL"
    };

    private static final String[] COMPANIES = {
        "TechCorp Kenya", "Innovate Africa", "Digital Solutions Ltd",
        "CloudBase Inc", "DataPipe Systems"
    };

    private static final String[] CERT_PROVIDERS = {
        "Google", "AWS", "Microsoft", "Coursera", "Udemy"
    };

    /** Returns a random project title, e.g. "Scalable API" */
    public static String projectTitle() {
        return ADJECTIVES[random.nextInt(ADJECTIVES.length)]
                + " " + NOUNS[random.nextInt(NOUNS.length)];
    }

    /** Returns a random tech stack string */
    public static String techStack() {
        return TECH_STACKS[random.nextInt(TECH_STACKS.length)];
    }

    /** Returns a short random description paragraph */
    public static String description() {
        return "Automated test entry " + UUID.randomUUID().toString().substring(0, 8)
                + ". This is a test-generated description that verifies form submission works correctly.";
    }

    /** Returns a random year string like "2023" */
    public static String year() {
        return String.valueOf(2019 + random.nextInt(6));
    }

    /** Returns today's date formatted as "January 1, 2024" */
    public static String certDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
    }

    /** Returns a random company name */
    public static String company() {
        return COMPANIES[random.nextInt(COMPANIES.length)];
    }

    /** Returns a random certification title */
    public static String certTitle() {
        return CERT_PROVIDERS[random.nextInt(CERT_PROVIDERS.length)]
                + " " + NOUNS[random.nextInt(NOUNS.length)] + " Certificate";
    }

    /** Returns a random role title */
    public static String jobRole() {
        return ADJECTIVES[random.nextInt(ADJECTIVES.length)] + " Software Engineer";
    }

    /** Returns a random URL-safe tag for test tracking */
    public static String tag() {
        return "selenium-" + UUID.randomUUID().toString().substring(0, 6);
    }
}

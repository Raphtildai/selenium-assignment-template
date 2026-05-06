package com.portfolio.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads test settings from config.properties so no values are hardcoded
 * in test classes or page objects.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static String getBaseUrl()    { return get("base.url"); }
    public static String getAdminUrl()   { return get("admin.url"); }
    public static String getAdminEmail() { return get("admin.email"); }
    public static String getAdminPassword() { return get("admin.password"); }
    public static boolean isHeadless()   { return getBoolean("headless"); }
    public static int getWindowWidth()   { return getInt("window.width"); }
    public static int getWindowHeight()  { return getInt("window.height"); }
    public static int getExplicitWait()  { return getInt("explicit.wait"); }
}

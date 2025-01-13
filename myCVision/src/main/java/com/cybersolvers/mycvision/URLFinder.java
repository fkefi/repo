package com.cybersolvers.mycvision;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A utility class to find the path of a URL-based resource on the local system.
 */
public class URLFinder {

    /**
     * Finds the absolute path of a given resource URL.
     *
     * @param resourceName The name of the resource to find.
     * @return The absolute path of the resource as a String, or an error message if not found.
     */
    public static String findResourcePath(String resourceName) {
        try {
            Path resourcePath = Paths.get(URLFinder.class.getClassLoader().getResource(resourceName).toURI());
            return resourcePath.toAbsolutePath().toString();
        } catch (Exception e) {
            return "Error: Resource not found or invalid URL. " + e.getMessage();
        }
    }
}

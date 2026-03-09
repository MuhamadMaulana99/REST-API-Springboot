package com.example.demoapi.utils;

public class DeviceInfoUtil {

    public static String getOS(String userAgent) {
        if (userAgent.contains("Windows"))
            return "Windows";
        if (userAgent.contains("Mac"))
            return "MacOS";
        if (userAgent.contains("X11"))
            return "Unix/Linux";
        if (userAgent.contains("Android"))
            return "Android";
        if (userAgent.contains("iPhone"))
            return "iOS";
        return "Unknown OS";
    }

    public static String getBrowser(String userAgent) {
        if (userAgent.contains("Chrome"))
            return "Chrome";
        if (userAgent.contains("Firefox"))
            return "Firefox";
        if (userAgent.contains("Safari") && !userAgent.contains("Chrome"))
            return "Safari";
        if (userAgent.contains("Edge"))
            return "Edge";
        return "Unknown Browser";
    }

    public static String getDevice(String userAgent) {
        if (userAgent.contains("Mobi"))
            return "Mobile";
        return "Desktop/PC";
    }
}
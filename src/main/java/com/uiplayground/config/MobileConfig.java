package com.uiplayground.config;

/**
 * Carries all required Appium desired capabilities for mobile driver creation.
 * Extend fields as your test suite grows (e.g. appActivity, bundleId, etc.)
 */
public record MobileConfig(
        String deviceName,
        String platformVersion,
        String appPath,          // Absolute path or .apk/.ipa on the host
        String appiumServerUrl   // e.g. "http://localhost:4723"
) {}

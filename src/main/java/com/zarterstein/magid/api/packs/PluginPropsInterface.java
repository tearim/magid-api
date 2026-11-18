package com.zarterstein.magid.api.packs;

public interface PluginPropsInterface {
    String get(String key);
    String get(String key, String defaultValue);
}

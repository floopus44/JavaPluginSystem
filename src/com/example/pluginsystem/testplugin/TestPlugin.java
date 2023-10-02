package com.example.pluginsystem.testplugin;

import com.example.pluginsystem.plugin.PluginBase;

public class TestPlugin extends PluginBase {
    @Override
    public void onLoad() {
        System.out.println("Plugin loaded");
    }

    @Override
    public void onUnload() {
        System.out.println("Plugin unloaded");
    }
}

package com.example.pluginsystem.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.jar.JarFile;

public class PluginLoader {
    private File workingDir = new File("run");
    private ArrayList<PluginBase> plugins = new ArrayList<>();

    public PluginLoader() {
        try {
            this.loadPlugins();
        } catch (Exception ignored) {}
        Runtime.getRuntime().addShutdownHook(new Thread(this::unloadPlugins)); // automatically unloads plugin when program stops
    }

    public PluginLoader(File workingDir) {
        this.workingDir = workingDir;
        try {
            this.loadPlugins();
        } catch (Exception ignored) {}
    }

    private void loadPlugins() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (File file : Objects.requireNonNull(this.workingDir.listFiles())) {
            if (file.getName().split("\\.")[file.getName().split("\\.").length - 1].toLowerCase().equals("jar")) {
                this.loadPlugin(new JarFile(file));
            }
        }
    }

    private void unloadPlugins() {
        for (PluginBase pluginBase : this.plugins) unloadPlugin(pluginBase);
    }

    private PluginBase getMainClass(JarFile jarFile) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (jarFile.getEntry("properties") == null) throw new RuntimeException("The jar file " + jarFile.getName() + " does not contain a proper properties file");
        Properties properties = new Properties();
        properties.load(jarFile.getInputStream(jarFile.getJarEntry("properties")));
        return (PluginBase) new URLClassLoader(new URL[]{new File(jarFile.getName()).toURI().toURL()}).loadClass(properties.getProperty("mainClass")).newInstance();
    }

    private void loadPlugin(JarFile jarFile) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        PluginBase pluginBase = this.getMainClass(jarFile);
        this.plugins.add(pluginBase);
        pluginBase.onLoad();
    }

    private void unloadPlugin(PluginBase pluginBase) {
        pluginBase.onUnload();
    }
}

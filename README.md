# JavaPluginSystem
a simple plugin loader written in Java

This program loads plugins in the form of .jar files.
The jar files must contain a file named `properties`: The content of the file has to look like this:
```
mainClass=path.to.PluginMainClass
```

The plugin main class has to be an instance of the PluginBase class.

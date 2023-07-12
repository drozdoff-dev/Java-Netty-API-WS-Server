package com.processing.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectionTools {

    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
        ArrayList<Class> classes = new ArrayList<Class>();
        while (resources.hasMoreElements()) {
            File directory = new File(URLDecoder.decode(resources.nextElement().getFile(), "UTF-8"));
            if (directory.exists()) {
                // Unpacked jar
                File[] files = directory.listFiles();
                if (files!=null) {
                    for (File f : files) {
                        if (f.getName().endsWith(".class")) {
                            classes.add(Class.forName(packageName + '.' + f.getName().substring(0, f.getName().length() - 6)));
                        }
                    }
                }
            } else {
                // Packed jar
                String[] parts = directory.getPath().substring("file:".length()).split("!");
                if (parts.length==2) {
                    Enumeration e = new JarFile(parts[0]).entries();
                    while (e.hasMoreElements()) {
                        String jen = ((JarEntry) e.nextElement()).getName();
                        if (jen.startsWith(packageName.replace('.', '/')) && jen.endsWith(".class")) {
                            jen = jen.substring(packageName.length() + 1);
                            jen = jen.substring(0, jen.length() - 6);
                            classes.add(Class.forName(packageName + '.' + jen));
                        }
                    }
                }
            }
        }
        return classes.toArray(new Class[classes.size()]);
    }
}

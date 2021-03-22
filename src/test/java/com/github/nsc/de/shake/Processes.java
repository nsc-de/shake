package com.github.nsc.de.shake;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Processes {

    public static ClassLoader createClassLoader(String classPath) throws MalformedURLException {
        return new URLClassLoader(new URL[] { new File(classPath).toURI().toURL() });
    }

    public static Class<?> loadClassInternal(String classPath, String cls) throws MalformedURLException, ClassNotFoundException {
        ClassLoader cl = createClassLoader(classPath);
        return cl.loadClass(cls);
    }

    public static Method getMainMethod(Class<?> cls) throws NoSuchMethodException {
        return cls.getMethod("main", String[].class);
    }

    public static void executeClassInternal(Class<?> cls, String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        getMainMethod(cls).invoke(null, (Object) args);
    }

    public static void executeClassInternal(String classPath, String cls, String[] args) throws MalformedURLException,
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        executeClassInternal(loadClassInternal(classPath, cls), args);
    }

    public static Process exec(Class<?> the_class, String[] jvmArgs, String[] args) throws IOException {
        return exec(System.getProperty("java.class.path"), the_class.getName(), jvmArgs, args);
    }

    public static Process exec(String classPath, String className, String[] jvmArgs, String[] args) throws IOException {

        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.addAll(Arrays.asList(jvmArgs));
        command.add("-cp");
        command.add(classPath);
        command.add(className);
        command.addAll(Arrays.asList(args));

        ProcessBuilder builder = new ProcessBuilder(command);

        return builder.inheritIO().start();

    }

}

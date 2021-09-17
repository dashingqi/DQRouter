package com.dashingqi.router.gradle

import java.util.jar.JarEntry
import java.util.jar.JarFile

/**
 * 映射收集
 */
class RouterMapCollector {

    /** 自动生成映射类的包名*/
    private static final String PACKAGE_NAME = "com/dashing/router/mapping"

    /** 自动生成映射类的类名前缀*/
    private static final String CLASS_NAME_PRE = "RouterMapping_"

    /** 要收集类名的后缀*/
    private static final String CLASS_FILE_SUFFIX = ".class"

    /** 收集自动生成class类名*/
    private final Set<String> mapping = new HashSet<>()

    /**
     * 返回收集的好的映射类名Set
     * @return
     */
    Set<String> getMapping() {
        return mapping
    }

    /**
     * 收集class文件或者class文件的目录
     * @param classFile
     */
    void collect(File classFile) {

        // 安全检查
        if (classFile == null || !classFile.exists()) {
            return
        }

        if (classFile.isFile()) {
            // 是个文件直接就去收集
            if (classFile.absolutePath.concat(PACKAGE_NAME) &&
                    classFile.name.startsWith(CLASS_NAME_PRE) &&
                    classFile.name.endsWith(CLASS_FILE_SUFFIX)) {
                String className = classFile.name.replace(CLASS_FILE_SUFFIX, "")
                mapping.add(className)
            }
        } else {
            // 是目录 直接递归
            classFile.listFiles().each {
                collect(it)
            }
        }

    }

    /**
     * 收集Jar包中的目标类
     * @param jarFile
     */
    void collectFromJarFile(File jarFile) {

        Enumeration enumeration = (JarEntry) new JarFile(jarFile).entries()

        while (enumeration.hasMoreElements()){
            JarEntry jarEntry = (JarEntry)enumeration.nextElement()
            String entryName = jarEntry.getName()
            if (entryName.contains(PACKAGE_NAME) &&
                entryName.contains(CLASS_NAME_PRE) &&
                    entryName.contains(CLASS_FILE_SUFFIX)

            ){
                String className = entryName.replace(PACKAGE_NAME, "")
                        .replace("/", "")
                        .replace(CLASS_FILE_SUFFIX, "")
                mapping.add(className)
            }
        }
    }

}
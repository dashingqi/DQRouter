package com.dashingqi.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * RouterPlugin
 */
class RouterPlugin implements Plugin<Project> {

    // 实现apply方法 注入我们插件的逻辑
    @Override
    void apply(Project project) {

        println("I am from RouterPlugin ,apply from ${project.name}")

        // 1. 自动将路径参数传递到注解处理器中（无需用户手动）
        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", project.rootProject.projectDir.absolutePath)
            }
        }

        // 2. 实现旧的构建产物自动清理(指定某一个构建产物进行清理)
        project.clean.doFirst {
            File routerMappingDir =
                    new File(project.rootProject.projectDir, "router_mapping")
            if (routerMappingDir.exists()) {
                routerMappingDir.deleteDir()
            }
        }

        project.getExtensions().create("router", RouterExtension)
        project.afterEvaluate {
            // 拿到Extension
            RouterExtension extension = project["router"]
            println "用户设置的wiki路径为 == ${extension.wikiDir}"
        }
    }
}
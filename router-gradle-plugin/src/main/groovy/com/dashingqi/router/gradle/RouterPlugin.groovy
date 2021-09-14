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

        if (project.extensions.findByName("kapt") != null) {
            project.extensions.findByName("kapt").arguments {
                arg("root_project_dir", rootProject.projectDir.absolutePath)
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
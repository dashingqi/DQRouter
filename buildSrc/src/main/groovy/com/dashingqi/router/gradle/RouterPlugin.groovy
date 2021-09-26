package com.dashingqi.router.gradle

import com.android.build.api.transform.Transform
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import groovy.json.JsonSlurper
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * RouterPlugin
 */
class RouterPlugin implements Plugin<Project> {

    // 实现apply方法 注入我们插件的逻辑
    @Override
    void apply(Project project) {

        // 注册自定义的Transform
        if (project.plugins.hasPlugin(AppPlugin)){
            AppExtension appExtension = project.extensions.getByType(AppExtension)
            Transform transform = new RouterMappingTransform()
            appExtension.registerTransform(transform)
        }

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
        if (!project.plugins.hasPlugin(AppPlugin)) {
            // 将生成文档交由App工程来做
            return
        }
        project.getExtensions().create("router", RouterExtension)
        project.afterEvaluate {
            // 拿到Extension
            RouterExtension extension = project["router"]
            println "用户设置的wiki路径为 == ${extension.wikiDir}"

            // 3. 在Javac任务执行后(compileDebugJavaWithJavac) 写成文档

            project.tasks.findAll { task ->
                task.name.startsWith('compile') &&
                        task.name.endsWith('JavaWithJavac')
            }.each { targetTask ->
                targetTask.doLast {
                    File routerMappingDir =
                            new File(project.rootProject.projectDir, 'router_mapping')

                    if (!routerMappingDir.exists()) {
                        return
                    }

                    File[] childFiles = routerMappingDir.listFiles()
                    if (childFiles.size() < 1) {
                        return
                    }
                    // 构建文档内容

                    StringBuilder markDownStringBuilder = new StringBuilder()

                    markDownStringBuilder.append("# 页面文档 \n\n")

                    childFiles.each { file ->
                        // 仅读取.json文件
                        if (file.name.endsWith(".json")) {
                            JsonSlurper jsonSlurper = new JsonSlurper()
                            def jsonContent = jsonSlurper.parse(file)
                            jsonContent.each { item ->
                                def url = item["url"]
                                def description = item["description"]
                                def realPath = item["realPath"]

                                markDownStringBuilder.append("## $description \n")
                                markDownStringBuilder.append("- url: $url \n")
                                markDownStringBuilder.append("- realPath: $realPath \n\n")
                            }
                        }
                    }

                    // 写入文件

                    File wikiFileDir = new File(extension.wikiDir)
                    if (!wikiFileDir.exists()) {
                        wikiFileDir.mkdir()
                    }

                    File markDownFile = new File(wikiFileDir, "页面文档.md")
                    if (markDownFile.exists()) {
                        markDownFile.delete()
                    }
                    markDownFile.write(markDownStringBuilder.toString())
                }
            }
        }
    }
}
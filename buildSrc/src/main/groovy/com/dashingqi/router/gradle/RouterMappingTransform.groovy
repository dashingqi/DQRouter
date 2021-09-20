package com.dashingqi.router.gradle

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils

import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * 建立Transform
 */
class RouterMappingTransform extends Transform {

    /**
     * 当前Transform的名称
     * @return
     */
    @Override
    String getName() {
        return "RouterMappingTransform"
    }

    /**
     * 返回告知编译器，当前Transform需要处理的类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 告知编译器，当前Transform作用的范围（整个工程还是单个子工程）
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 是否支持增量
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 把打包好的.class文件通过回调，传递到transform方法中
     * @param transformInvocation
     * @throws TransformException* @throws InterruptedException* @throws IOException
     * Transform基本的操作
     * 1. 遍历所有的Input
     * 2. 对Input进行二次处理 (非必需)
     * 3. 将Input拷贝到目标
     */
    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        RouterMapCollector routerMapCollector = new RouterMapCollector()
        // 1. 遍历所有的Input
        transformInvocation.inputs.each {
            // 文件夹类型(将Input拷贝到目标目录)
            it.directoryInputs.each { directoryInput ->
                routerMapCollector.collect(directoryInput.file)
                def destDir = transformInvocation.outputProvider
                        .getContentLocation(directoryInput.name,
                                directoryInput.contentTypes,
                                directoryInput.scopes,
                                Format.DIRECTORY)

                FileUtils.copyDirectory(directoryInput.file, destDir)
            }

            // jar类型 (将Input拷贝到目标目录)
            it.jarInputs.each { jarInput ->
                routerMapCollector.collectFromJarFile(jarInput.file)
                def dest = transformInvocation.outputProvider
                        .getContentLocation(jarInput.name,
                                jarInput.contentTypes,
                                jarInput.scopes,
                                Format.JAR)

                FileUtils.copyFile(jarInput.file, dest)
            }
        }

        println("router-map-collect ${routerMapCollector.getMapping()}")

        // 生成的类保存到一个Jar文件中
        File mappingJarFile = transformInvocation.outputProvider.getContentLocation(
                // 名称
                "router_mapping",
                getOutputTypes(),
                getScopes(),
                Format.JAR
        )

        // 打印即将生成的Jar包位置
        println("${getName()} mappingJarFile = $mappingJarFile")

        if (mappingJarFile.getParentFile().exists()) {
            mappingJarFile.getParentFile().mkdirs()
        }

        if (mappingJarFile.exists()) {
            mappingJarFile.delete()
        }

        // 将生成的字节码写入到本地文件中
        FileOutputStream fos = new FileOutputStream(mappingJarFile)
        JarOutputStream jarOutputStream = new JarOutputStream(fos)
        ZipEntry zipEntry =
                new ZipEntry(RouterMappingByteCodeBuilder.CLASS_NAME + ".class")
        jarOutputStream.putNextEntry(zipEntry)
        jarOutputStream.write(
                RouterMappingByteCodeBuilder.get(routerMapCollector.getMapping()))
        jarOutputStream.closeEntry()
        jarOutputStream.close()
        fos.close()
    }
}
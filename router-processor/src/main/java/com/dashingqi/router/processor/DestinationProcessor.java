package com.dashingqi.router.processor;

import com.dashingqi.router.annotaions.Destination;
import com.google.auto.service.AutoService;


import java.io.IOException;

import java.io.Writer;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * @author : zhangqi
 * @desc : Destination 注解处理器
 * @time : 2021/9/11 11:53
 */
// 自动注册注解处理器
@AutoService(Processor.class)
public class DestinationProcessor extends AbstractProcessor {

    /**
     * TAG
     */
    private final static String TAG = "DestinationProcessor";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        System.out.println("init");
    }

    /**
     * 告诉编译器，当前处理器支持的注解类型
     *
     * @return 注解集合
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Destination.class.getCanonicalName());
    }

    /**
     * 编译器找到指定的注解后，要回调的方法
     *
     * @param annotations
     * @param roundEnv
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 防止被重复调用
        if (roundEnv.processingOver()) {
            return false;
        }

        System.out.println(TAG + ">>> process start ....");

        String rootDir = processingEnv.getOptions().get("root_project_dir");
        System.out.println(rootDir + " <<<< ");


        // 获取了标记 @Destination注解类的信息
        Set<? extends Element> allDestinationElements =
                roundEnv.getElementsAnnotatedWith(Destination.class);
        // 当没有收集到 @Destination注解的时候，直接结束执行
        if (allDestinationElements.size() < 1) {
            return false;
        }

        //==================== 处理生成类文件


        StringBuilder sb = new StringBuilder();
        // 添加包名
        sb.append("package com.dashingqi.router.mapping;\n\n");

        // 添加导入的包名
        sb.append("import java.util.HashMap;\n");
        sb.append("import java.util.Map;\n\n");

        // 添加类信息
        String className = "RoutersMapping_" + System.currentTimeMillis();
        sb.append("public class " + className + " {\n");

        // 构建方法信息
        sb.append("    public Map<String, String> getMapping() {\n\n");

        sb.append("        HashMap<String, String> mapping = new HashMap<>();\n\n");
        for (Element element : allDestinationElements) {
            final TypeElement typeElement = (TypeElement) element;
            // 尝试在当前类上 获取@Destination的信息
            final Destination destination = typeElement.getAnnotation(Destination.class);
            if (destination == null) {
                continue;
            }

            final String url = destination.url();
            final String description = destination.description();
            // 使用当前注解的全类名
            final String realPath = typeElement.getQualifiedName().toString();
            sb.append("        mapping.put(")
                    .append("\"" + url + "\"")
                    .append(", ")
                    .append("\"" + realPath + "\"")
                    .append(");\n\n");


            System.out.println(TAG + ">>> url = " + url);
            System.out.println(TAG + ">>> description = " + description);
            System.out.println(TAG + ">>> realPath = " + realPath);

        }

        sb.append("        return mapping;\n\n");
        sb.append("    }\n\n");
        sb.append("}");

        System.out.println(TAG + ">>> process finish ....");

        System.out.println(TAG + ">>> class file content = \n\n" + sb);

        // 将文件写入到本地文件中
        String classFullName = "com.dashingqi.router.mapping." + className;

        // 将自动生成的文件写入到本地文件中
        Writer fileWriter = null;
        try {
            JavaFileObject filer = processingEnv.getFiler()
                    .createSourceFile(classFullName);
            fileWriter = filer.openWriter();
            fileWriter.write(sb.toString());
            fileWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error when create file", e);
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

}

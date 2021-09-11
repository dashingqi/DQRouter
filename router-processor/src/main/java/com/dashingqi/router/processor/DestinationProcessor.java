package com.dashingqi.router.processor;

import com.dashingqi.router.annotaions.Destination;
import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * @author : zhangqi
 * @desc : Destination 注解处理器
 * @time : 2021/9/11 11:53
 */
// 自动注册注解处理器
@AutoService(Processor.class)
class DestinationProcessor extends AbstractProcessor {

    /**
     * TAG
     */
    private final static String TAG = "DestinationProcessor";

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
        System.out.println(TAG + ">>> process start ....");


        // 防止被重复调用
        if (roundEnv.processingOver()){
            return false;
        }

        // 获取了标记 @Destination注解类的信息
        Set<Element> allDestinationElements = (Set<Element>)
                roundEnv.getElementsAnnotatedWith(Destination.class);
        // 当没有收集到 @Destination注解的时候，直接结束执行
        if (allDestinationElements.size() < 1) {
            return false;
        }

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

            System.out.println(TAG + ">>> url = " + url);
            System.out.println(TAG + ">>> description = " + description);
            System.out.println(TAG + ">>> realPath = " + realPath);

        }

        System.out.println(TAG + ">>> process finish ....");

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }
}

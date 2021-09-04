package com.dashingqi.router.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : zhangqi
 * @desc : 路由注解
 * @time : 2021/9/4 22:12
 */

/**
 * 说明当前注解可以修饰的元素，此处表示可以用于标记在类上面
 */
@Target({ElementType.TYPE})
/**
 * 说明当前注解可以保留的时间 - 当.java --> .class文件是可以保留的
 */
@Retention(RetentionPolicy.CLASS)
public @interface Destination {
    /**
     * 当前页面的URL，不能为空
     *
     * @return 页面URL
     */
    String url();

    /**
     * 页面的描述
     *
     * @return 页面的描述信息
     */
    String description();
}

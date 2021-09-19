package com.dashingqi.router.gradle

import org.objectweb.asm.Opcodes

class RouterMappingByteCodeBuilder implements Opcodes {

    public static final String CLASS_NAME =
            "com/dashingqi/router/mapping/generated/RouterMapping"

    static byte[] get(Set<String> allMappingNames) {

        // 1.创建一个类
        // 2.创建构造方法
        // 3.创建get方法
        // (3.1)创建一个Map
        // (3.2)塞入所有映射表的内容
        // (3.3)返回map
    }

}
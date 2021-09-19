package com.dashingqi.router.gradle

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
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

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
        cw.visit(V1_8,
                ACC_PUBLIC + ACC_SUPER,
                CLASS_NAME,
                null,
                "java/lang/Object",
                null
        )

        // 生成或者编辑方法
        MethodVisitor mv

        // 生成构造方法
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC,
            "<init>",
                "()V"
        )
    }

}
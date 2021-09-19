package com.dashingqi.router.gradle

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 生成字节码
 */
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
                // 类名
                CLASS_NAME,
                null,
                // 父类的名字
                "java/lang/Object",
                null
        )

        // 生成或者编辑方法
        MethodVisitor mv

        // 生成构造方法
        mv = cw.visitMethod(
                // public类型
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null, null
        )

        mv.visitCode()
        // 访问父类的构造方法
        mv.visitVarInsn(Opcodes.ALOAD, 0)
        mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                "java/lang/Object",
                "<init>",
                "()V",
                false
        )
        mv.visitInsn(Opcodes.RETURN)
        mv.visitMaxs(1, 1)
        mv.visitEnd()

        // 生成Get方法

        mv = cw.visitMethod(ACC_PUBLIC,
                "get",
                "()Ljava/util/Map;",
                "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;",
                null)

        mv.visitCode()
        mv.visitTypeInsn(NEW, "java/util/HashMap")
        mv.visitInsn(DUP)
        //得到一个HashMap的实例
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/util/HashMap",
                "<init>",
                "()V",
                false)
        // 保存这个HashMap实例
        mv.visitVarInsn(ASTORE, 0)

        // 向map中，逐个塞入所有映射的内容
        allMappingNames.each {
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC,
                    "com/dashingqi/router/mapping/$it",
                    "getMapping",
                    "()Ljava/util/Map;",
                    false)
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "java/util/Map",
                    "putAll",
                    "(Ljava/util/Map;)V",
                    true)
        }
        // 返回map
        mv.visitVarInsn(ALOAD, 0)
        mv.visitInsn(ARETURN)
        // 设置局部变量的栈帧大小
        mv.visitMaxs(2, 2)
        mv.visitEnd()

        return cw.toByteArray()
    }

}
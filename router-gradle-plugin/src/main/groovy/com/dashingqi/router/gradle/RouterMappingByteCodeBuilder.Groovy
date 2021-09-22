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

        // 用来修改或者生成类的接口          帮助我们自动计算局部变量栈帧的大小
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)

        // 创建类
        cw.visit(V1_8,
                // 修饰符
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

        // 开启字节码的生成或者访问
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
        // 局部变量对应的栈帧大小
        mv.visitMaxs(1, 1)
        mv.visitEnd()


        // 生成Get方法
        mv = cw.visitMethod(ACC_PUBLIC,
                "get",
                "()Ljava/util/Map;",
                "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;",
                null)

        mv.visitCode()
        // 访问HashMap
        mv.visitTypeInsn(NEW, "java/util/HashMap")
        // 入栈
        mv.visitInsn(DUP)
        // 得到一个HashMap的实例
        mv.visitMethodInsn(INVOKESPECIAL,
                "java/util/HashMap",
                "<init>",
                // 方法的签名
                "()V",
                false)
        // 保存这个HashMap实例
        mv.visitVarInsn(ASTORE, 0)

        // 向map中，逐个塞入所有映射的内容
        allMappingNames.each {

            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESTATIC,
                    // 这个包名 和我们在注解处理器中声明的要一致
                    "com/dashingqi/router/mapping/$it",
                    // 调用那个方法
                    "getMapping",
                    // 签名
                    "()Ljava/util/Map;",
                    false)
            // 将我们得到的值put进去
            mv.visitMethodInsn(INVOKEINTERFACE,
                    "java/util/Map",
                    "putAll",
                    "(Ljava/util/Map;)V",
                    // Map是一个接口
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
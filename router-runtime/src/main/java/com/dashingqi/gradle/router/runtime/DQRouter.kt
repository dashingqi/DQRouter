package com.dashingqi.gradle.router.runtime

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

/**
 * @desc : 路由初始化的过程
 * @author : zhangqi
 * @time : 2021/9/21 10:40
 */
object DQRouter {

    /** TAG */
    private const val TAG = "DQRouter"

    /** 编译期间生成的总映射表 （运行期间是 .） */
    private const val GENERATED_MAPPING =
        "com.dashingqi.router.mapping.generated.RouterMapping"

    /** 存储所有映射表信息*/
    private val mapping: HashMap<String, String> = HashMap()

    /**
     * 初始化的方法
     * 1. 反射获取到对应的总映射类
     */
    fun init() {
        try {
            val clazz = Class.forName(GENERATED_MAPPING)
            val method = clazz.getMethod("get")
            // 静态方法 传入null
            val allMappingMethod = method.invoke(null)
            val allMapping = allMappingMethod as Map<String, String>
            if (allMapping?.size > 0) {
                Log.d(TAG, "init:get all mapping success ")
                allMapping.onEach {
                    Log.d(TAG, "${it.key} =====> ${it.value} ")
                }

                mapping.putAll(allMapping)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "init: error while init router :$e")
        }
    }

    /**
     * 页面跳转逻辑
     * @param context Context activityContext
     * @param url String url信息
     *
     * 配置Url
     * 解析Url 封装成Bundle
     * 打开对应的activity，传入指定参数
     */
    fun go(context: Context, url: String) {

        if (context == null || url.isNullOrEmpty()) {
            Log.d(TAG, " go error: context or url is null or empty")
            return
        }

        // 解析Url
        val targetUri = Uri.parse(url)

        // router://dashingqi/gradle?name=zhangqi&age=18
        val scheme = targetUri.scheme
        val host = targetUri.host
        val path = targetUri.path

        var targetActivityClassName = ""
        mapping.onEach {
            val templateUri = Uri.parse(it.key)
            val tScheme = templateUri.scheme
            val tHost = templateUri.host
            val tPath = templateUri.path

            if (scheme == tScheme && host == tHost && path == tPath) {
                targetActivityClassName = it.value
                return@onEach
            }
        }

        if (targetActivityClassName.isNullOrEmpty()) {
            Log.e(TAG, "go: target url is not found")
            return
        }

        // 查找参数，封装成Bundle
        val targetBundle = Bundle()
        val params = targetUri.query
        params?.let {
            if (it.length >= 3) {
                val paramsArr = it.split("&")
                paramsArr.onEach { singleParam ->
                    val singleParam = singleParam.split("=")
                    if (singleParam.size == 2) {
                        targetBundle.putString(singleParam[0], singleParam[1])
                    }
                }
            }
        }

        // 目标页面的跳转
        try {
            val targetActivity = Class.forName(targetActivityClassName)
            Intent(context, targetActivity).apply {
                putExtras(targetBundle)
                context.startActivity(this)
            }
        } catch (t: Throwable) {
            Log.e(TAG, "go: get target activity error $t")
        }
    }
}
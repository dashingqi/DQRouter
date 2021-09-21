package com.dashingqi.router

import android.app.Application
import com.dashingqi.gradle.router.runtime.DQRouter

/**
 * @desc : Application
 * @author : zhangqi
 * @time : 2021/9/21 10:49
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化DQRouter
        DQRouter.init()
    }
}
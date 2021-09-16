package com.dashingqi.gradle.lib_read

import androidx.appcompat.app.AppCompatActivity
import com.dashingqi.router.annotaions.Destination

/**
 * @author zhangqi61
 * @since 2021/9/16
 */
@Destination(
    url = "router://activity_reading",
    description = "阅读页面"
)
class ReadingActivity : AppCompatActivity() {
}
package com.dashingqi.router.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhangqi
 * @desc : Mapping的测试文件
 * @time : 2021/9/12 11:08
 */
public class RouterMapping_1 {

    public static Map<String, String> getMapping() {
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("url", "classPath");
        return mapping;
    }
}

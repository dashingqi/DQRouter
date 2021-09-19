package com.dashingqi.router.mapping;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhangqi
 * @desc :
 * @time : 2021/9/19 09:20
 */
public class RouterMapping_2 {

    public static Map<String, String> getMapping() {
        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("url", "classPath");
        return mapping;
    }
}

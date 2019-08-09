package com.ted.resonance.utils.log;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/3/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogProxy {

    // 功能描述
    String value() default "";

//    // 是否开启执行时间（性能监控）
//    boolean executeTime() default false;
}

package com.ted.resonance.utils.log;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/26.
 */
@Aspect
@Component
public class LogProxyAspect {

    private Logger LOG = LoggerFactory.getLogger(LogProxyAspect.class);

//    @Autowired
//    SystemConfig systemConfig;

    @Pointcut("@annotation(com.ted.resonance.utils.log.LogProxy)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = null;
        try {
            Object[] args = proceedingJoinPoint.getArgs();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = signature.getMethod();
            LogProxy logProxy = method.getAnnotation(LogProxy.class);
            boolean isExecuteTime = false;
            // TODO 此处可以加一个开关
            isExecuteTime = true;
            StringBuilder paramsInfo = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if ((args[i] instanceof HttpServletRequest) ||
                        (args[i] instanceof HttpServletResponse) ||
                        (args[i] instanceof Model)) {
                    continue;
                }
                if (i == args.length - 1) {
                    paramsInfo.append(JSON.toJSONString(args[i]));
                } else {
                    paramsInfo.append(JSON.toJSONString(args[i]) + ", ");
                }
            }

            // 处理开始字符串
            StringBuilder startStrDesc = new StringBuilder();
            startStrDesc.append("【" + logProxy.value() + "开始】");
            startStrDesc.append(" 请求参数: " + paramsInfo.toString());
            LOG.info(startStrDesc.toString());

            long startTime = 0;
            if (isExecuteTime) {
                startTime = System.currentTimeMillis();
            }
            result = proceedingJoinPoint.proceed(args);

            // 处理结束字符串
            StringBuilder endStrDesc = new StringBuilder();
            endStrDesc.append("【" + logProxy.value() + "结束】");
            if (isExecuteTime) {
                endStrDesc.append(" |耗时：" + (System.currentTimeMillis() - startTime) + "毫秒|");
            }
            endStrDesc.append(" 返回值：" + JSON.toJSONString(result));
            LOG.info(endStrDesc.toString());
        } catch (Throwable throwable) {
            throw throwable;
        }

        return result;
    }
}

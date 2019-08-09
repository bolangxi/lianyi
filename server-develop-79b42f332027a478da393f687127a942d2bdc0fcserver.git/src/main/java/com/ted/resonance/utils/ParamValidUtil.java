package com.ted.resonance.utils;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.exception.TedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * 座右铭：求真、务实
 *
 * @Author： Gread
 * @Date： Created in 下午3:20 2019/5/20
 * @Description：
 */
public class ParamValidUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParamValidUtil.class);

    /**
     * 校验参数不为空
     *
     * @param params
     * @return
     */
    public static boolean validParams(Object... params) {
        for (Object obj : params) {
            if (obj == null) {
                return false;
            } else if (obj instanceof Integer) {
                if (Integer.valueOf(obj + "").intValue() == 0) {
                    return false;
                }
            } else if (obj instanceof String) {
                if ("".equals(obj + "")) {
                    return false;
                }
            } else if (obj instanceof Long) {
                if (Long.valueOf(obj + "").longValue() == 0) {
                    return false;
                }
            } else if (obj instanceof List) {
                List list = (List) obj;
                if (list.size() == 0) {
                    return false;
                }
            } else if (obj instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) obj;
                if (bigDecimal.doubleValue() == 0) {
                    return false;
                }
            } else {
                LOGGER.error("不支持此类型：{}", obj.getClass().getTypeName());
                return false;
            }
        }
        return true;
    }

    public static void validParams(DServiceConstant dServiceConstant, Object... params) {
        for (Object obj : params) {
            if (obj == null) {
                throw new TedException(dServiceConstant, null);
            } else if (obj instanceof Integer) {
                if (Integer.valueOf(obj + "").intValue() == 0) {
                    throw new TedException(dServiceConstant, null);
                }
            } else if (obj instanceof String) {
                if ("".equals(obj + "")) {
                    throw new TedException(dServiceConstant, null);
                }
            } else if (obj instanceof Long) {
                if (Long.valueOf(obj + "").longValue() == 0) {
                    throw new TedException(dServiceConstant, null);
                }
            } else if (obj instanceof List) {
                List list = (List) obj;
                if (list.size() == 0) {
                    throw new TedException(dServiceConstant, null);
                }
            } else if (obj instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) obj;
                if (bigDecimal.doubleValue() == 0) {
                    throw new TedException(dServiceConstant, null);
                }
            } else {
                LOGGER.error("不支持此类型：{}", obj.getClass().getTypeName());
                throw new TedException(dServiceConstant, null);
            }
        }
    }

    public static void main(String[] args) {
        validParams(null, 111L, 111);
    }
}

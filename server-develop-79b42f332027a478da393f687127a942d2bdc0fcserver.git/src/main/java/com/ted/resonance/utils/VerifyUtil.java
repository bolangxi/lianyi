package com.ted.resonance.utils;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.exception.TedException;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

/**
 * Created by qingyun.yu on 2018/11/23.
 */
public class VerifyUtil {
    public static void objectNotNull(Object object, DServiceConstant dServiceConstant) {
        if(Objects.isNull(object)) {
            throw new TedException(dServiceConstant, null);
        }
    }

    public static void stringNotBlank(String str, DServiceConstant dServiceConstant) {
        if(Strings.isBlank(str)) {
            throw new TedException(dServiceConstant, null);
        }
    }

    public static void numberNotZore(Integer rowNum, DServiceConstant dServiceConstant) {
        if (rowNum <= 0) {
            throw new TedException(dServiceConstant, null);
        }
    }

    public static void objectEquals(Object a, Object b, DServiceConstant dServiceConstant) {
        if(!a.equals(b)) {
            throw new TedException(dServiceConstant, null);
        }
    }
}

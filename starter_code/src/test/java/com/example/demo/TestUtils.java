package com.example.demo;

import java.lang.reflect.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestUtils {
    private static final Logger logger = LogManager.getLogger();
    public static void injectObject(Object target,String fieldName,Object toInject) {
        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            if (!f.isAccessible()) {
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);
            if (wasPrivate) {
                f.setAccessible(false);
            }
        }catch (NoSuchFieldException e){
            e.printStackTrace();
            logger.info("There is no such field.");
        }catch (IllegalAccessException e){
            e.printStackTrace();
            logger.info("This is a illegal access.");
        }

    }
}

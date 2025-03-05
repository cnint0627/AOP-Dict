package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class DictAspect {
    @Pointcut(value = "execution(* org.example.UserController.*(..))")
    public void translateDict() {

    }

    @Around(value = "translateDict()")
    public Object test(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("执行切面方法");
        Object object = joinPoint.proceed();
        if (object instanceof Result<?>) {
            Object data = ((Result<?>) object).getData();
            Class<?> clazz = data.getClass();
            JSONObject jo = (JSONObject) JSON.toJSON(data);
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Dict.class)) {
                    String dictFieldName = field.getName() + "_text";
                    String dictText = DictManager.getDictItem(field.getDeclaredAnnotation(Dict.class).dictName(), field.getInt(data));
                    jo.put(dictFieldName, dictText);
                }
            }
            ((Result) object).setData(jo);
        }
        return object;
    }
}

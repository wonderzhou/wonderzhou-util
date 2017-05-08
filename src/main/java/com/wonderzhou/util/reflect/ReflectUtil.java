package com.wonderzhou.util.reflect;

import java.lang.reflect.Method;

public class ReflectUtil {
    public static String getQualifiedName(Method method) {

        StringBuilder sb = new StringBuilder();

        if (method != null) {
            sb.append(method.getDeclaringClass().getName()).append(".").append(method.getName());
            sb.append("(");

            Class<?>[] parameterTypes = method.getParameterTypes();

            for (int i = 0; i < parameterTypes.length; i++) {
                if (i != 0) {
                    sb.append(",");
                }

                sb.append(parameterTypes[i].getName());
            }

            sb.append(")");

        }

        return sb.toString();
    }

    public static String getSuperClassQualifiedName(Method method) {

        if (method != null) {
            Class<?> declaringClass = method.getDeclaringClass();

            Class<?>[] interfaces = declaringClass.getInterfaces();

            Method superMethod = null;

            for (Class<?> item : interfaces) {
                try {
                    superMethod = item.getDeclaredMethod(method.getName(), method.getParameterTypes());
                } catch (Exception e) {
                }

                if (superMethod != null) {
                    return getQualifiedName(superMethod);
                }
            }
        }

        return null;
    }
}

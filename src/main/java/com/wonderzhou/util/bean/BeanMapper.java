package com.wonderzhou.util.bean;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.dozer.CustomFieldMapper;
import org.dozer.DozerBeanMapper;
import org.dozer.classmap.ClassMap;
import org.dozer.fieldmap.FieldMap;
import org.hibernate.Hibernate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * 
 * 1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4. 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 * 
 * 
 */
public class BeanMapper {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper mapper = new DozerBeanMapper();

    static {
        mapper.setCustomFieldMapper(new CustomFieldMapper() {
            public boolean mapField(Object source,
                                    Object destination,
                                    Object sourceFieldValue,
                                    ClassMap classMap,
                                    FieldMap fieldMapping) {
                // If field is initialized, Dozer will continue mapping
                return !Hibernate.isInitialized(sourceFieldValue);
            }
        });
    }

    /**
     * 基于Dozer转换对象的类型.
     * 
     * @param source
     * @param destClz
     * @return
     */
    public static <T> T map(Object source, Class<T> destClz) {
        return mapper.map(source, destClz);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     * 
     * @param sourceList
     * @param destClz
     * @return
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destClz) {
        List<T> destList = Lists.newArrayList();

        for (Object sourceObject : sourceList) {
            destList.add(mapper.map(sourceObject, destClz));
        }

        return destList;
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     * 
     * @param sourceList
     * @param destClz
     * @return
     */
    public static <T> Set<T> mapSet(Collection<?> sourceList, Class<T> destClz) {
        Set<T> destList = Sets.newHashSet();

        for (Object sourceObject : sourceList) {
            destList.add(mapper.map(sourceObject, destClz));
        }

        return destList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     * 
     * @param source
     * @param destObj
     */
    public static void copy(Object source, Object destObj) {
        mapper.map(source, destObj);
    }

}

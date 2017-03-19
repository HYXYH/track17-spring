package track.container;

import org.apache.commons.lang3.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import track.container.config.Bean;
import track.container.config.Property;
import track.container.config.ValueType;

/**
 * Основной класс контейнера
 * У него определено 2 публичных метода, можете дописывать свои методы и конструкторы
 */
public class Container {

    private Map<String, Object> objById;
    private Map<String, Object> objByClassName;
    private Map<String, Bean> beanById;
    private Map<String, Bean> beanByClassName;


    // Реализуйте этот конструктор, используется в тестах!
    public Container(List<Bean> beans) {
        objById = new HashMap<String, Object>();
        objByClassName = new HashMap<String, Object>();
        beanById = new HashMap<String, Bean>();
        beanByClassName = new HashMap<String, Bean>();
        for (Bean b : beans) {
            beanById.put(b.getId(), b);
            beanByClassName.put(b.getClassName(), b);
        }
    }

    /**
     * Вернуть объект по имени бина из конфига
     * Например, Car car = (Car) container.getById("carBean")
     */
    public Object getById(String id) throws GetBeanException {
        return beanToObject(objById, beanById, id);
    }

    /**
     * Вернуть объект по имени класса
     * Например, Car car = (Car) container.getByClass("track.container.beans.Car")
     */
    public Object getByClass(String className) throws GetBeanException {
        return beanToObject(objByClassName, beanByClassName, className);
    }

    private Object beanToObject(Map<String, Object> objMap, Map<String, Bean> beanMap, String name) throws GetBeanException {
        if (objMap.containsKey(name)) {
            return objMap.get(name);
        } else {
            if (beanMap.containsKey(name)) {
                try {
                    Bean bean = beanMap.get(name);
                    Class<?> clazz = Class.forName(bean.getClassName());
                    Object object = clazz.newInstance();

                    objById.put(bean.getId(), object);
                    objByClassName.put(bean.getClassName(), object);

                    setProperties(object, bean);
                    return object;
                } catch (Exception e) {
                    throw new GetBeanException(e.getMessage());
                }
            } else {
                throw new GetBeanException("No such bean: " + name);
            }
        }
    }


    private void setProperties(Object obj, Bean bean) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, GetBeanException {
        Class<?> clazz = Class.forName(bean.getClassName());
        for (String propName : bean.getProperties().keySet()) {
            Method setter = getSetterFor(clazz.getMethods(), propName);
            Property property = bean.getProperties().get(propName);
            Class<?> propClazz = setter.getParameterTypes()[0];

            if (property.getType().equals(ValueType.REF)) {
                setter.invoke(obj, propClazz.cast(getById(property.getValue())));
            } else {
                setter.invoke(obj, toObject(property.getValue(), propClazz));
            }
        }
    }

    private Object toObject(String value, Class clazz) {
        if (Boolean.class == clazz || Boolean.TYPE == clazz) {
            return Boolean.parseBoolean(value);
        }
        if (Byte.class == clazz || Byte.TYPE == clazz) {
            return Byte.parseByte(value);
        }
        if (Short.class == clazz || Short.TYPE == clazz) {
            return Short.parseShort(value);
        }
        if (Integer.class == clazz || Integer.TYPE == clazz) {
            return Integer.parseInt(value);
        }
        if (Long.class == clazz || Long.TYPE == clazz) {
            return Long.parseLong(value);
        }
        if (Float.class == clazz || Float.TYPE == clazz) {
            return Float.parseFloat(value);
        }
        if (Double.class == clazz || Double.TYPE == clazz) {
            return Double.parseDouble(value);
        }
        return value;
    }

    private Method getSetterFor(Method[] methods, String paramName) throws GetBeanException {
        paramName = StringUtils.capitalize(paramName);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.equalsIgnoreCase("set" + paramName)) {
                return method;
            }
        }
        throw new GetBeanException("no setter for " + paramName);
    }
}


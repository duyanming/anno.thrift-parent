package anno.infrastructure;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanMapping {
    private static BeanMapping  _instance;
    public static BeanMapping instance(){
        if(_instance==null){
            _instance=new BeanMapping();
        }
        return _instance;
    }
    public <T, T1> T copy(T1 source, Class<T> toResult) {
        try {
            T target = toResult.newInstance();
            List<Field> fieldsTarget = getAllDeclaredFields(target.getClass());
            List<Field> filesSource = getAllDeclaredFields(source.getClass());
            for (Field field : fieldsTarget) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                for (Field fieldSource : filesSource) {
                    if (field.getName().toLowerCase().equals(fieldSource.getName().toLowerCase())) {
                        fieldSource.setAccessible(true);
                        if (field.getType().getTypeName() == fieldSource.getType().getTypeName()) {
                            field.set(target, fieldSource.get(source));
                        } else {
                            try {
                                String typeName = field.getType().getName();
                                Class<?> type = field.getType();
                                if (!typeName.startsWith("java.lang.") && !typeName.startsWith("int")) {
                                    // 不是基本类型 且类型不同不转换
                                } else if (type.equals(String.class)) {
                                    field.set(target, fieldSource.get(source).toString());
                                } else if (type.equals(Integer.class)) {
                                    field.set(target, Integer.parseInt(fieldSource.get(source).toString()));
                                } else if (type.equals(int.class)) {
                                    field.set(target, (int) Integer.parseInt(fieldSource.get(source).toString()));
                                } else if (type.equals(Long.class)) {
                                    field.set(target, Long.parseLong(fieldSource.get(source).toString()));
                                } else if (type.equals(Double.class)) {
                                    field.set(target, Double.parseDouble(fieldSource.get(source).toString()));
                                } else if (type.equals(Float.class)) {
                                    field.set(target, Float.parseFloat(fieldSource.get(source).toString()));
                                } else if (type.equals(Character.class)) {
                                    field.set(target, fieldSource.get(source).toString().charAt(0));
                                } else if (type.equals(Short.class)) {
                                    field.set(target, Short.parseShort(fieldSource.get(source).toString()));
                                } else if (type.equals(Boolean.class)) {
                                    field.set(target, Boolean.parseBoolean(fieldSource.get(source).toString()));
                                }
                            } catch (Exception ex) {
                                //
                            }
                        }
                    }
                }
            }
            return target;
        } catch (Exception ex) {
            return null;
        }
    }

    public <T, T1> List<T> copy(List<T1> sources, Class<T> toResult) {
        if(sources==null){
            return null;
        }
        List<T> ts = new ArrayList<>();
        for (T1 t1 : sources) {
            T t= copy(t1, toResult);
            if(t!=null){
                ts.add(t);
            }
        }
        return ts;
    }

    public <T, T1> T1 copy(T source, T1 target) {
        try {
            List<Field> fieldsTarget = getAllDeclaredFields(target.getClass());
            List<Field> filesSource = getAllDeclaredFields(source.getClass());
            for (Field field : fieldsTarget) {
                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                for (Field fieldSource : filesSource) {
                    if (field.getName().toLowerCase().equals(fieldSource.getName().toLowerCase())) {
                        fieldSource.setAccessible(true);
                        if (field.getType().getTypeName().equals(fieldSource.getType().getTypeName())) {
                            field.set(target, fieldSource.get(source));
                        } else {
                            try {
                                String typeName = field.getType().getName();
                                Class<?> type = field.getType();
                                if (!typeName.startsWith("java.lang.") && !typeName.startsWith("int")) {
                                    // 不是基本类型 且类型不同不转换
                                } else if (type.equals(String.class)) {
                                    field.set(target, fieldSource.get(source).toString());
                                } else if (type.equals(Integer.class)) {
                                    field.set(target, Integer.parseInt(fieldSource.get(source).toString()));
                                } else if (type.equals(int.class)) {
                                    field.set(target, (int) Integer.parseInt(fieldSource.get(source).toString()));
                                } else if (type.equals(Long.class)) {
                                    field.set(target, Long.parseLong(fieldSource.get(source).toString()));
                                } else if (type.equals(Double.class)) {
                                    field.set(target, Double.parseDouble(fieldSource.get(source).toString()));
                                } else if (type.equals(Float.class)) {
                                    field.set(target, Float.parseFloat(fieldSource.get(source).toString()));
                                } else if (type.equals(Character.class)) {
                                    field.set(target, fieldSource.get(source).toString().charAt(0));
                                } else if (type.equals(Short.class)) {
                                    field.set(target, Short.parseShort(fieldSource.get(source).toString()));
                                } else if (type.equals(Boolean.class)) {
                                    field.set(target, Boolean.parseBoolean(fieldSource.get(source).toString()));
                                }
                            } catch (Exception ex) {
                                //
                            }
                        }
                    }
                }
            }
            return target;
        } catch (Exception ex) {
            return null;
        }
    }

    private List<Field> getAllDeclaredFields(Class clazz) {
        List<Field> files = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null
                && !clazz.getSuperclass().getName().equals(Object.class.getName())) {
            List<Field> fs = getAllDeclaredFields(clazz.getSuperclass());
            files.addAll(fs);
        }
        return files;
    }
}

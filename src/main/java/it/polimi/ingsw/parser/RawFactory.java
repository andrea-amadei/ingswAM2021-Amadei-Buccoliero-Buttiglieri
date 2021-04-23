package it.polimi.ingsw.parser;

import com.google.gson.annotations.SerializedName;
import jdk.jfr.Experimental;

import java.lang.reflect.*;
import java.util.Map;

@Deprecated
@Experimental
public class RawFactory {
    private RawFactory() { }

    public static <R extends RawObject<?>> Class<?> getGenericRawList(String newFieldName) {
        // Get the annotation of Generic
        SerializedName annotation;
        Field f;
        Object type;
        Class<GenericRawList> newClass = GenericRawList.class;

        try {
            annotation = newClass.getDeclaredField("list").getAnnotation(SerializedName.class);
        } catch (NoSuchFieldException e) {
            throw new UnknownError("Could not find field \"list\"");
        }

        if (annotation == null)
            throw new UnknownError("Unable to find annotation \"SerializedName\"");

        // Get the proxy processor
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);

        // Get the private memberValues property
        try {
            f = invocationHandler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException e) {
            throw new UnknownError("Could not find field \"value\" of annotation \"SerializedName\"");
        }
        f.setAccessible(true);

        // Get the attribute map of the instance
        Map<String, Object> values;
        try {
            values = (Map<String, Object>) f.get(invocationHandler);
        } catch (IllegalAccessException e) {
            throw new UnknownError("Could not find field \"list\"");
        }

        System.out.println(values);

        // Modify the attribute value
        values.put("value", newFieldName);

        System.out.println(values);

        return newClass;
    }
}

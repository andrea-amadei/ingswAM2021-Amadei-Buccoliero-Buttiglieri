package it.polimi.ingsw.annotations;

import java.lang.annotation.*;

/**
 * TODO
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface SerializedGroup {
    String value();
}

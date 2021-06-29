package it.polimi.ingsw.common.annotations;

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

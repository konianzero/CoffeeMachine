package net.coffeemachine.util.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
//@Inherited
public @interface DatabaseLogging {
}

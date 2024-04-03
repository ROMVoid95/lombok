package lombok;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(SOURCE)
@Target(TYPE)
public @interface EliteCreator
{
    @Target(FIELD)
    @Retention(SOURCE)
    public @interface Default {}

    
    String builderMethodName() default "Creator";

    String buildMethodName() default "create";

    String builderClassName() default "Generator";
    
    boolean toBuilder() default true;

    String setterPrefix() default "";
    
    @Target({FIELD, PARAMETER})
    @Retention(SOURCE)
    public @interface ObtainVia {

        String field() default "";

        String method() default "";

        boolean isStatic() default false;
    }
}

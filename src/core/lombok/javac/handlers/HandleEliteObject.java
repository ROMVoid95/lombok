package lombok.javac.handlers;

import static lombok.javac.handlers.JavacHandlerUtil.*;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.util.List;

import lombok.AccessLevel;
import lombok.EliteObject;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.spi.Provides;

@Provides
@HandlerPriority(-512)
public class HandleEliteObject extends JavacAnnotationHandler<EliteObject>
{

    private HandleFieldDefaults     handleFieldDefaults     = new HandleFieldDefaults();
    private HandleGetter            handleGetter            = new HandleGetter();
    private HandleEqualsAndHashCode handleEqualsAndHashCode = new HandleEqualsAndHashCode();

    @Override
    public void handle(AnnotationValues<EliteObject> annotation, JCAnnotation ast, JavacNode annotationNode)
    {

        deleteAnnotationIfNeccessary(annotationNode, EliteObject.class);
        JavacNode typeNode = annotationNode.up();
        boolean notAClass = !isClass(typeNode);

        if (notAClass)
        {
            annotationNode.addError("@Value is only supported on a class.");
            return;
        }

        handleFieldDefaults.generateFieldDefaultsForType(typeNode, annotationNode, AccessLevel.PRIVATE, false, true);
        handleGetter.generateGetterForType(typeNode, annotationNode, AccessLevel.PUBLIC, true, List.<JCAnnotation> nil());
        handleEqualsAndHashCode.generateEqualsAndHashCodeForType(typeNode, annotationNode, Boolean.FALSE);
    }
}

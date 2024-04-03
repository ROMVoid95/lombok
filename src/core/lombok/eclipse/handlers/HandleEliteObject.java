package lombok.eclipse.handlers;

import static lombok.core.handlers.HandlerUtil.handleFlagUsage;
import static lombok.eclipse.handlers.EclipseHandlerUtil.isClass;

import java.util.Collections;

import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

import lombok.AccessLevel;
import lombok.ConfigurationKeys;
import lombok.EliteObject;
import lombok.ObjectType;
import lombok.core.AnnotationValues;
import lombok.core.HandlerPriority;
import lombok.eclipse.EclipseAnnotationHandler;
import lombok.eclipse.EclipseNode;
import lombok.spi.Provides;

@Provides
@HandlerPriority(-510)
public class HandleEliteObject extends EclipseAnnotationHandler<EliteObject>
{
    private HandleFieldDefaults     handleFieldDefaults     = new HandleFieldDefaults();
    private HandleGetter            handleGetter            = new HandleGetter();
    private HandleEqualsAndHashCode handleEqualsAndHashCode = new HandleEqualsAndHashCode();

    public void handle(AnnotationValues<EliteObject> annotation, Annotation ast, EclipseNode annotationNode)
    {
        handleFlagUsage(annotationNode, ConfigurationKeys.VALUE_FLAG_USAGE, "@EliteObject");

        EclipseNode typeNode = annotationNode.up();

        if (!isClass(typeNode))
        {
            annotationNode.addError("@EliteObject is only supported on a class.");
            return;
        }
        TypeDeclaration typeDecl = (TypeDeclaration) typeNode.get();
        EliteObject annotationInstance = annotation.getInstance();
        ObjectType type = annotationInstance.value();
        
        
        
        if (!(type == ObjectType.PARENT)) {
            if ((typeDecl.modifiers & ClassFileConstants.AccFinal) == 0)
            {
                typeDecl.modifiers |= ClassFileConstants.AccFinal;
                typeNode.rebuild();
            }
        }

        handleFieldDefaults.generateFieldDefaultsForType(typeNode, annotationNode, AccessLevel.PRIVATE, false, true);
        handleGetter.generateGetterForType(typeNode, annotationNode, AccessLevel.PUBLIC, true, Collections.<Annotation> emptyList());
        handleEqualsAndHashCode.generateEqualsAndHashCodeForType(typeNode, annotationNode, false);
    }
}

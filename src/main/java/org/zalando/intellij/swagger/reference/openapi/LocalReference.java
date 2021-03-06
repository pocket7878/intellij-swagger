package org.zalando.intellij.swagger.reference.openapi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zalando.intellij.swagger.traversal.path.PathFinder;

import static org.zalando.intellij.swagger.reference.swagger.OpenApiConstants.COMPONENT_REFERENCE_PREFIX;
import static org.zalando.intellij.swagger.reference.swagger.OpenApiConstants.SLASH;

public class LocalReference extends PsiReferenceBase<PsiElement> {

    private final String originalRefValue;

    public LocalReference(@NotNull final PsiElement element,
                          @NotNull final String originalRefValue) {
        super(element);
        this.originalRefValue = originalRefValue;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        final String referenceType = ReferenceValueExtractor.extractType(originalRefValue);
        final String referencedValue = ReferenceValueExtractor.extractValue(originalRefValue);

        final String pathExpression = String.format("$.components.%s.%s", referenceType, referencedValue);
        final PsiFile psiFile = getElement().getContainingFile();

        return new PathFinder().findByPathFrom(pathExpression, psiFile)
                .orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        final String referenceType = ReferenceValueExtractor.extractType(originalRefValue);
        return super.handleElementRename(COMPONENT_REFERENCE_PREFIX + referenceType + SLASH + newElementName);
    }

}

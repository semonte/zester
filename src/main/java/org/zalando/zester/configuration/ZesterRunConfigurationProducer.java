package org.zalando.zester.configuration;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl;
import org.zalando.zester.file.FileDetector;

public class ZesterRunConfigurationProducer extends RunConfigurationProducer<ZesterRunConfiguration> {

    public ZesterRunConfigurationProducer() {
        super(ZesterConfigurationType.getInstance().getFactory());
    }

    @Override
    protected boolean setupConfigurationFromContext(ZesterRunConfiguration configuration,
                                                    ConfigurationContext context,
                                                    Ref<PsiElement> sourceElement) {
        PsiClass psiClass = getPsiClass(context.getPsiLocation());

        if (shouldCreateConfigurationFromPsiClass(psiClass)) {
            handleClassConfiguration(configuration, psiClass);
            return true;
        }

        PsiPackage psiPackage = getPsiPackage(context.getPsiLocation());

        if (shouldCreateConfigurationFromPsiPackage(psiPackage)) {
            handlePackageConfiguration(configuration, psiPackage);
            return true;
        }

        return false;
    }

    private void handlePackageConfiguration(ZesterRunConfiguration configuration, PsiPackage psiPackage) {
        configuration.setTargetTestClassQualifiedName(psiPackage.getQualifiedName() + ".*");
        configuration.setTargetClasses(psiPackage.getQualifiedName() + ".*");
        configuration.setGeneratedName();
    }

    private void handleClassConfiguration(ZesterRunConfiguration configuration, PsiClass psiClass) {
        if (psiClass.getQualifiedName().lastIndexOf(PitJavaParameters.UNIT_TEST_SUFFIX) != -1) {
            String testClasses = psiClass.getQualifiedName().substring(0, psiClass.getQualifiedName().lastIndexOf(PitJavaParameters.UNIT_TEST_SUFFIX));
            configuration.setTargetClasses(testClasses);
        }

        configuration.setTargetTestClassQualifiedName(psiClass.getQualifiedName());
        configuration.setGeneratedName();
    }

    private boolean shouldCreateConfigurationFromPsiClass(PsiClass psiClass) {
        return psiClass != null && psiClass.getQualifiedName() != null && FileDetector.isTestClass(psiClass);
    }

    private boolean shouldCreateConfigurationFromPsiPackage(PsiPackage psiPackage) {
        return psiPackage != null;
    }

    private PsiClass getPsiClass(PsiElement psiElement) {
        if (psiElement instanceof PsiClass) {
            return (PsiClass) psiElement;
        }
        return null;
    }

    private PsiPackage getPsiPackage(PsiElement psiElement) {
        if (psiElement instanceof PsiJavaDirectoryImpl) {
            return JavaDirectoryService.getInstance().getPackage((PsiDirectory) psiElement);
        }
        return null;
    }

    @Override
    public boolean isConfigurationFromContext(ZesterRunConfiguration configuration, ConfigurationContext context) {
        PsiClass psiClass = getPsiClass(context.getPsiLocation());

        if (psiClass != null) {
            return FileDetector.isTestClass(psiClass)
                    && configuration.getTargetTestClassQualifiedName().equals(psiClass.getQualifiedName());
        }

        PsiPackage psiPackage = getPsiPackage(context.getPsiLocation());

        return psiPackage != null
                && configuration.getTargetTestClassQualifiedName().equals(psiPackage.getQualifiedName() + ".*");

    }
}

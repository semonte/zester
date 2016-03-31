package org.zalando.zester.settings;

import com.intellij.ide.util.ClassFilter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiClass;
import org.zalando.zester.file.FileDetector;

class SourceClassFilter implements ClassFilter {

    public boolean isAccepted(PsiClass aClass) {
        return ApplicationManager.getApplication()
                .runReadAction((Computable<Boolean>) () -> !FileDetector.isTestClass(aClass));
    }
}

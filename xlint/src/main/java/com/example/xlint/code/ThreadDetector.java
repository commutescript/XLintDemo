package com.example.xlint.code;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.uast.UCallExpression;

import java.util.Collections;
import java.util.List;

/**
 * @author David.Yi
 * @Describe check the thread
 * @create 2019/3/14
 */
public class ThreadDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create(
        "NewThreadError",
        "Don't use new Thread() directly.",
        "Please use executors and thread utils.",
        Category.PERFORMANCE, 9, Severity.ERROR,
        new Implementation(ThreadDetector.class, Scope.JAVA_FILE_SCOPE));

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Collections.singletonList("java.lang.Thread");
    }

    @Override
    public void visitConstructor(JavaContext context, UCallExpression node, PsiMethod constructor) {
        context.report(ISSUE, node, context.getLocation(node), "Don't use new Thread() directly, please use executors and thread utils.");
    }
}

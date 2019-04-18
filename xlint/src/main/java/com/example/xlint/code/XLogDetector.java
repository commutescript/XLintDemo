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

import java.util.Arrays;
import java.util.List;

/**
 * @author David.Yi
 * @Describe check the log component
 * @create 2019/4/16
 */
public class XLogDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create(
        "XLogUseError",
        "Don't use android.util.Log directly.",
        "Please use custom component -- XLog.",
        Category.PERFORMANCE, 9, Severity.ERROR,
        new Implementation(XLogDetector.class, Scope.JAVA_FILE_SCOPE)

    );

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v", "d", "i", "w", "e");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, node, context.getLocation(node), "Don't use android.util.Log directly, please use custom component -- XLog.");
        }
    }

}

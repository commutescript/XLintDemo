package com.example.xlint.code;

import com.android.annotations.NonNull;
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
 * @Describe check the toast component
 * @create 2019/3/14
 */
public class ToastDetector extends Detector implements Detector.UastScanner {


    private static final String CHECK_PACKAGE = "android.widget.Toast";

    public static final Issue ISSUE = Issue.create(
        "ToastUseError",
        "Avoid use Toast directly.",
        "Please use our Toast factory.",
        Category.CORRECTNESS,
        9,
        Severity.ERROR,
        new Implementation(ToastDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("makeText");
    }

    @Override
    public void visitMethod(@NonNull JavaContext context, @NonNull UCallExpression node, @NonNull PsiMethod method) {
        if (!context.getEvaluator().isMemberInClass(method, CHECK_PACKAGE)) {
            return;
        }

        context.report(ISSUE, node, context.getLocation(node), "Avoid use Toast directly, please use our Toast factory.");
    }
}

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
 * @Describe check the router component
 * @create 2019/3/14
 */
public class XRouterDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create(
        "XRouterUseError",
        "Don't use startActivity or startActivityForResult to router directly .",
        "Please use custom component -- XRouter.",
        Category.PERFORMANCE, 9, Severity.ERROR,
        new Implementation(XRouterDetector.class, Scope.JAVA_FILE_SCOPE)

    );

    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("startActivity", "startActivityForResult");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        String name = method.getName();
        if ("startActivity".equals(name) || "startActivityForResult".equals(name)) {
            context.report(ISSUE, node, context.getLocation(node), "请勿直接调用startActivity，请使用基础组件—-XRouter");
        }
    }

}

package com.example.xlint.xml;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author David.Yi
 * @Describe check the view id name
 * @create 2019/4/18
 */
public class ViewIdDetector extends ResourceXmlDetector {

    String reportStr = "view id isn't standard:{%s} should start with：{%s}";

    public static final Issue ISSUE = Issue.create(
        "ViewIdError",
        "view id name isn't standard.",
        "View id name should start with view headword, please check the design rules.",
        Category.USABILITY,
        5,
        Severity.ERROR,
        new Implementation(ViewIdDetector.class, Scope.RESOURCE_FILE_SCOPE)
    );


    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.LAYOUT == folderType;
    }


    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList("id");
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        super.visitAttribute(context, attribute);
        String prnMain = context.getMainProject().getDir().getPath();
        String prnCur = context.getProject().getDir().getPath();
        if (attribute.getName().startsWith("android:id") && prnMain.equals(prnCur)) {
            checkNameSpace(context, attribute);
        }
    }

    private void checkNameSpace(XmlContext context, Attr attribute) {

        String rootView = attribute.getOwnerElement().getTagName();
        // "+!id/xxx"
        String viewId = attribute.getValue().substring(5);

        String standardId = "";
        boolean shouldReport = false;

        switch (rootView) {
            case "TextView":
                standardId = "tv";
                shouldReport = !viewId.startsWith(standardId);
                break;
            case "ImageView":
                standardId = "img";
                shouldReport = !viewId.startsWith(standardId);
        }
        if (shouldReport && !standardId.isEmpty()) {
            context.report(ISSUE, attribute, context.getLocation(attribute), String.format(reportStr, viewId, standardId));
        }

    }

}

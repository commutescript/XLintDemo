package com.example.xlint.xml;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;

/**
 * @author David.Yi
 * @Describe checkout the layout name.
 * @create 2019/4/18
 */
public class LayoutNameDetector extends ResourceXmlDetector {

    public static Issue ISSUE = Issue.create(
        "LayoutNameError",
        "The layout name isn't standard.",
        "Layout name should start with 'layout_', please check the design rules.",
        Category.ICONS,
        9,
        Severity.FATAL,
        new Implementation(LayoutNameDetector.class, Scope.RESOURCE_FILE_SCOPE)
    );

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT;
    }

    @Override
    public void visitDocument(@NotNull XmlContext context, @NotNull Document document) {
        String fileName = context.file.getName();
        if (fileName.contains(".xml") && !fileName.startsWith("layout_")) {
            context.report(ISSUE, Location.create(context.file), "The layout name isn't standard, Layout name should start with 'layout_'.");
        }

    }

}


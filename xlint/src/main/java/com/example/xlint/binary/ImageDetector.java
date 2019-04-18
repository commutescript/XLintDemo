package com.example.xlint.binary;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.ResourceContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.annotations.NotNull;

/**
 * @author David.Yi
 * @Describe check large size image
 * @create 2019/4/1
 */
public class ImageDetector extends Detector implements Detector.BinaryResourceScanner {

    private final static int IMG_SIZE = 200;

    private final static String PNG = ".png";
    private final static String JPEG = ".jpeg";
    private final static String JPG = ".jpg";
    private final static String WEBP = ".webp";

    String reportStr = "The image size is : %d" + "KB, large than the limited:" + IMG_SIZE + "KB, please compress it.";

    public static Issue ISSUE = Issue.create(
        "ImgLargeError",
        "The image size it too lager.",
        "Please compress it.",
        Category.ICONS,
        9,
        Severity.FATAL,
        new Implementation(ImageDetector.class, Scope.BINARY_RESOURCE_FILE_SCOPE)
    );

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return (folderType == ResourceFolderType.MIPMAP || folderType == ResourceFolderType.DRAWABLE);
    }

    @Override
    public void checkBinaryResource(@NotNull ResourceContext context) {
        ResourceFolderType folderType = context.getResourceFolderType();
        if (folderType != null) {
            String filename = context.file.getName();
            if (filename.contains(PNG)
                || filename.contains(JPEG)
                || filename.contains(JPG)
                || filename.contains(WEBP)) {
                long fileRealSize = context.file.length() / 1024;
                if (fileRealSize > IMG_SIZE) {
                    context.report(ISSUE, Location.create(context.file), String.format(reportStr, fileRealSize));
                }
            }
        }
    }
}

package com.example.xlint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.example.xlint.binary.ImageDetector;
import com.example.xlint.xml.LayoutNameDetector;
import com.example.xlint.code.ThreadDetector;
import com.example.xlint.code.XLogDetector;
import com.example.xlint.code.XRouterDetector;
import com.example.xlint.code.ToastDetector;
import com.example.xlint.xml.ViewIdDetector;

import java.util.Arrays;
import java.util.List;

/**
 * @author David.Yi
 * @Describe
 * @create 2019/3/13
 */
public class XIssueRegister extends IssueRegistry {

    public XIssueRegister() {
        System.out.println("************************* xlint check begin *************************");
        System.out.println();
    }

    @Override
    public List<Issue> getIssues() {

        return Arrays.asList(
            ToastDetector.ISSUE,
            XRouterDetector.ISSUE,
            XLogDetector.ISSUE,
            ThreadDetector.ISSUE,
            ImageDetector.ISSUE,
            ViewIdDetector.ISSUE,
            LayoutNameDetector.ISSUE
        );
    }

    @Override
    public int getApi() {
        return ApiKt.CURRENT_API;
    }

    @Override
    public int getMinApi() {
        return 1;
    }


}

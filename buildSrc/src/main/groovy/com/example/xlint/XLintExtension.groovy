package com.example.xlint

import org.gradle.api.*


/**
 * @author David.Yi
 * @Describe lint plugin
 * @create 2019/3/14
 */
class XLintExtension {

    boolean runAfterAssemble

    boolean abortOnError

    XLintExtension(Project project) {
    }
}
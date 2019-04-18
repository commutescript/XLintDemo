package com.example.xlint

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.dsl.LintOptions
import com.android.build.gradle.tasks.LintBaseTask
import org.gradle.api.*
import org.gradle.api.tasks.TaskState

/**
 * @author David.Yi
 * @Describe lint plugin
 * @create 2019/3/14
 */
class XLintPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.extensions.create("xlint", XLintExtension, project)

        // add dependencies
        addDependencies(project)

        project.afterEvaluate {
            getAndroidVariants(project).all { variant ->
                // clean lintOptions and merge lint lint.xml
                mergeLint(variant.name.capitalize(), project)
            }
        }
    }

    private void addDependencies(Project project) {
        project.dependencies {
            // lint jar, put your aar dependencies.
            // lintChecks 'com.example.android:xlint:0.1.0-alpha'

        }
        project.configurations.all {
            resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
            resolutionStrategy.cacheDynamicVersionsFor 0, 'seconds'
        }

    }

    private void mergeLint(def variantName, Project project) {

        def extension = project.getExtensions().findByName("xlint") as XLintExtension

        LintBaseTask lintBaseTask = project.tasks.getByName("lint$variantName") as LintBaseTask
        Task assembleTask = project.getTasks().getByName("assemble$variantName") as Task

        File lintFile = project.file("lint.xml")
        File lintOldFile = null

        def newLintOptions = new LintOptions()

        newLintOptions.textReport = true
        newLintOptions.lintConfig = lintFile
        newLintOptions.abortOnError = extension.abortOnError
        newLintOptions.htmlReport = true
        newLintOptions.htmlOutput = project.file("${project.buildDir}/reports/lint/lint-result.html")
        newLintOptions.xmlReport = true
        newLintOptions.xmlOutput = project.file("${project.buildDir}/reports/lint/lint-result.xml")

        lintBaseTask.lintOptions = newLintOptions

        lintBaseTask.doFirst {
            if (lintFile.exists()) {
                lintOldFile = project.file("lintOld.xml")
                lintFile.renameTo(lintOldFile)
            }

            if (!copyLintXml(lintFile)) {
                if (lintOldFile != null) {
                    lintOldFile.renameTo(lintFile)
                }
                project.getLogger().error("lint.xml is not exist")
            }
        }

        project.gradle.taskGraph.afterTask { task, TaskState state ->
            if (task == lintBaseTask) {
                lintFile.delete()
                if (lintOldFile != null) {
                    lintOldFile.renameTo(lintFile)

                }
                System.out.println()
                System.out.println("************************* xlint check end *************************");
            }
        }


        if (lintBaseTask != null && !project.tasks.getNames().contains("xlint")) {
            project.tasks.create("xlint").dependsOn lintBaseTask

        }

        if (extension.runAfterAssemble) {
            // execute lint task after assemble task
            assembleTask.finalizedBy project.tasks.getByName("xlint")
        }

    }

    // collect app and library variant
    private static DomainObjectCollection<BaseVariant> getAndroidVariants(Project project) {

        if (project.getPlugins().hasPlugin(AppPlugin)) {
            return project.getPlugins().getPlugin(AppPlugin).extension.applicationVariants
        }

        if (project.getPlugins().hasPlugin(LibraryPlugin)) {
            return project.getPlugins().getPlugin(LibraryPlugin).extension.libraryVariants
        }

    }


    boolean copyLintXml(File targetFile) {
        // TODO merge this 2 .xml file, not just copy.

        if (!targetFile.parentFile.exists()) {
            targetFile.parentFile.mkdirs()
        }

        InputStream lintIns = this.class.getResourceAsStream("/config/lint.xml")
        OutputStream outputStream = new FileOutputStream(targetFile)

        XUtils.copy(lintIns, outputStream)

        if (targetFile.exists()) {
            return true
        }
        return false
    }
}
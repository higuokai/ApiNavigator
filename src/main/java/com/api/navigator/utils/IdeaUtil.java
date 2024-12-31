package com.api.navigator.utils;

import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

public class IdeaUtil {

    public static void smartInvokeLater(Project project, Runnable runnable) {
        DumbService.getInstance(project).smartInvokeLater(runnable);
    }

    public static void backBackgroundTask(String title, Project project, Runnable runnable) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, title) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                runnable.run();
            }
        });
    }

    public static <T> T runRead(Project project, Computable<T> runnable) {
        return DumbService.getInstance(project).runReadActionInSmartMode(runnable);
    }

    public static void copyToClipboard(String content) {
        if (StringUtils.isNoneEmpty(content)) {
            CopyPasteManager.getInstance().setContents(new StringSelection(content));
        }
    }

    public static String getProjectKey(Project project) {
        return project.getLocationHash();
    }

}

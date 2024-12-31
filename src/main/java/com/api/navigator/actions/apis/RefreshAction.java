package com.api.navigator.actions.apis;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.api.navigator.constant.Icons;
import com.api.navigator.constant.ProjectCache;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RefreshAction extends DumbAwareAction {

    public RefreshAction() {
        super("Refresh", "", Icons.System.Refresh);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (Objects.isNull(project)) {
            return;
        }
        ProjectCache.removeApiCache(project);
        // 调用刷新
        ProjectCache.getApiTree(project).refreshApiTreeLater();
    }
}

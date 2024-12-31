package com.api.navigator.actions.apis;

import com.api.navigator.properties.MyPropertiesUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import com.api.navigator.constant.ProjectCache;
import org.jetbrains.annotations.NotNull;

public class EnableLibraryAction extends ToggleAction {

    public EnableLibraryAction() {
        super("Enable Library", "", AllIcons.ObjectBrowser.ShowLibraryContents);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return false;
        }
        return MyPropertiesUtil.getScanLib(project);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent anActionEvent, boolean state) {
        Project project = anActionEvent.getProject();
        if (project == null) {
            return;
        }
        // set scan
        MyPropertiesUtil.setScanLib(project, state);
        ProjectCache.getApiTree(project).refreshApiTreeLater();
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

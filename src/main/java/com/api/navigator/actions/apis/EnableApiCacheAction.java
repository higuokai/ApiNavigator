package com.api.navigator.actions.apis;

import com.api.navigator.constant.Icons;
import com.api.navigator.properties.MyPropertiesUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class EnableApiCacheAction extends ToggleAction {
    
    public EnableApiCacheAction() {
        super("Cache Api Before Refresh", "", Icons.System.Cache);
    }
    
    @Override
    public boolean isSelected(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            return false;
        }
        return MyPropertiesUtil.isCacheApi(project);
    }

    @Override
    public void setSelected(@NotNull AnActionEvent event, boolean value) {
        Project project = event.getProject();
        if (project == null) {
            return;
        }
        MyPropertiesUtil.setCacheApi(project, value);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

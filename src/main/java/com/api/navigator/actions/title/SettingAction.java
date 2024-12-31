package com.api.navigator.actions.title;

import com.api.navigator.constant.Icons;
import com.api.navigator.properties.SettingDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class SettingAction extends DumbAwareAction {
    
    public SettingAction() {
        super("Setting", "", Icons.System.Setting);
    }
    
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        SettingDialog.show(anActionEvent.getProject());
    }
}

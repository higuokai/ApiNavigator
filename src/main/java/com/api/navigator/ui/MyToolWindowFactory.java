package com.api.navigator.ui;

import com.api.navigator.actions.title.SettingAction;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.api.navigator.ui.apis.ApiPanel;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.getInstance();
        ContentManager contentManager = toolWindow.getContentManager();

        // 根据配置, 加载不同的窗口(先加载api)
        ApiPanel apiPanel = new ApiPanel(project);
        Content apisContent = contentFactory.createContent(apiPanel, "", false);
        contentManager.addContent(apisContent);
        
        // titleAction
        toolWindow.setTitleActions(Lists.newArrayList(new SettingAction()));
    }
}

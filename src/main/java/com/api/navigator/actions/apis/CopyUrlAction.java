package com.api.navigator.actions.apis;

import com.api.navigator.properties.MyPropertiesUtil;
import com.api.navigator.utils.Notify;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.api.navigator.constant.Icons;
import com.api.navigator.constant.TreeDataKey;
import com.api.navigator.model.ApiService;
import com.api.navigator.utils.IdeaUtil;
import com.api.navigator.utils.PathUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopyUrlAction extends DumbAwareAction {

    public CopyUrlAction() {
        super("Copy Url", "", Icons.System.CopyFull);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        List<ApiService> serviceItems = TreeDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        if (CollectionUtils.isEmpty(serviceItems)) {
            return;
        }
        ApiService apiService = serviceItems.get(0);
        // 获取模块名
        String moduleName = apiService.getModuleName();

        Integer port = MyPropertiesUtil.getModulePort(project, moduleName);
        String context = MyPropertiesUtil.getModuleContext(project, moduleName);

        String url = PathUtil.buildUrl("http", port, context, apiService.getPath());
        IdeaUtil.copyToClipboard(url);
        Notify.getInstance(project).info("copy success");
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

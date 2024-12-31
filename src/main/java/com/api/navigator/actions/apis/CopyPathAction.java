package com.api.navigator.actions.apis;

import com.api.navigator.utils.Notify;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.api.navigator.constant.Icons;
import com.api.navigator.constant.TreeDataKey;
import com.api.navigator.model.ApiService;
import com.api.navigator.utils.IdeaUtil;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CopyPathAction extends DumbAwareAction {

    public CopyPathAction() {
        super("Copy Path", "", Icons.System.Copy);
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
        IdeaUtil.copyToClipboard(apiService.getPath());
        Notify.getInstance(project).info("copy success");
    }
}

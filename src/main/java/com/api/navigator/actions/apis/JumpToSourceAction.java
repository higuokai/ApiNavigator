package com.api.navigator.actions.apis;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.util.PsiNavigateUtil;
import com.api.navigator.constant.Icons;
import com.api.navigator.constant.TreeDataKey;
import com.api.navigator.model.ApiService;
import com.api.navigator.model.spring.SpringApiService;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JumpToSourceAction extends DumbAwareAction {

    public JumpToSourceAction() {
        super("Jump to Source", "", Icons.System.Jump);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        List<ApiService> items = TreeDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        boolean match = items != null && items.stream()
                .allMatch(restItem -> restItem instanceof SpringApiService);
        e.getPresentation().setVisible(match);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<ApiService> itemList = TreeDataKey.SELECTED_SERVICE.getData(e.getDataContext());
        if (CollectionUtils.isNotEmpty(itemList)) {
            itemList.stream().filter(item -> item instanceof SpringApiService)
                    .findFirst()
                    .ifPresent(item -> PsiNavigateUtil.navigate(((SpringApiService) item).getPsiElement()));
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
}

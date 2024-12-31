package com.api.navigator.actions.apis;

import com.api.navigator.properties.MyPropertiesUtil;
import com.api.navigator.utils.Notify;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.api.navigator.constant.TreeDataKey;
import com.api.navigator.model.ApiModule;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 右键模块弹出编辑框
 */
public class ModulePortEditAction extends DumbAwareAction {

    public ModulePortEditAction() {
        super("Edit Port", "", AllIcons.CodeWithMe.CwmPermissionEdit);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }
        List<ApiModule> apiModules = TreeDataKey.SELECTED_MODULE_SERVICE.getData(e.getDataContext());
        if (CollectionUtils.isEmpty(apiModules)) {
            return;
        }
        ApiModule apiModule = apiModules.get(0);

        Integer oldPort = MyPropertiesUtil.getModulePort(project, apiModule.getModuleName());

        String inputPort = Messages.showInputDialog(project, "Input module server.port", "Edit Server Port", null, oldPort==null? "8080":String.valueOf(oldPort), new InputValidator() {
            @Override
            public boolean checkInput(String inputString) {
                return !StringUtil.isEmpty(inputString);
            }
            @Override
            public boolean canClose(String inputString) {
                return true;
            }
        });
        if (StringUtil.isEmpty(inputPort)) {
            return;
        }
        try {
            int portInteger = Integer.parseInt(inputPort);
            if (portInteger < 1 || portInteger > 65535) {
                throw new IllegalArgumentException();
            }
            MyPropertiesUtil.setModulePort(project, apiModule.getModuleName(), portInteger);
        } catch (Exception ex) {
            // 非数字或 1-65535, 不动
            Notify.getInstance(project).warning("set module port error:" + inputPort);
        }
    }
}

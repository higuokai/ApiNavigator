package com.api.navigator.ui.apis;

import com.api.navigator.actions.apis.*;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;

public class ApiPanel extends SimpleToolWindowPanel {

    public ApiPanel(Project project) {
        super(true);
        
        // toolbar
        this.initActionToolbar();
        // tree
        ApiTree apiTree = new ApiTree(project);
        setContent(apiTree);
    }

    private void initActionToolbar() {
        ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        // actions
        actionGroup.addAction(new RefreshAction());
        actionGroup.addAction(new SearchEverywhereAction());
        actionGroup.addSeparator();

        actionGroup.addAction(new ExpandAllAction());
        actionGroup.add(new CollapseAllAction());
        actionGroup.addSeparator();

        actionGroup.addAction(new ModuleFilterAction());
        actionGroup.addAction(new EnableLibraryAction());
        actionGroup.addAction(new EnableApiCacheAction());
        
        ActionToolbar actionToolbar = actionManager.createActionToolbar(ActionPlaces.TOOLWINDOW_TOOLBAR_BAR,
                actionGroup,
                true);
        
        setToolbar(actionToolbar.getComponent());
        actionToolbar.setTargetComponent(this);
    }
}

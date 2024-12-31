package com.api.navigator.actions.apis;

import com.api.navigator.properties.MyPropertiesUtil;
import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.ElementsChooser;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.util.containers.ContainerUtil;
import com.api.navigator.constant.Icons;
import com.api.navigator.constant.ProjectCache;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ModuleFilterAction extends ToggleAction {

    private JBPopup myFilterPopup;

    public ModuleFilterAction() {
        super("Module Filter", "", Icons.System.ModuleFilter);
    }

    @Override
    public boolean isSelected(@NotNull AnActionEvent anActionEvent) {
        return myFilterPopup != null && !myFilterPopup.isDisposed();
    }

    @Override
    public void setSelected(@NotNull AnActionEvent anActionEvent, boolean state) {
        if (state) {
            showPopup(anActionEvent);
        } else {
            if (myFilterPopup != null && !myFilterPopup.isDisposed()) {
                myFilterPopup.cancel();
            }
        }
    }

    private void showPopup(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        if (myFilterPopup != null) {
            return;
        }
        JBPopupListener popupCloseListener = new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                myFilterPopup = null;
            }
        };
        myFilterPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(createFilterPanel(project), null)
                .setModalContext(false)
                .setFocusable(true)
                .setRequestFocus(true)
                .setResizable(true)
                .setCancelOnClickOutside(true)
                .setMinSize(new Dimension(200, 200))
//                                      .setDimensionServiceKey(project, getDimensionServiceKey(), false)
                .addListener(popupCloseListener)
                .createPopup();
        Component anchor = e.getInputEvent().getComponent();
        if (anchor.isValid()) {
            myFilterPopup.showUnderneathOf(anchor);
        } else {
            Component component = e.getData(PlatformDataKeys.CONTEXT_COMPONENT);
            if (component != null) {
                myFilterPopup.showUnderneathOf(component);
            } else {
                myFilterPopup.showInFocusCenter();
            }
        }
    }

    private JComponent createFilterPanel(Project project) {
        ElementsChooser<?> chooser = createChooser(project);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chooser);
        JPanel buttons = new JPanel();
        JButton all = new JButton(IdeBundle.message("big.popup.filter.button.all"));
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                chooser.setAllElementsMarked(true);
            }
        });
        buttons.add(all);
        JButton none = new JButton(IdeBundle.message("big.popup.filter.button.none"));
        none.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                chooser.setAllElementsMarked(false);
            }
        });
        buttons.add(none);
        JButton invert = new JButton(IdeBundle.message("big.popup.filter.button.invert"));
        invert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                chooser.invertSelection();
            }
        });
        buttons.add(invert);
        panel.add(buttons);
        return panel;
    }

    private ElementsChooser<String> createChooser(@NotNull Project project) {
        List<String> allModules = ProjectCache.getAllModules(project);
        
        ElementsChooser<String> res = new ElementsChooser<String>(allModules, false) {
            @Override
            protected String getItemText(@NotNull String value) {
                return value;
            }
        };
        res.markElements(ContainerUtil.filter(allModules, e -> MyPropertiesUtil.isModuleVisible(project, e)));

        ElementsChooser.ElementsMarkListener<String> listener = (x, y) -> {
            MyPropertiesUtil.setModuleVisible(project, x, y);
        };
        res.addElementsMarkListener(listener);
        return res;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }
    
}

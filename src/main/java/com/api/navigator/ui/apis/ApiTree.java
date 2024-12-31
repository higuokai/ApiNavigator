package com.api.navigator.ui.apis;

import com.api.navigator.constant.TreeDataKey;
import com.api.navigator.ui.apis.tree.ApiNode;
import com.api.navigator.ui.apis.tree.BaseNode;
import com.api.navigator.ui.apis.tree.ModuleNode;
import com.api.navigator.ui.apis.tree.RootNode;
import com.google.common.collect.Lists;
import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.AppUIUtil;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.tree.AsyncTreeModel;
import com.intellij.ui.tree.StructureTreeModel;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.ui.treeStructure.SimpleTreeStructure;
import com.intellij.util.ui.tree.TreeUtil;
import com.api.navigator.model.ApiModule;
import com.api.navigator.model.ApiService;
import com.api.navigator.utils.ApiServiceUtil;
import com.api.navigator.constant.ProjectCache;
import com.api.navigator.utils.IdeaUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApiTree extends JPanel implements DataProvider, Disposable {

    private final Project project;
    
    private final SimpleTree apiTree;
    
    private final Lock lock = new ReentrantLock();

    @Getter
    private final StructureTreeModel<AbstractTreeStructure> treeModel;

    private final RootNode rootNode;

    public ApiTree(Project project) {
        this.project = project;

        rootNode = new RootNode(project, null);

        treeModel = new StructureTreeModel<>(new SimpleTreeStructure() {
            @Override
            public @NotNull Object getRootElement() {
                return rootNode;
            }
        }, null, this);

        apiTree = new SimpleTree(new AsyncTreeModel(treeModel, this));
        apiTree.setRootVisible(true);
        apiTree.setShowsRootHandles(true);
        apiTree.getEmptyText().clear();
        apiTree.setBorder(BorderFactory.createEmptyBorder());
        apiTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        // 布局
        setLayout(new BorderLayout());
        // 带滚动条的panel
        add(ScrollPaneFactory.createScrollPane(apiTree), BorderLayout.CENTER);

        // 树列表点击事件
        initEvent();

        ProjectCache.setApiTree(project, this);

        this.refreshApiTreeLater();
    }

    public void refreshApiTreeLater() {
        try {
            if(lock.tryLock()) {
                IdeaUtil.smartInvokeLater(project, () -> {
                    ToolWindow toolWindow = ProjectCache.getToolWindow(project);
                    if (toolWindow.isDisposed() || !toolWindow.isVisible()) {
                        toolWindow.show(this::refreshApiTreeBackground);
                    } else {
                        refreshApiTreeBackground();
                    }
                });
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 更新树列表
     */
    private void refreshApiTreeBackground() {
        // 禁用按钮
        Runnable backgroundTask = () -> {
            List<ApiModule> restModules = IdeaUtil.runRead(project, () -> {
                Map<String, List<ApiService>> apiServices = ApiServiceUtil.getApis(project);
                
                return apiServices
                        .entrySet()
                        .stream()
                        .map(entry -> new ApiModule(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());
            });
            // 重新渲染树
            AppUIUtil.invokeOnEdt(() -> rootNode.updateModuleNodes(restModules));
            
            // 启用按钮
        };
        IdeaUtil.backBackgroundTask("ApiNavigator Search Restful Apis...", project, backgroundTask);
    }


    /**
     * 点击事件
     */
    private void initEvent() {
        Function<Collection<? extends BaseNode>, String> getMenuIdFunction = nodes -> {
            return Optional.ofNullable(nodes)
                    .orElse(Collections.emptyList())
                    .stream()
                    .filter(e -> StringUtils.isNotBlank(e.getMenuId()))
                    .findFirst()
                    .map(BaseNode::getMenuId)
                    .orElse(null);
        };

        apiTree.addMouseListener(new PopupHandler() {
            @Override
            public void invokePopup(Component comp, int x, int y) {
                String menuId = getMenuIdFunction.apply(getSelectedNodes());
                if (menuId != null) {
                    final ActionManager actionManager = ActionManager.getInstance();
                    final ActionGroup actionGroup = (ActionGroup) actionManager.getAction(menuId);
                    if (actionGroup != null) {
                        JPopupMenu component = actionManager.createActionPopupMenu(ActionPlaces.TOOLWINDOW_CONTENT, actionGroup).getComponent();
                        component.show(comp, x, y);
                    }
                }
            }
        });
    }

    /**
     * 返回节点数据
     */
    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if (TreeDataKey.ALL_SERVICE.is(dataId)) {
            return rootNode.getChildrenNodes();
        }
        if (TreeDataKey.ALL_MODULE.is(dataId)) {
            return rootNode.getChildrenNodes().stream()
                    .map(moduleNode -> ((ModuleNode)moduleNode).getApiModule().getModuleName())
                    .distinct()
                    .collect(Collectors.toList());
        }
        // getMenu保证了不能选择不同类型节点
        if (TreeDataKey.SELECTED_SERVICE.is(dataId)) {
            List<ApiService> list = Lists.newArrayList();
            for (BaseNode node : getSelectedNodes()) {
                if (node instanceof RootNode) {
                    return rootNode.getApiServices();
                } else if (node instanceof ModuleNode) {
                    list.addAll(node.getApiServices());
//                    return node;
                } else if (node instanceof ApiNode) {
                    list.add(((ApiNode)node).getApiService());
                }
            }
            return list;
        }
        if (TreeDataKey.SELECTED_MODULE_SERVICE.is(dataId)) {
            return getSelectedNodes().stream()
                    .filter(node -> node instanceof ModuleNode)
                    .map(node -> ((ModuleNode)node).getApiModule())
                    .collect(Collectors.toList());
        }
        return null;
    }

    private java.util.List<BaseNode> getSelectedNodes() {
        final List<BaseNode> filtered = new ArrayList<>();
        TreePath[] treePaths = apiTree.getSelectionPaths();
        if (treePaths != null) {
            for (TreePath treePath : treePaths) {
                SimpleNode nodeFor = apiTree.getNodeFor(treePath);
                if (!(nodeFor instanceof BaseNode)) {
                    filtered.clear();
                    break;
                }
                filtered.add((BaseNode) nodeFor);
            }
        }
        return filtered;
    }

    @Override
    public void dispose() {
        // 销毁事件
        ProjectCache.close(this.project);
    }

    public void expandAll(boolean expand) {
        if (expand) {
            TreeUtil.expandAll(apiTree);
        } else {
            TreeUtil.collapseAll(apiTree, false, 1);
        }
    }

//    @Override
//    public void uiDataSnapshot(@NotNull DataSink dataSink) {
//        
//    }
    
}

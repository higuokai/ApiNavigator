package com.api.navigator.ui.apis.tree;

import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.CachingSimpleNode;
import com.intellij.ui.treeStructure.SimpleNode;
import com.api.navigator.model.ApiService;
import com.api.navigator.constant.ProjectCache;
import lombok.Getter;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseNode extends CachingSimpleNode {

    protected List<BaseNode> childrenNodes = Lists.newArrayList();

    protected BaseNode(SimpleNode parent) {
        super(parent);
    }

    public BaseNode(Project project, NodeDescriptor nodeDescriptor) {
        super(project, nodeDescriptor);
        childrenNodes = new ArrayList<>();
    }

    /**
     * 获取点击的菜单ID
     */
    public abstract String getMenuId();

    protected void updateUpTo(SimpleNode node) {
        SimpleNode each = node;
        while (each != null) {
            updateFrom(each);
            each = each.getParent();
        }
    }

    protected void updateFrom(SimpleNode node) {
        if (node != null) {
            node.update();
            ProjectCache.getApiTree(getProject()).getTreeModel().invalidate();
        }
    }

    protected void childrenChanged() {
        BaseNode each = this;
        while (each != null) {
            each.cleanUpCache();
            each = (BaseNode) each.getParent();
        }
        updateUpTo(this);
    }

    public List<ApiService> getApiServices() {
        return childrenNodes.stream().flatMap(moduleNode -> moduleNode.getApiServices().stream()).toList();
    }
}

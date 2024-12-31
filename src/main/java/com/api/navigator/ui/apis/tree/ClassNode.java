package com.api.navigator.ui.apis.tree;

import com.api.navigator.constant.Icons;
import com.api.navigator.model.ApiClass;
import com.api.navigator.model.ApiService;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.treeStructure.SimpleNode;

import java.util.List;
import java.util.stream.Collectors;

public class ClassNode extends BaseNode {

    private final ApiClass apiClass;
    
    protected ClassNode(SimpleNode parent, ApiClass apiClass) {
        super(parent);
        this.apiClass = apiClass;
        getTemplatePresentation().setIcon(Icons.Node.ClassNode);

        List<ApiService> apiServices = apiClass.getApiServices();

        this.childrenNodes.clear();
        this.childrenNodes = apiServices.stream().map(e -> new ApiNode(this, e)).collect(Collectors.toList());
        if (parent != null) {
            ((BaseNode) parent).cleanUpCache();
        }
        updateFrom(parent);
        childrenChanged();
        updateUpTo(this);
    }

    @Override
    public String getMenuId() {
        return "";
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return childrenNodes.toArray(new SimpleNode[0]);
    }

    @Override
    public @NlsSafe String getName() {
        return this.apiClass.getName();
    }
}

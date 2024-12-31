package com.api.navigator.ui.apis.tree;

import com.api.navigator.model.ApiClass;
import com.api.navigator.properties.DefaultFalseConfig;
import com.api.navigator.properties.MyPropertiesUtil;
import com.intellij.ui.treeStructure.SimpleNode;
import com.api.navigator.constant.Icons;
import com.api.navigator.model.ApiModule;
import com.api.navigator.model.ApiService;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class ModuleNode extends BaseNode{

    private ApiModule apiModule;

    protected ModuleNode(SimpleNode aParent, ApiModule apiModule) {
        super(aParent);
        this.apiModule = apiModule;

        getTemplatePresentation().setIcon(Icons.Node.ModuleNode);

        // 判断使用apiNode还是classNode
        boolean showClass = MyPropertiesUtil.get(getProject(), DefaultFalseConfig.SHOW_CLASS_NODE);
        if (showClass) {
            List<ApiClass> apiClasses = apiModule.getApiClasses();

            this.childrenNodes.clear();
            this.childrenNodes = apiClasses.stream().map(e -> new ClassNode(this, e)).collect(Collectors.toList());
        } else {
            List<ApiService> apiServices = apiModule.getApiServices();

            this.childrenNodes.clear();
            this.childrenNodes = apiServices.stream().map(e -> new ApiNode(this, e)).collect(Collectors.toList());
        }
        
        SimpleNode parent = getParent();
        if (parent != null) {
            ((BaseNode) parent).cleanUpCache();
        }
        updateFrom(parent);
        childrenChanged();
        updateUpTo(this);
    }

    @Override
    public String getMenuId() {
        return "apis.moduleMenu";
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return childrenNodes.toArray(new SimpleNode[0]);
    }

    @Override
    public String getName() {
        return apiModule.getModuleName();
    }

    @Override
    public List<ApiService> getApiServices() {
        if (childrenNodes.isEmpty()) {
            return Collections.emptyList();
        } else {
            return childrenNodes.stream().map(e -> (ApiService)((ApiNode)e).getApiService()).toList();
        }
    }

}

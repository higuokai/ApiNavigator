package com.api.navigator.ui.apis.tree;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.treeStructure.SimpleNode;
import com.intellij.ui.treeStructure.SimpleTree;
import com.intellij.util.PsiNavigateUtil;
import com.api.navigator.constant.Icons;
import com.api.navigator.model.ApiService;
import com.api.navigator.model.spring.SpringApiService;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.event.InputEvent;

@Setter
@Getter
public class ApiNode extends BaseNode{

    private ApiService apiService;

    public ApiNode(SimpleNode aParent, ApiService apiService) {
        super(aParent);

        this.apiService = apiService;
        Icon icon = Icons.Node.getServiceNodeIcon(apiService);
        getTemplatePresentation().setIcon(icon);

        NavigatablePsiElement psiElement = apiService.getPsiElement();
        if (psiElement instanceof PsiMethod) {
            PsiClass containingClass = ((PsiMethod) psiElement).getContainingClass();
            if (containingClass != null) {
                getTemplatePresentation().setTooltip(containingClass.getName() + "#" + psiElement.getName());
            }
        }
    }

    @Override
    public void handleDoubleClickOrEnter(SimpleTree tree, InputEvent inputEvent) {
        ApiNode selectedNode = (ApiNode) tree.getSelectedNode();
        if (selectedNode != null && selectedNode.apiService instanceof SpringApiService) {
            PsiElement psiElement = selectedNode.apiService.getPsiElement();
            if (!psiElement.isValid()) {
//                LOG.info("psiMethod is invalid: " + psiElement);
                return;
            }
            PsiNavigateUtil.navigate(psiElement);
        }
    }

    @Override
    public String getMenuId() {
        return "apis.requestMenu";
    }

    @Override
    public String getName() {
        return apiService.getName();
    }

    @Override
    protected SimpleNode[] buildChildren() {
        return new SimpleNode[0];
    }
}

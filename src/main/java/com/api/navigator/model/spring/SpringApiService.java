package com.api.navigator.model.spring;

import com.intellij.psi.NavigatablePsiElement;
import com.api.navigator.constant.HttpMethod;
import com.api.navigator.model.ApiService;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.javadoc.PsiDocComment;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class SpringApiService extends ApiService {

    private HttpMethod method;

    private String path;
    
    private String description;

    public SpringApiService(HttpMethod method, String path, NavigatablePsiElement psiElement) {
        super(psiElement);
        this.method = method;
        this.path = path;
        
        if (psiElement instanceof PsiMethod psiMethod) {
            PsiDocComment docComment = psiMethod.getDocComment();
            if (docComment != null) {
                StringBuilder descBuilder = new StringBuilder();
                for (PsiElement element : docComment.getDescriptionElements()) {
                    if (!(element instanceof PsiWhiteSpace)) {
                        descBuilder.append(element.getText().trim());
                        break;
                    }
                }
                description = descBuilder.toString();
            }
        }
    }

    @NotNull
    public SpringApiService copyWithParent(@Nullable SpringApiService parent) {
        SpringApiService apiService = new SpringApiService(this.getMethod(), this.getPath(), this.getPsiElement());
        if (parent != null) {
            apiService.setParent(parent);
        }
        return apiService;
    }

    private void setParent(@NotNull SpringApiService parent) {
        if ((this.getMethod() == null || this.getMethod() == HttpMethod.REQUEST) && parent.getMethod() != null) {
            this.setMethod(parent.getMethod());
        }
        String parentPath = parent.getPath();
        if (parentPath != null && parentPath.endsWith("/")) {
            // 去掉末尾的斜杠
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        }
        this.setPath(parentPath + this.getPath());
    }

    @Override
    public String getName() {
        return path + (StringUtils.isEmpty(description) ? "" : "    " + StringUtils.substring(description, 0, 10));
    }
}

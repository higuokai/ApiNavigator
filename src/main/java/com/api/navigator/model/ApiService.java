package com.api.navigator.model;

import com.intellij.psi.NavigatablePsiElement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ApiService {

    private NavigatablePsiElement psiElement;

    private String moduleName;

    public ApiService(NavigatablePsiElement psiElement) {
        this.psiElement = psiElement;
    }

    public abstract String getName();

    public abstract String getPath();

}

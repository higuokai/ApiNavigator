package com.api.navigator.model;

import com.intellij.psi.NavigatablePsiElement;
import lombok.Data;

import java.util.List;

@Data
public class ApiClass {

    private NavigatablePsiElement psiClass;
    
    private List<ApiService> apiServices;

    private String moduleName;
    
    public String getName() {
        return psiClass.getName();
    }
    
}

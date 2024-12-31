package com.api.navigator.model;

import com.api.navigator.properties.MyPropertiesUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiModule {

    private String moduleName;

    private List<ApiService> apiServices;
    
    private List<ApiClass> apiClasses;
    
    public ApiModule(String moduleName, List<ApiService> apiServices) {
        this.moduleName = moduleName;
        this.apiServices = apiServices;
    }
    
    @Override
    public String toString() {
        return moduleName;
    }
}

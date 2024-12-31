package com.api.navigator.properties;

import com.intellij.openapi.project.Project;

import java.util.Map;

public class MyPropertiesUtil {
    
    public static Integer getModulePort(Project project, String moduleName) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<String, Integer> modulePorts = projectProperties.getModulePorts();
        return modulePorts.get(moduleName);
    }
    
    public static void setModulePort(Project project, String moduleName, int port) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<String, Integer> modulePorts = projectProperties.getModulePorts();
        modulePorts.put(moduleName, port);
    }
    
    public static boolean get(Project project, DefaultFalseConfig config) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<DefaultFalseConfig, Boolean> map = projectProperties.getDefaultFalseConfig();
        return map.getOrDefault(config, false);
    }

    public static void set(Project project, DefaultFalseConfig config, boolean value) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<DefaultFalseConfig, Boolean> map = projectProperties.getDefaultFalseConfig();
        map.put(config, value);
    }

    public static boolean get(Project project, DefaultTrueConfig config) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<DefaultTrueConfig, Boolean> map = projectProperties.getDefaultTrueConfig();
        return map.getOrDefault(config, true);
    }

    public static void set(Project project, DefaultTrueConfig config, boolean value) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        Map<DefaultTrueConfig, Boolean> map = projectProperties.getDefaultTrueConfig();
        map.getOrDefault(config, value);
    }
    
    public static String getModuleContext(Project project, String moduleName) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        return projectProperties.getModuleContexts().get(moduleName);
    }
    
    public static void setModuleContext(Project project, String moduleName, String context) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        projectProperties.getModuleContexts().put(moduleName, context);
    }
    
    public static boolean isModuleVisible(Project project, String moduleName) {
        ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
        return !projectProperties.getDisabledModules().contains(moduleName);
    }
    
    public static void setModuleVisible(Project project, String moduleName, boolean value) {
        if (value) {
            ProjectPropertiesStorage projectProperties = project.getService(ProjectPropertiesStorage.class);
            projectProperties.getDisabledModules().remove(moduleName);
        }
    }

}

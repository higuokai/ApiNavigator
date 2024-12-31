package com.api.navigator.constant;

import com.api.navigator.utils.IdeaUtil;
import com.google.common.collect.Maps;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.api.navigator.model.ApiService;
import com.api.navigator.ui.apis.ApiTree;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class ProjectCache {

    private static final Map<String, ApiTree> TREE_MAP = Maps.newHashMap();

    private static final Map<String, Map<String, List<ApiService>>> API_CACHE = Maps.newHashMap();
    
    private static final Map<String, Set<String>> MODULE_NAME_CACHE = Maps.newHashMap();
    
    public static ToolWindow getToolWindow(Project project) {
        return Objects.requireNonNull(ToolWindowManager.getInstance(project).getToolWindow(Constant.TOOLWINDOW_ID));
    }

    public static void setApiTree(Project project, ApiTree apiTree) {
        TREE_MAP.put(IdeaUtil.getProjectKey(project), apiTree);
    }

    public static ApiTree getApiTree(Project project) {
        return TREE_MAP.get(IdeaUtil.getProjectKey(project));
    }

    public static void close(Project project) {
        String projectKey = IdeaUtil.getProjectKey(project);
        TREE_MAP.remove(projectKey);
        API_CACHE.remove(projectKey);
        MODULE_NAME_CACHE.remove(projectKey);
    }

    public static void removeApiCache(Project project) {
        API_CACHE.remove(IdeaUtil.getProjectKey(project));
    }
    
    public static Map<String, List<ApiService>> getApiCache(Project project) {
        return API_CACHE.get(IdeaUtil.getProjectKey(project));
    }

    public static void setApiCache(Project project, Map<String, List<ApiService>> cacheVal) {
        API_CACHE.put(IdeaUtil.getProjectKey(project), cacheVal);
    }

    public static void setModules(Project project, Set<String> modules) {
        MODULE_NAME_CACHE.put(IdeaUtil.getProjectKey(project), modules);
    }
    
    public static List<String> getAllModules(Project project) {
        Set<String> resultSet = MODULE_NAME_CACHE.get(IdeaUtil.getProjectKey(project));
        return resultSet == null ? Lists.newArrayList() : new ArrayList<>(resultSet);
    }
    
}

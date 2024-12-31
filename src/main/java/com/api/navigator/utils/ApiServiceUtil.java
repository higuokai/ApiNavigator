package com.api.navigator.utils;

import com.api.navigator.constant.ProjectCache;
import com.api.navigator.properties.MyPropertiesUtil;
import com.api.navigator.properties.ProjectPropertiesStorage;
import com.google.common.collect.Maps;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.api.navigator.model.ApiService;
import com.api.navigator.scanner.ScannerHelper;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ApiServiceUtil {

    public static Map<String, List<ApiService>> getApis(@NotNull Project project) {
        // 如果启用了缓存, 直接从缓存获取, 否则重新获取
        boolean cacheApi = MyPropertiesUtil.isCacheApi(project);
        if (cacheApi) {
            Map<String, List<ApiService>> apiCache = ProjectCache.getApiCache(project);
            if (apiCache != null) {
                return apiCache;
            }
        }

        Module[] modules = ModuleManager.getInstance(project).getModules();
        // 缓存module
        ProjectCache.setModules(project, Arrays.stream(modules).map(Module::getName).collect(Collectors.toSet()));
        
        Map<String, List<ApiService>> resultMap = reScanApi(project, modules);
        ProjectCache.setApiCache(project, resultMap);
        return resultMap;
    }

    public static List<ApiService> getApisForEditor(@NotNull Project project) {
        return Arrays.stream(ModuleManager.getInstance(project).getModules())
                .flatMap(module -> getModuleApis(project, module).stream())
                .collect(Collectors.toList());
    }

    public static Map<String, List<ApiService>> reScanApi(@NotNull Project project, @NotNull Module[] modules) {
        Map<String, List<ApiService>> resultMap = Maps.newHashMapWithExpectedSize(modules.length);

        for (Module module : modules) {
            if (!MyPropertiesUtil.isModuleVisible(project, module.getName())) {
                continue;
            }
            List<ApiService> moduleApis = getModuleApis(project, module);
            if (CollectionUtils.isEmpty(moduleApis)) {
                continue;
            }
            resultMap.put(module.getName(), moduleApis);
        }
        return resultMap;
    }

    public static List<ApiService> getModuleApis(@NotNull Project project, @NotNull Module module) {
        List<ApiService> resultList = new ArrayList<>();
        List<ScannerHelper> javaHelpers = ScannerHelper.getJavaHelpers();
        for (ScannerHelper javaHelper : javaHelpers) {
            Collection<ApiService> services = javaHelper.getService(project, module);
            if (CollectionUtils.isEmpty(services)) {
                continue;
            }
            resultList.addAll(services);
        }
        return resultList;
    }

    public static String getCombinedPath(@NotNull String typePath, @NotNull String methodPath) {
        if (typePath.isEmpty()) {
            typePath = "/";
        } else if (!typePath.startsWith("/")) {
            typePath = "/".concat(typePath);
        }

        if (!methodPath.isEmpty()) {
            if (!methodPath.startsWith("/") && !typePath.endsWith("/")) {
                methodPath = "/".concat(methodPath);
            }
        }

        return (typePath + methodPath).replace("//", "/");
    }

}

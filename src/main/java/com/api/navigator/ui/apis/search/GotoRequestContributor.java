package com.api.navigator.ui.apis.search;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.api.navigator.model.ApiService;
import com.api.navigator.utils.ApiServiceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GotoRequestContributor implements ChooseByNameContributor {

    private transient final List<RestSearchItem> cacheSearchItemList = new ArrayList<>();

    public GotoRequestContributor(Module module) {
    }

    @Override
    public String @NotNull [] getNames(Project project, boolean includeNonProjectItems) {

        Map<String, List<ApiService>> apiServices = ApiServiceUtil.getApis(project);

        cacheSearchItemList.clear();

        return apiServices
                .entrySet().stream()
                .flatMap(i -> i.getValue().stream())
                .peek(e -> cacheSearchItemList.add(new RestSearchItem(e)))
                .map(ApiService::getPath).toArray(String[]::new);
    }

    @Override
    public NavigationItem @NotNull [] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
        List<RestSearchItem> patternList = cacheSearchItemList.stream().filter(e -> Objects.requireNonNull(e.getName()).contains(name)).toList();
        return patternList.toArray(new NavigationItem[0]);
    }
}

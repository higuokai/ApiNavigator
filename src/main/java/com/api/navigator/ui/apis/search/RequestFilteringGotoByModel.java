package com.api.navigator.ui.apis.search;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.gotoByName.CustomMatcherModel;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.api.navigator.constant.HttpMethod;
import com.api.navigator.model.spring.SpringApiService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 接口过滤模型
 */
public class RequestFilteringGotoByModel
        extends FilteringGotoByModel<HttpMethod>
        implements DumbAware, CustomMatcherModel {

    public RequestFilteringGotoByModel(
            @NotNull Project project, @NotNull ChooseByNameContributor[] contributors) {
        super(project, contributors);
    }

    @Nullable
    @Override
    protected HttpMethod filterValueFor(NavigationItem item) {
        if (item instanceof RestSearchItem) {
            return ((SpringApiService)((RestSearchItem) item).getApiService()).getMethod();
        }
        return null;
    }

    @Override
    public String getPromptText() {
        return "Enter service API";
    }

    @NotNull
    @Override
    public String getNotInMessage() {
        return IdeBundle.message("label.no.matches.found", getProject().getName());
    }

    @NotNull
    @Override
    public String getNotFoundMessage() {
        return IdeBundle.message("label.no.matches.found");
    }

    @Nullable
    @Override
    public String getCheckBoxName() {
        return null;
    }

    @Override
    public boolean loadInitialCheckBoxState() {
        return false;
    }

    @Override
    public void saveInitialCheckBoxState(boolean state) {
    }

    @NotNull
    @Override
    public String @NotNull [] getSeparators() {
        return new String[]{"/", "?"};
    }

    @Nullable
    @Override
    public String getFullName(@NotNull Object element) {
        return getElementName(element);
    }

    @Override
    public boolean willOpenEditor() {
        return true;
    }

    @Override
    public boolean matches(@NotNull String popupItem, @NotNull String userPattern) {
        return true;
    }
}

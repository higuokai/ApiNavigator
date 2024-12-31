package com.api.navigator.constant;

import com.intellij.openapi.actionSystem.DataKey;
import com.api.navigator.model.ApiModule;
import com.api.navigator.model.ApiService;

import java.util.List;

public class TreeDataKey {

    public static final DataKey<List<ApiService>> ALL_SERVICE = DataKey.create("ALL_SERVICE");

    public static final DataKey<List<ApiService>> SELECTED_SERVICE = DataKey.create("SELECTED_SERVICE");

    public static final DataKey<List<ApiModule>> SELECTED_MODULE_SERVICE = DataKey.create("SELECTED_MODULE_SERVICE");

    public static final DataKey<List<String>> ALL_MODULE = DataKey.create("ALL_MODULE");

}

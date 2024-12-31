package com.api.navigator.properties;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Setter
@Getter
@State(
        name = "ApiNavigator",
        storages = @Storage("ApiNavigator.xml"),
        category = SettingsCategory.CODE
)
@Service(Service.Level.PROJECT)
public final class ProjectPropertiesStorage implements PersistentStateComponent<ProjectPropertiesStorage> {
    
    private Map<String, Integer> modulePorts = Maps.newHashMap();
    
    private Map<String, String> moduleContexts = Maps.newHashMap();
    
    private Set<String> disabledModules = Sets.newHashSet();

    private Map<DefaultFalseConfig, Boolean> defaultFalseConfig = Maps.newHashMap();

    private Map<DefaultTrueConfig, Boolean> defaultTrueConfig = Maps.newHashMap();
    
    @Override
    public @NotNull ProjectPropertiesStorage getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ProjectPropertiesStorage state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}

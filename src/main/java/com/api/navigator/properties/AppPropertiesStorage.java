//package com.api.navigator.properties;
//
//import com.intellij.openapi.components.*;
//import com.intellij.util.xmlb.XmlSerializerUtil;
//import lombok.Getter;
//import lombok.Setter;
//import org.jetbrains.annotations.NotNull;
//
//@Setter
//@Getter
//@State(
//        name = "ApiNavigator",
//        storages = @Storage("ApiNavigator.xml"),
//        category = SettingsCategory.CODE
//)
//@Service(Service.Level.APP)
//public final class AppPropertiesStorage implements PersistentStateComponent<AppPropertiesStorage> {
//
//    private boolean scanWithLib = false;
//    
//    @Override
//    public @NotNull AppPropertiesStorage getState() {
//        return this;
//    }
//
//    @Override
//    public void loadState(@NotNull AppPropertiesStorage state) {
//        XmlSerializerUtil.copyBean(state, this);
//    }
//}

package com.api.navigator.constant;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import com.api.navigator.model.ApiService;
import com.api.navigator.model.spring.SpringApiService;

import javax.swing.*;

public class Icons {

    public static class HTTP {

        public static class METHOD {
            public static Icon get(HttpMethod method) {
                if (method == null) {
                    return UNDEFINED;
                }
                return switch (method) {
                    case GET -> GET;
                    case POST -> POST;
                    case PUT -> PUT;
                    case PATCH -> PATCH;
                    case DELETE -> DELETE;
                    case REQUEST -> ALL;
                    default -> UNDEFINED;
                };
            }
            private static final Icon GET = load("/icons/method/GET.svg");
            private static final Icon PUT = load("/icons/method/PUT.svg");
            private static final Icon POST = load("/icons/method/POST.svg");
            private static final Icon PATCH = load("/icons/method/PATCH.svg");
            private static final Icon DELETE = load("/icons/method/DELETE.svg");
            public static final Icon ALL = load("/icons/method/ALL.svg");
            private static final Icon UNDEFINED = load("/icons/method/UNDEFINED.svg");
        }
    }

    public static class System {
        public static final Icon Refresh = AllIcons.Actions.Refresh;
        public static final Icon ModuleFilter = load("/icons/system/moduleFilter.svg");
        public static final Icon Copy = load("/icons/system/copy.svg");
        public static final Icon CopyFull = load("/icons/system/goto.svg");
        public static final Icon Jump = load("/icons/system/goto.svg");
        public static final Icon Setting = AllIcons.General.Settings;
        public static final Icon Cache = load(("/icons/system/cache.svg"));
    }

    public static class Node {
        public static final Icon TreeRootNode = AllIcons.Nodes.ModuleGroup;
        public static final Icon ModuleNode = AllIcons.Nodes.Module;
        public static final Icon ClassNode = AllIcons.Nodes.Class;
        
        
        public static Icon getServiceNodeIcon(ApiService apiService) {
            return HTTP.METHOD.get(((SpringApiService)apiService).getMethod());
        }
    }

    public static final Icon PluginIcon = load(("/icons/favicon.svg"));
    
    private static Icon load(String path) {
        return IconLoader.getIcon(path, Icons.class);
    }
}

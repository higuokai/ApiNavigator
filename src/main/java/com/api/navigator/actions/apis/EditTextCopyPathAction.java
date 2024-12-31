package com.api.navigator.actions.apis;

import com.api.navigator.constant.Icons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.api.navigator.utils.Notify;
import com.api.navigator.model.ApiService;
import com.api.navigator.utils.ApiServiceUtil;
import com.api.navigator.utils.IdeaUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EditTextCopyPathAction extends DumbAwareAction {

    public EditTextCopyPathAction() {
        super("Copy Path", "", Icons.PluginIcon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if(project == null) {
            return;
        }

        PsiMethod psiMethod = getPsiMethod(e);
        if (psiMethod == null) {
            Notify.getInstance(project).warning("No service here.");
            return;
        }

        Optional<ApiService> any = ApiServiceUtil
                .getApisForEditor(project)
                .stream()
                .filter(element -> element.getPsiElement().equals(psiMethod))
                .findAny();

        if (any.isEmpty()) {
            Notify.getInstance(project).warning("No service here.");
            return;
        }

        IdeaUtil.copyToClipboard(any.get().getPath());
        Notify.getInstance(project).info("copy success");
    }

    @Nullable
    public static PsiElement getCurrentEditorElement(@NotNull AnActionEvent e) {
        Editor editor = e.getData(LangDataKeys.EDITOR);
        if (editor == null) {
            return null;
        }
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (psiFile == null) {
            return null;
        }
        return psiFile.findElementAt(editor.getCaretModel().getOffset());
    }

    @Nullable
    public static PsiMethod getPsiMethod(@NotNull AnActionEvent event) {
        PsiElement currentEditorElement = getCurrentEditorElement(event);
        if (currentEditorElement == null) {
            return null;
        }
        // 如果右键处为当前方法其中的 注解末尾 或 方法体中
        PsiElement editorElementContext = currentEditorElement.getContext();
        if (editorElementContext instanceof PsiMethod) {
            return ((PsiMethod) editorElementContext);
        }
        return null;
    }
}

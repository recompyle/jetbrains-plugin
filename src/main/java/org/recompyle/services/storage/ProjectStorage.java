package org.recompyle.services.storage;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.recompyle.services.DebugService.Logger;
import static org.recompyle.services.ProjectService.getIdeRootPath;


@State(
        name = "recompyle",
        storages = {
                @Storage("recompyle.xml")
        }
)
public class ProjectStorage implements PersistentStateComponent<ProjectStorage> {

    public static class ProjectConfig {

        public ProjectConfig() {
            this.port = "3101";
            this.enabled = true;
            this.openedFilesListener = true;
            this.breakpointListener = true;
            this.followCursor = false;
            this.fileSave = false;
        }

        public String port;
        public Boolean enabled;
        public Boolean openedFilesListener;
        public Boolean breakpointListener;
        public Boolean followCursor; // will be used in future versions
        public Boolean fileSave; // will be used in future versions
    }

    public static class AppConfig {
        public String ideRoot;
        public ProjectConfig config;

        public AppConfig() {
            this.config = new ProjectConfig();
        }
    }

    public List<AppConfig> projectConfigList = new ArrayList<AppConfig>();


    @Override
    public @Nullable ProjectStorage getState() {
        System.out.println("get state event " + this.projectConfigList.size());
        // Filter out AppConfig instances that should not be serialized.
        ProjectStorage stateToSerialize = new ProjectStorage();
        for (AppConfig appConfig : this.projectConfigList) {
            if (shouldBeSerialized(appConfig)) {
                stateToSerialize.projectConfigList.add(appConfig);
            }
        }
        return stateToSerialize.projectConfigList.isEmpty() ? null : stateToSerialize;
    }

    private boolean shouldBeSerialized(AppConfig appConfig) {
        // Check if ideRoot is not null or empty
        if (appConfig.ideRoot == null || appConfig.ideRoot.trim().isEmpty()) {
            return false;
        }

        // Check if at least one setting in config is not at its default value
        ProjectConfig config = appConfig.config;
        if (config == null) {
            return false;
        }

        ProjectConfig pjc = new ProjectConfig();

        // Assuming default values for ProjectConfig are as defined in the constructor
        boolean isDefault = Objects.equals(config.port, pjc.port) &&
                Objects.equals(config.enabled, pjc.enabled) &&
                Objects.equals(config.openedFilesListener, pjc.openedFilesListener) &&
                Objects.equals(config.breakpointListener, pjc.breakpointListener) &&
                Objects.equals(config.followCursor, pjc.followCursor) &&
                Objects.equals(config.fileSave, pjc.fileSave);

        return !isDefault; // Serialize if not all settings are default
    }

    @Override
    public void loadState(@NotNull ProjectStorage state) {
        System.out.println("load state event " + state.toString());
        XmlSerializerUtil.copyBean(state, this);
    }


    public static ProjectStorage getInstance() {
        return ServiceManager.getService(ProjectStorage.class);
    }


    public AppConfig getConfigForIdeRoot(String IdeRoot) {
//        Logger("getConfigForProjectPath");
//        Logger(projectPath);

        AppConfig mAppConfig = null;

        for (AppConfig appConf : this.projectConfigList) {
            if (Objects.equals(appConf.ideRoot, IdeRoot)) {
                mAppConfig = appConf;
            }
        }
        if (mAppConfig == null) {
            AppConfig appConfig = new AppConfig();
            appConfig.ideRoot = IdeRoot;
            mAppConfig = appConfig;
            this.projectConfigList.add(appConfig);
        }

        return mAppConfig;
    }

    public AppConfig getConfigForActionEvent(AnActionEvent e) {
        return getConfigForProject(e.getProject());
    }

    public AppConfig getConfigForProject(Project pj) {
        String projectPath = getIdeRootPath(pj);
        return getConfigForIdeRoot(projectPath);
    }


}


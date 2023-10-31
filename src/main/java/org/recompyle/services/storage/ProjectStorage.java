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
            this.followCursor = false;
            this.fileSave = true;
            this.openedFilesListener = true;
            this.breakpointListener = true;
        }

        public String port;
        public Boolean followCursor;
        public Boolean fileSave;
        public Boolean openedFilesListener;
        public Boolean breakpointListener;
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
//        System.out.println("get  "+this.state.followCursor.toString());
        return this;
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

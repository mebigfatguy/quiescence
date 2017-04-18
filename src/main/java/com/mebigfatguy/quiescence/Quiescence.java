package com.mebigfatguy.quiescence;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.property.LocalProperties;

public class Quiescence extends Task implements TaskContainer {

    private List<Task> nestedTasks = new ArrayList<Task>();

    public void addTask(Task nestedTask) {
        nestedTasks.add(nestedTask);
    }

    @Override
    public void execute() {
        LocalProperties localProperties = LocalProperties.get(getProject());
        localProperties.enterScope();

        List<BuildListener> savedListeners = removeListeners();
        try {

            for (Task nestedTask : nestedTasks) {
                nestedTask.perform();
            }
        } finally {
            restoreListeners(savedListeners);
            localProperties.exitScope();
        }
    }

    private List<BuildListener> removeListeners() {
        Project p = getProject();
        List<BuildListener> listeners = p.getBuildListeners();
        for (BuildListener listener : listeners) {
            p.removeBuildListener(listener);
        }

        return listeners;
    }

    private void restoreListeners(List<BuildListener> savedListeners) {
        Project p = getProject();
        for (BuildListener listener : savedListeners) {
            p.addBuildListener(listener);
        }
    }
}

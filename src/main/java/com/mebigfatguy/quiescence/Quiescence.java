/*
 * quiescence - an ant task to suppress output
 * Copyright 2017-2019 MeBigFatGuy.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
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

    @Override
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

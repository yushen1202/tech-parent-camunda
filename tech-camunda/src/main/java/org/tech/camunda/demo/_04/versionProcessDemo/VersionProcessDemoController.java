package org.tech.camunda.demo._04.versionProcessDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This demo is to demonstrate multiple version support of Camunda workflows
 * Attention:
 * This demo needs to be run by connecting a non-memory rational DB(e.g. Mysql)
 * Can start the application by property file of application_mysql.properties
 */
@RestController
@RequestMapping("/_04")
@Tag(name = "default", description = "default group")
@Slf4j
public class VersionProcessDemoController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @RequestMapping(path = "/versionProcessPoc", method = RequestMethod.GET)
    @Operation
    public void vacationRequest() {

        String processDefinitionKey = "version_process";
        // can start the process of different version by switching vers
        String versionTag = "1.3";

        /**
         * Get process definition ID
         */
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .versionTag(versionTag)
                .list();

        /**
         * Case 0: start process by definition key
         * When there are multiple versions of process definition,
         * by default the system uses the latest version.
         * At this senario, businessKey can be the same.
         */
        ProcessInstance instance0_0 = runtimeService.startProcessInstanceByKey("version_process", "bk_0");
        ProcessInstance instance0_1 = runtimeService.startProcessInstanceByKey("version_process", "bk_0");

        /**
         * Case 1: start process by process definition ID
         * process definition ID is unique across all versions of a process definition
         * By this way, potentially the system can support multiple versions for users
         */
        ProcessDefinition pd1_0 = processDefinitionList.get(0);
        ProcessDefinition pd1_1 = processDefinitionList.get(1);

        // start process
        ProcessInstance instance1_0 = runtimeService.startProcessInstanceById(pd1_0.getId(), "bk_instance1_0");
        ProcessInstance instance1_1 = runtimeService.startProcessInstanceById(pd1_1.getId(), "bk_instance1_1");

        // process query
        ProcessInstance instance1_0_query = runtimeService.createProcessInstanceQuery().processDefinitionKey("version_process").processInstanceBusinessKey("bk_instance1_0").singleResult();
        ProcessInstance instance1_1_query = runtimeService.createProcessInstanceQuery().processDefinitionKey("version_process").processInstanceBusinessKey("bk_instance1_1").singleResult();

        // check all task at phase 1
        List<Task> allTasksOfPhasse1 = taskService.createTaskQuery().list();

        /*
         * task query and finish tasks for the first instance
         */
        Task instance1_1_task_1 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_1.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_1_task_1.getId());

        Task instance1_1_task_2 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_1.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_1_task_2.getId());

        Task instance1_1_task_3 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_1.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_1_task_3.getId());

        // check the existing process instance here

        /*
         * task query and finish tasks for the second instance
         */
        Task instance1_0_task_1 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_0.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_0_task_1.getId());

        Task instance1_0_task_2 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_0.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_0_task_2.getId());

        Task instance1_0_task_3 = taskService.createTaskQuery().processDefinitionKey("version_process").processDefinitionId(instance1_0.getProcessDefinitionId()).singleResult();
        taskService.complete(instance1_0_task_3.getId());

        // check process instance here

    }

}

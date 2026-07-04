package org.tech.camunda.demo._06.camundaListenerDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tech.camunda.demo._05.messageTestDemo.ServiceTask_04_Service;

import java.util.List;
import java.util.Optional;

/**
 * This demo is to demonstrate belows
 * 1) Kinds of TaskListener
 * 2) Kinds of ExecutionListener
 */
@RestController
@Tag(name = "default", description = "default group")
@RequestMapping("/_06")
@Slf4j
public class CamundaListenerDemoController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ServiceTask_04_Service messageIntermediateThrowService;

    @RequestMapping(path = "/startMessageDemo", method = RequestMethod.GET)
    @Operation
    public void messageDemo() {
        /**
         * Case 1: Process Start, Task Create, Task Complete
         */
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("camunda_listener", "BK_001");
        Task instance1Task1 = taskService.createTaskQuery().processDefinitionKey("camunda_listener").processInstanceBusinessKey("BK_001").taskDefinitionKey("User_Task_1").list().get(0);
        taskService.createComment(instance1Task1.getId(), instance1.getProcessInstanceId(), "This is to test update listener");
        taskService.complete(instance1Task1.getId());

        /**
         * Case 2: Task Assignment
         */
        ProcessInstance instance2 = runtimeService.startProcessInstanceByKey("camunda_listener", "BK_002");
        Task instance2Task1 = taskService.createTaskQuery().processDefinitionKey("camunda_listener").processInstanceBusinessKey("BK_002").taskDefinitionKey("User_Task_1").list().get(0);
        taskService.claim(instance2Task1.getId(), "A_User_ID");
        taskService.complete(instance2Task1.getId());

        /**
         * Case 3: Task delete
         */
        ProcessInstance instance3 = runtimeService.startProcessInstanceByKey("camunda_listener", "BK_003");
        Task instance3Task3 = taskService.createTaskQuery().processDefinitionKey("camunda_listener").processInstanceBusinessKey("BK_003").taskDefinitionKey("User_Task_3").list().get(0);
        /*
         * Attention: seems like unfinished task cannot be deleted directly, otherwise exception happens
         * Need to complete the task first and then delete
         */
        // taskService.deleteTask(instance3Task3.getId(), "delete task");
        taskService.complete(instance3Task3.getId());
        // seems that task delete listener isn't triggered
        taskService.deleteTask(instance3Task3.getId(), "delete task");

        /**
         * Case 4: process completed
         */
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey("camunda_listener").list();
        Optional.ofNullable(taskList).get().stream().forEach( t -> taskService.complete(t.getId()));
    }

}

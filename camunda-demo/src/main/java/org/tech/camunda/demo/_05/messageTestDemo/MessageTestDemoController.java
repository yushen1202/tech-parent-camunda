package org.tech.camunda.demo._05.messageTestDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This demo is to demonstrate belows
 * 1) Message intermediate throw event
 * 2) Message intermediate catch event
 * 3) Message start event
 * 4) Participant
 */
@RestController
@Tag(name = "default", description = "default group")
@RequestMapping("/_05")
@Slf4j
public class MessageTestDemoController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ServiceTask_04_Service messageIntermediateThrowService;

    @RequestMapping(path = "/startMessageDemo", method = RequestMethod.GET)
    @Operation
    public void messageDemo() {
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("A_Process","A_001");
    }

    @RequestMapping(path = "/throwIntermediateMessage", method = RequestMethod.GET)
    @Operation
    public void throwIntermediateMessage() {
        Task beforeThrowMessageTask = taskService.createTaskQuery().processDefinitionKey("A_Process").processInstanceBusinessKey("A_001").singleResult();
        taskService.complete(beforeThrowMessageTask.getId());
    }

}

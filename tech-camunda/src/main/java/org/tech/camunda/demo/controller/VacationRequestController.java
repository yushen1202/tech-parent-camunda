package org.tech.camunda.demo.controller;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
@Tag(name = "default", description = "default group")
@Slf4j
public class VacationRequestController {

    /**
     * Services API
     * https://docs.camunda.org/manual/latest/user-guide/process-engine/process-engine-api/
     */
    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @RequestMapping(path = "/vacationRequest", method = RequestMethod.GET)
    @Operation
    public void vacationRequest() {

        /*
         * Start process
         */
        Map<String, Object> map1 = new HashMap<>();
        map1.put("days", 1);
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("VacationRequestProcess", "id_1001", map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("days", 3);
        ProcessInstance instance2 = runtimeService.startProcessInstanceByKey("VacationRequestProcess", "id_1002", map2);

        String instance1Id = instance1.getId();
        String instance2Id = instance2.getId();

        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

        ProcessInstance instance1Queried = runtimeService.createProcessInstanceQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1001").singleResult();
        ProcessInstance instance2Queried = runtimeService.createProcessInstanceQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1002").singleResult();

        List<Task> stage1AllTaskList1 = taskService.createTaskQuery().list();
        List<Task> stage1Instance1Tasks = taskService.createTaskQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1001").list();
        List<Task> stage1Instance2Tasks = taskService.createTaskQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1002").list();

        stage1Instance1Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId(), Map.of("result", true));
        });

        stage1Instance2Tasks.stream().forEach(t -> {
            // Notice: When Department Manager Approve run, a service task is followed
            log.info("pause here");
            taskService.complete(t.getId(), Map.of("result", true));
        });

        ProcessInstance stage1Instance1 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance1Id)
                .singleResult();
        ProcessInstance stage1Instance2 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance2Id)
                .singleResult();

        List<Task> stage2AllTaskList1 = taskService.createTaskQuery().list();
        List<Task> stage2Instance1Tasks = taskService.createTaskQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1001").list();
        List<Task> stage2Instance2Tasks = taskService.createTaskQuery().processDefinitionKey("VacationRequestProcess").processInstanceBusinessKey("id_1002").list();

        stage2Instance1Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId());
        });

        stage2Instance2Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId());
        });

        log.info("success");
    }

}

package org.tech.camunda.demo.CamundaBasicDemo;

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
public class CamundaBasicDemoController {

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

    @RequestMapping(path = "/requestAndApprove", method = RequestMethod.GET)
    @Operation
    public void requestAndApprove() {

        /*
         * process definition
         * see Modeler application and /resources/bpmn folder
         */

        /*
         * Start process
         */
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("request_and_approve", "id_1001");
        ProcessInstance instance2 = runtimeService.startProcessInstanceByKey("request_and_approve", "id_1002");
        Map<String, Object> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        ProcessInstance instance3 = runtimeService.startProcessInstanceByKey("request_and_approve", "id_1003", map);

        String instance1Id = instance1.getId();
        String instance2Id = instance2.getId();
        String instance3Id = instance3.getId();

        /*
         * Find process definition
         */
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();

        /*
         * Find process instance
         */
        ProcessInstance instance1Queried = runtimeService.createProcessInstanceQuery().processDefinitionKey("request_and_approve").processInstanceBusinessKey("id_1001").singleResult();

        /*
         * Find tasks
         */
        List<Task> allTaskList1 = taskService.createTaskQuery().list();
        Task instance1Task1 = taskService.createTaskQuery().processDefinitionKey("request_and_approve").processInstanceBusinessKey("id_1001").list().get(0);

        /**
         * Finish tasks
         */
        // Finish the first task of instance 1
        taskService.complete(instance1Task1.getId());

        List<Task> allTaskList2 = taskService.createTaskQuery().list();
        Task instance1Task2 = taskService.createTaskQuery().processDefinitionKey("request_and_approve").processInstanceBusinessKey("id_1001").list().get(0);

        // Finish the second task of instance 1
        taskService.complete(instance1Task2.getId());

        /**
         * Check the status of process instance
         */
        // Check left tasks of instance 1
        List<Task> allTaskListOfInstance1 = taskService.createTaskQuery().processDefinitionKey("request_and_approve").processInstanceBusinessKey("id_1001").list();

        log.info("The status of process instance {}: isEnded: {}; isSuspended: {}", instance1.getBusinessKey(), instance1.isEnded(), instance1.isSuspended());
        log.info("The status of process instance {}: isEnded: {}; isSuspended: {}", instance2.getBusinessKey(), instance2.isEnded(), instance2.isSuspended());
        log.info("The status of process instance {}: isEnded: {}; isSuspended: {}", instance3.getBusinessKey(), instance3.isEnded(), instance3.isSuspended());

        // Check whether a process instance has been finished or not
        ProcessInstance stage1Instance1 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance1Id)
                .singleResult();
        ProcessInstance stage1Instance2 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance2Id)
                .singleResult();
        ProcessInstance stage1Instance3 = runtimeService.createProcessInstanceQuery()
                .processInstanceId(instance3Id)
                .singleResult();

        /**
         * Terminate a process instance
         */
        // instance1 has been finished, can't be deleted.
        // runtimeService.deleteProcessInstance(instance1.getProcessInstanceId(), "for test");
        runtimeService.deleteProcessInstance(instance2.getProcessInstanceId(), "for test");
        runtimeService.deleteProcessInstance(instance3.getProcessInstanceId(), "for test");
        log.info("The status of process instance {}: isEnabled: {}; isSuspended: {}", instance1.getBusinessKey(), instance1.isEnded(), instance1.isSuspended());
        log.info("The status of process instance {}: isEnabled: {}; isSuspended: {}", instance2.getBusinessKey(), instance2.isEnded(), instance2.isSuspended());
        log.info("The status of process instance {}: isEnabled: {}; isSuspended: {}", instance3.getBusinessKey(), instance3.isEnded(), instance3.isSuspended());

        log.info("success");
    }

}

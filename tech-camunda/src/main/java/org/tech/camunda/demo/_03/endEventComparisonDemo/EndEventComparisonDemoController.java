package org.tech.camunda.demo._03.endEventComparisonDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * this demo is to demonstrate belows
 * 1) Event sub process
 * 2) Conditional boundary event
 * 3) The comparison between end event and terminated end event
 * 4) Message start event(non interrupting)
 */
@RestController
@RequestMapping("/_03")
@Tag(name = "default", description = "default group")
@Slf4j
public class EndEventComparisonDemoController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @RequestMapping(path = "/endEventComparisonDemo", method = RequestMethod.GET)
    @Operation
    public void requestAndApprove() throws InterruptedException {

        /*
         * Clear existing process instances
         */
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey("decision_tree").list();
        Optional.ofNullable(processInstances).get().stream().forEach(pi -> {
            runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "");
        });

        /*
         * Case 1: No sub process, normal end event
         */
        Map<String, Object> map1 = new HashMap<>();
        map1.put("isSubProcessType", false);
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("end_event_comparison", "non_sub_process_normal_end", map1);

        List<Task> allTaskList1 = taskService.createTaskQuery().list();
        Task instance1Task1 = taskService.createTaskQuery().processDefinitionKey("end_event_comparison").processInstanceBusinessKey("non_sub_process_normal_end").list().get(0);
        VariableMap variableMap1 = Variables.createVariables()
                .putValue("triggerNonSubProcessTerminateEndEvent", false);
        taskService.complete(instance1Task1.getId(), variableMap1);

        ProcessInstance processInstance1 = runtimeService.createProcessInstanceQuery().processInstanceId(instance1.getProcessInstanceId()).singleResult();

        // 检查流程实例是否存在
        checkProcessInstance(processInstance1);

        /*
         * Case 2: No sub process, terminated end event
         */
        Map<String, Object> map2 = new HashMap<>();
        map2.put("isSubProcessType", false);
        ProcessInstance instance2 = runtimeService.startProcessInstanceByKey("end_event_comparison", "non_sub_process_terminated_end", map2);

        List<Task> allTaskList2 = taskService.createTaskQuery().list();
        Task instance2Task1 = taskService.createTaskQuery().processDefinitionKey("end_event_comparison").processInstanceBusinessKey("non_sub_process_terminated_end").list().get(0);
        VariableMap variableMap2 = Variables.createVariables()
                .putValue("triggerNonSubProcessTerminateEndEvent", true);
        taskService.complete(instance2Task1.getId(), variableMap2);

        ProcessInstance processInstance2 = runtimeService.createProcessInstanceQuery().processInstanceId(instance2.getProcessInstanceId()).singleResult();

        // 检查流程实例是否存在
        checkProcessInstance(processInstance2);

        /*
         * Case 3: sub process, normal end event
         */
        Map<String, Object> map3 = new HashMap<>();
        map3.put("isSubProcessType", true);
        map3.put("uuid", UUID.randomUUID());
        ProcessInstance instance3 = runtimeService.startProcessInstanceByKey("end_event_comparison", "sub_process_normal_end", map3);

        List<ProcessInstance> processInstanceList3 = runtimeService.createProcessInstanceQuery().list();

        List<Task> allTaskList3 = taskService.createTaskQuery().list();
        Task instance3Task1 = taskService.createTaskQuery().processDefinitionKey("end_event_comparison").processInstanceBusinessKey("sub_process_normal_end").list().get(0);
        VariableMap variableMap3 = Variables.createVariables()
                .putValue("triggerSubProcessTerminateEndEvent", false);
        taskService.complete(instance3Task1.getId(), variableMap3);

        List<Task> allTaskList3_1 = taskService.createTaskQuery().list();
        Optional.ofNullable(allTaskList3_1).get().stream().forEach(t -> {
           if(t.getTaskDefinitionKey().equals("sub_process_usertask_B")) {
               taskService.complete(t.getId());
           }
        });

        List<ProcessInstance> processInstanceList3_1 = runtimeService.createProcessInstanceQuery().list();
        ProcessInstance processInstance3 = runtimeService.createProcessInstanceQuery().processInstanceId(instance3.getProcessInstanceId()).singleResult();

        // 检查流程实例是否存在
        checkProcessInstance(processInstance3);

        /*
         * Case 4: sub process, terminated end event
         */
        Map<String, Object> map4 = new HashMap<>();
        map4.put("isSubProcessType", true);
        ProcessInstance instance4 = runtimeService.startProcessInstanceByKey("end_event_comparison", "sub_process_terminated_end", map4);

        List<ProcessInstance> processInstanceList4 = runtimeService.createProcessInstanceQuery().list();

        List<Task> allTaskList4 = taskService.createTaskQuery().list();
        Task instance4Task1 = taskService.createTaskQuery().processDefinitionKey("end_event_comparison").processInstanceBusinessKey("sub_process_terminated_end").list().get(0);
        VariableMap variableMap4 = Variables.createVariables()
                .putValue("triggerSubProcessTerminateEndEvent", true);
        taskService.complete(instance4Task1.getId(), variableMap4);

        // 注意！这里我们会发现，随着主流程terminated end event, 子流程的sub_task也被动结束了。
        List<Task> allTaskList4_1 = taskService.createTaskQuery().list();
        Optional.ofNullable(allTaskList4_1).get().stream().forEach(t -> {
            if(t.getTaskDefinitionKey().equals("sub_process_usertask_B")) {
                taskService.complete(t.getId());
            }
        });

        List<ProcessInstance> processInstanceList4_1 = runtimeService.createProcessInstanceQuery().list();

        ProcessInstance processInstance4 = runtimeService.createProcessInstanceQuery().processInstanceId(instance4.getProcessInstanceId()).singleResult();
        // 检查流程实例是否存在
        checkProcessInstance(processInstance4);
    }

    private void checkProcessInstance(ProcessInstance processInstance) {
        if (processInstance != null) {
            // 流程实例存在，获取并打印状态
            System.out.println("流程实例ID: " + processInstance.getId());
            System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
            System.out.println("isSuspended: " + processInstance.isSuspended());
        } else {
            System.out.println("流程实例不存在或已结束。");
        }
    }

}

package org.tech.camunda.demo._02.decisionTreeDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This demo is to demonstrate the usage of decision tree in a workflow
 */
@RestController
@RequestMapping("/_02")
@Tag(name = "default", description = "default group")
@Slf4j
public class DecisionTreeDemoController {

    @Autowired
    TaskService taskService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    DecisionService decisionService;

    @RequestMapping(path = "/approveDecision", method = RequestMethod.GET)
    @Operation
    public void approveDecision() {

        /*
         * Clear existing process instances
         */
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionKey("decision_tree").list();
        Optional.ofNullable(processInstances).get().stream().forEach(pi -> {
            runtimeService.deleteProcessInstance(pi.getProcessInstanceId(), "");
        });

        Map<String, Object> map1 = new HashMap<>();
        map1.put("amount", 1000);
        ProcessInstance instance1 = runtimeService.startProcessInstanceByKey("decision_tree", "id_1001", map1);

        Map<String, Object> map2 = new HashMap<>();
        map1.put("amount", 1000);
        ProcessInstance instance2 = runtimeService.startProcessInstanceByKey("decision_tree", "id_1002", map1);

        Map<String, Object> map3 = new HashMap<>();
        map1.put("amount", 10000);
        map1.put("role", "HR");
        map1.put("useFor", "computer");
        ProcessInstance instance3 = runtimeService.startProcessInstanceByKey("decision_tree", "id_1003", map1);

        Map<String, Object> map4 = new HashMap<>();
        map1.put("amount", 10000);
        map1.put("role", "Tech");
        map1.put("useFor", "computer");
        ProcessInstance instance4 = runtimeService.startProcessInstanceByKey("decision_tree", "id_1004", map1);

        List<Task> stage1Instance1Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1001").list();
        List<Task> stage1Instance2Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1002").list();
        List<Task> stage1Instance3Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1003").list();
        List<Task> stage1Instance4Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1004").list();

        stage1Instance1Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId(), Map.of("financialResult", true));
        });

        stage1Instance2Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId(), Map.of("financialResult", false));
        });

        stage1Instance3Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId(), Map.of("financialResult", false));
        });

        stage1Instance4Tasks.stream().forEach(t -> {
            log.info("pause here");
            taskService.complete(t.getId());
        });

        List<Task> stage2Instance1Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1001").list();
        List<Task> stage2Instance2Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1002").list();
        List<Task> stage2Instance3Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1003").list();
        List<Task> stage2Instance4Tasks = taskService.createTaskQuery().processDefinitionKey("decision_tree").processInstanceBusinessKey("id_1004").list();

        log.info("pause here");
    }

    @RequestMapping(path = "/decisionTree", method = RequestMethod.GET)
    @Operation
    public void decisionTree() {

        List<DecisionDefinition> decisionDefinitions = repositoryService.createDecisionDefinitionQuery().list();

        DecisionDefinition dd = repositoryService.createDecisionDefinitionQuery().decisionDefinitionName("Approve Decision").singleResult();
        VariableMap variables1 = Variables.createVariables()
                .putValue("role", "HR")
                .putValue("useFor", "stationery");

        String ddKey = dd.getKey();
        DmnDecisionTableResult decisionResult1 = decisionService.evaluateDecisionTableByKey(ddKey, variables1);
        Boolean result1 = decisionResult1.getSingleEntry();

        VariableMap variables2 = Variables.createVariables()
                .putValue("role", "HR")
                .putValue("useFor", "food");
        String ddId = dd.getId();
        DmnDecisionTableResult decisionResult2 = decisionService.evaluateDecisionTableById(ddId, variables2);
        Boolean result2 = decisionResult2.getSingleEntry();

        VariableMap variables3 = Variables.createVariables()
                .putValue("role", "Tech")
                .putValue("useFor", "computer");

        DmnDecisionTableResult decisionResult3 = decisionService.evaluateDecisionTableById(ddId, variables3);
        Boolean result3 = decisionResult3.getSingleEntry();

        VariableMap variables4 = Variables.createVariables()
                .putValue("role", "Tech");
        DmnDecisionTableResult decisionResult4 = decisionService.evaluateDecisionTableById(ddId, variables4);
        Boolean result4 = decisionResult4.getSingleEntry();

        log.info("pause here");
    }


}

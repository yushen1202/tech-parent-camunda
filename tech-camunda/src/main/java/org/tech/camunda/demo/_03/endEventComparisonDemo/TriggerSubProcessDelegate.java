package org.tech.camunda.demo._03.endEventComparisonDemo;


import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TriggerSubProcessDelegate implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("====>TriggerSubProcessDelegate");
        log.info("ProcessInstanceId: " + execution.getProcessInstanceId() + " ;ProcessDefinitionId: " + execution.getProcessDefinitionId() + " ;ProcessBusinessKey: " + execution.getProcessBusinessKey());
        runtimeService.createMessageCorrelation("start_sub_process_message")
                .processInstanceBusinessKey(execution.getProcessBusinessKey())
                .correlate();

        log.info("====>TriggerSubProcessDelegate executed");
    }
}
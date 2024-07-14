package org.tech.camunda.demo.approveDecisionDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RejectDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("ApproveProcess [Amount:" + execution.getVariables().get("amount")
                + ", role：" + execution.getVariables().get("role")
                + ", useFor：" + execution.getVariables().get("useFor") + "] has been rejected");
    }
}
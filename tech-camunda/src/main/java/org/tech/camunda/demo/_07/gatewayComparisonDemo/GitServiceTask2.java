package org.tech.camunda.demo._07.gatewayComparisonDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GitServiceTask2 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("GitServiceTask2 is run");
    }
}

package org.tech.camunda.demo._05.messageTestDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageThrow_03_Service implements JavaDelegate {

    @Autowired
    RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("MessageThrow_03_Service is running");
        runtimeService.createMessageCorrelation("message_reference_back")
                .processInstanceBusinessKey("A_001")
                .correlate();
        log.info("MessageThrow_03_Service run finished");
    }
}

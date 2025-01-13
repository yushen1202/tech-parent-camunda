package org.tech.camunda.demo._05.messageTestDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceTask_01_Service implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("ListenerServiceTask_01_Service is run");
    }
}

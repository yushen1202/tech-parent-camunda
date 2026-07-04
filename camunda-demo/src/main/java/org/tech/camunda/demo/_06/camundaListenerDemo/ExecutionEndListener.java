package org.tech.camunda.demo._06.camundaListenerDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecutionEndListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        log.info("ExecutionEndListener is run");
    }
}

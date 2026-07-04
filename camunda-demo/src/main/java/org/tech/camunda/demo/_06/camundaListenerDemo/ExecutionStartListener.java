package org.tech.camunda.demo._06.camundaListenerDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExecutionStartListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        log.info("ExecutionStartListener is run");
    }
}

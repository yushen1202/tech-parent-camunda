package org.tech.camunda.demo._06.camundaListenerDemo;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        log.info("TaskCompleteListener is run");
    }
}

package org.tech.camunda.demo._08.remainingTaskDemo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tech.camunda.demo._05.messageTestDemo.ServiceTask_04_Service;

import java.util.List;
import java.util.Optional;

/**
 * This demo continues to demonstrate the differences between normal end event and terminated end event.
 * This demo can be directly done through Camunda Cockpit.
 * Conclusion:
 * Terminated end event closes all pending user tasks and the process completely finish.
 * In contrast, normal end event just means the completion of the path where it belongs to.
 */
@RestController
@Tag(name = "default", description = "default group")
@RequestMapping("/_08")
@Slf4j
public class RemainingTaskDemoController {


}

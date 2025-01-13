package org.tech.camunda.utils;

import org.camunda.bpm.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamundaUtilServiceImpl implements CamundaUtilService {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    IdentityService identityService;

    @Autowired
    FormService formService;

    @Autowired
    HistoryService historyService;

    @Autowired
    ManagementService managementService;

    @Autowired
    FilterService filterService;

    @Autowired
    ExternalTaskService externalTaskService;

    @Autowired
    CaseService caseService;

    @Autowired
    DecisionService decisionService;

    /**
     * TODO: ADD COMMON UTILITY METHOD HERE
     */

}

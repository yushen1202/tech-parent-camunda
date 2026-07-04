package org.tech.camunda.demo._07.gatewayComparisonDemo;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This demo demonstrate the differences of 3 often used Camunda gateways.
 * This demo can be directly done through Camunda Cockpit.
 * 1) Parallel Gateway: all output branches must be completed and then the process move forwards
 * 2) Inclusive Gateway: All output branches that satisfy the conditions will be executed.
 *    Multiple outputs allowed.
 * 3) Exclusive Gateway: Only one output branch that meets the criteria will be executed.
 *    If multiple outputs meet, only pick one to execute.
 */
@RestController
@Tag(name = "default", description = "default group")
@RequestMapping("/_07")
@Slf4j
public class GatewayComparisonDemoController {


}

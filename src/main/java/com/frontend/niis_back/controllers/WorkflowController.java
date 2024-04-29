package com.frontend.niis_back.controllers;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.client.api.response.ProcessInstanceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/workflow")
public class WorkflowController {

    private final ZeebeClient zeebeClient;

    @Autowired
    public WorkflowController(ZeebeClient zeebeClient) {
        this.zeebeClient = zeebeClient;
    }

    @PostMapping("/get-restaurants")
    public ResponseEntity<?> startWorkflowInstance() {
        try {
            ProcessInstanceResult result = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("getAllRestaurantsProcess")
                    .latestVersion()
                    .withResult()
                    .requestTimeout(Duration.ofSeconds(30))
                    .send()
                    .join();

            return ResponseEntity.ok("Workflow instance started with ID: " + result.getProcessInstanceKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to start workflow instance: " + e.getMessage());
        }
    }

    @PostMapping("/create-restaurant")
    public ResponseEntity<?> startCreateRestaurantProcess(@RequestBody Map<String, Object> restaurantData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_0ct8wfn")
                    .latestVersion()
                    .variables(restaurantData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Create restaurant process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start create restaurant process: " + e.getMessage());
        }
    }

    @PostMapping("/update-restaurant")
    public ResponseEntity<?> startUpdateRestaurantProcess(@RequestBody Map<String, Object> updateData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_01n56je")
                    .latestVersion()
                    .variables(updateData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Update restaurant process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start update restaurant process: " + e.getMessage());
        }
    }

    @PostMapping("/delete-restaurant")
    public ResponseEntity<?> startDeleteRestaurantProcess(@RequestBody Map<String, Object> deleteData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_0yfmb3p")
                    .latestVersion()
                    .variables(deleteData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Delete restaurant process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start delete restaurant process: " + e.getMessage());
        }
    }

    @PostMapping("/get-reviews")
    public ResponseEntity<?> startGetReviewsProcess(@RequestBody Map<String, Object> requestData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_1s5o5wu")
                    .latestVersion()
                    .variables(requestData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Get reviews process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start get reviews process: " + e.getMessage());
        }
    }

    @PostMapping("/create-review")
    public ResponseEntity<?> startCreateReviewProcess(@RequestBody Map<String, Object> reviewData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_1ujbj8j")
                    .latestVersion()
                    .variables(reviewData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Create review process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start create review process: " + e.getMessage());
        }
    }

    @PostMapping("/update-review")
    public ResponseEntity<?> startUpdateReviewProcess(@RequestBody Map<String, Object> reviewData) {
        System.out.println("Received review update data: " + reviewData);
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_0udvjr2")
                    .latestVersion()
                    .variables(reviewData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Update review process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            System.err.println("Error starting update review process: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start update review process: " + e.getMessage());
        }
    }

    @PostMapping("/delete-review")
    public ResponseEntity<?> startDeleteReviewProcess(@RequestBody Map<String, Object> deleteData) {
        try {
            ProcessInstanceEvent instance = zeebeClient.newCreateInstanceCommand()
                    .bpmnProcessId("Process_10ui8a0")
                    .latestVersion()
                    .variables(deleteData)
                    .send()
                    .join();
            return ResponseEntity.ok().body("Delete review process started successfully with instance ID: " + instance.getProcessInstanceKey());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start delete review process: " + e.getMessage());
        }
    }


}

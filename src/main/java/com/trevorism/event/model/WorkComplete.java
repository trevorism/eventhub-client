package com.trevorism.event.model;

import com.trevorism.event.PingingEventProducer;
import com.trevorism.event.EventProducer;

/**
 * @author tbrooks
 */
public class WorkComplete {

    public static final String TOPIC = "workcomplete";

    private String projectName;
    private String serviceName;
    private String correlationId;

    public WorkComplete() {
    }

    public WorkComplete(String projectName, String serviceName, String correlationId) {
        this.projectName = projectName;
        this.serviceName = serviceName;
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}

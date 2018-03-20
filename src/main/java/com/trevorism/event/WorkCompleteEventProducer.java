package com.trevorism.event;

import com.trevorism.event.model.WorkComplete;

/**
 * @author tbrooks
 */
public class WorkCompleteEventProducer extends PingingEventProducer<WorkComplete> {

    void sendEvent(WorkComplete workComplete){
        super.sendEvent(WorkComplete.TOPIC, workComplete, workComplete.getCorrelationId());
    }

}

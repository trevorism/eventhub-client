package com.trevorism.event;

import com.trevorism.event.model.WorkComplete;
import com.trevorism.https.SecureHttpClient;

/**
 * @author tbrooks
 */
public class WorkCompleteEventProducer extends PingingEventProducer<WorkComplete> {

    public WorkCompleteEventProducer() {
        super();
    }

    public WorkCompleteEventProducer(SecureHttpClient httpClient) {
        super(httpClient);
    }

    public void sendEvent(WorkComplete workComplete){
        super.sendEvent(WorkComplete.TOPIC, workComplete, workComplete.getCorrelationId());
    }

}

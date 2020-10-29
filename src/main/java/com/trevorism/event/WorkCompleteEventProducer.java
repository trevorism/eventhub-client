package com.trevorism.event;

import com.trevorism.event.model.WorkComplete;
import com.trevorism.https.token.ObtainTokenStrategy;

/**
 * @author tbrooks
 */
public class WorkCompleteEventProducer extends PingingEventProducer<WorkComplete> {

    public WorkCompleteEventProducer() {
        super();
    }

    public WorkCompleteEventProducer(ObtainTokenStrategy strategy) {
        super(strategy);
    }

    public void sendEvent(WorkComplete workComplete){
        super.sendEvent(WorkComplete.TOPIC, workComplete, workComplete.getCorrelationId());
    }

}

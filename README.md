# eventhub-client
Java client for sending events to [trevorism eventhub](https://github.com/trevorism/eventhub)

Current version: 1.4.0

Use it like this:

```
EventProducer<MyObject> eventhubProducer = new PingingEventProducer<>();
eventProducer.sendEvent("myTopic", "myObjectInstance", "correlationId");
```

There is a built-in WorkComplete implementation for notifying the system of a job well done.
```
WorkCompleteEventProducer workCompleteProducer = new WorkCompleteEventProducer();
workCompleteProducer.sendEvent(new WorkComplete("trevorism-project", "service", "correlationId"));
```

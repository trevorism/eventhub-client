# eventhub-client
![Build](https://github.com/trevorism/eventhub-client/actions/workflows/build.yml/badge.svg)
![GitHub last commit](https://img.shields.io/github/last-commit/trevorism/eventhub-client)
![GitHub language count](https://img.shields.io/github/languages/count/trevorism/eventhub-client)
![GitHub top language](https://img.shields.io/github/languages/top/trevorism/eventhub-client)


Java client for sending events to [trevorism eventhub](https://github.com/trevorism/eventhub)

Current [Version](https://github.com/trevorism/eventhub-client/releases/latest)

## How to Use 
```
EventProducer<MyObject> eventhubProducer = new PingingEventProducer<>();
eventProducer.sendEvent("myTopic", "myObjectInstance", "correlationId");
```

There is a built-in WorkComplete implementation for notifying the system of a job well done.
```
WorkCompleteEventProducer workCompleteProducer = new WorkCompleteEventProducer();
workCompleteProducer.sendEvent(new WorkComplete("trevorism-project", "service", "correlationId"));
```

## How to Build
`gradle clean build`
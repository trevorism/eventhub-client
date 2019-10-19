# eventhub-client
![Jenkins](https://img.shields.io/jenkins/build/http/trevorism-build.eastus.cloudapp.azure.com/eventhub-client)
![Jenkins Coverage](https://img.shields.io/jenkins/coverage/jacoco/http/trevorism-build.eastus.cloudapp.azure.com/eventhub-client)
![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/trevorism/eventhub-client)
![GitHub last commit](https://img.shields.io/github/last-commit/trevorism/eventhub-client)
![GitHub language count](https://img.shields.io/github/languages/count/trevorism/eventhub-client)
![GitHub top language](https://img.shields.io/github/languages/top/trevorism/eventhub-client)


Java client for sending events to [trevorism eventhub](https://github.com/trevorism/eventhub)

Current version: 1.4.1

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

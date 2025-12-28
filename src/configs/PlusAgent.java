package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton.TopicManager;
import graph.TopicManagerSingleton;

public class PlusAgent implements Agent {
    private final String name;
    private final String[] subsNames;
    private final String[] pubsNames;

    private double x = 0;
    private double y = 0;

    public PlusAgent(String name, String[] subs, String[] pubs) {
        this.name = name;
        this.subsNames = subs;
        this.pubsNames = pubs;

        TopicManager tm = TopicManagerSingleton.get();

        if (subs.length >= 2) {
            tm.getTopic(subs[0]).subscribe(this);
            tm.getTopic(subs[1]).subscribe(this);
        }

        if (pubs.length >= 1) {
            tm.getTopic(pubs[0]).addPublisher(this);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(subsNames[0])) {
            x = msg.asDouble;
        } else if (topic.equals(subsNames[1])) {
            y = msg.asDouble;
        }
        if (!Double.isNaN(x) && !Double.isNaN(y)) {
            double result = x + y;
            if (pubsNames.length >= 1) {
                TopicManagerSingleton.get().getTopic(pubsNames[0]).publish(new Message(result));
            }
        }
    }

    @Override
    public void close() {
        TopicManager tm = TopicManagerSingleton.get();
        if (subsNames.length >= 2) {
            tm.getTopic(subsNames[0]).unsubscribe(this);
            tm.getTopic(subsNames[1]).unsubscribe(this);
        }
        if (pubsNames.length >= 1) {
            tm.getTopic(pubsNames[0]).removePublisher(this);
        }
    }
}
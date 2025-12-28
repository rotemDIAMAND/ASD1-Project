package configs;

import graph.Agent;
import graph.Message;
import graph.TopicManagerSingleton;
import graph.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    private final String name;
    private final String inputTopic;
    private final String outputTopic;

    public IncAgent(String name, String[] subs, String[] pubs) {
        this.name = name;
        this.inputTopic = subs[0];
        this.outputTopic = pubs[0];

        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(inputTopic).subscribe(this);
        tm.getTopic(outputTopic).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
    }

    @Override
    public void callback(String topic, Message msg) {
        if (!Double.isNaN(msg.asDouble)) {
            double result = msg.asDouble + 1;
            TopicManagerSingleton.get().getTopic(outputTopic).publish(new Message(result));
        }
    }

    @Override
    public void close() {
        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(inputTopic).unsubscribe(this);
        tm.getTopic(outputTopic).removePublisher(this);
    }
}
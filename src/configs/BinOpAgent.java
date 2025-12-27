package configs;

import java.util.function.BinaryOperator;
import graph.Agent;
import graph.Message;
import graph.Topic;
import graph.TopicManagerSingleton;

public class BinOpAgent implements Agent {
    private final String name;
    private final String input1;
    private final String input2;
    private final String output;
    private final BinaryOperator<Double> operator;

    private Double val1 = 0.0;
    private Double val2 = 0.0;

    public BinOpAgent(String name, String input1, String input2, String output, BinaryOperator<Double> operator) {
        this.name = name;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.operator = operator;

        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(input1).subscribe(this);
        tm.getTopic(input2).subscribe(this);
        tm.getTopic(output).addPublisher(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void reset() {
        val1 = 0.0;
        val2 = 0.0;
    }

    @Override
    public void callback(String topic, Message msg) {
        if (topic.equals(input1)) {
            val1 = msg.asDouble;
        } else if (topic.equals(input2)) {
            val2 = msg.asDouble;
        }

        if (!Double.isNaN(val1) && !Double.isNaN(val2)) {
            Double result = operator.apply(val1, val2);
            TopicManagerSingleton.get().getTopic(output).publish(new Message(result));
        }
    }

    @Override
    public void close() {
        TopicManagerSingleton.TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(input1).unsubscribe(this);
        tm.getTopic(input2).unsubscribe(this);
        tm.getTopic(output).removePublisher(this);
    }
}
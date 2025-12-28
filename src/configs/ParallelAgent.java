package configs;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import graph.Agent;
import graph.Message;

public class ParallelAgent implements Agent {
    public ParallelAgent(Agent realAgent) {
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void reset() {}

    @Override
    public void callback(String topic, Message msg) {}

    @Override
    public void close() {}
}

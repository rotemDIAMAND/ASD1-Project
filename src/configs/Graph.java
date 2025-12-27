package configs;

import java.util.ArrayList;
import graph.Agent;
import graph.Topic;
import java.util.Map;
import java.util.HashMap;
import graph.TopicManagerSingleton;

public class Graph extends ArrayList<Node> {

    public Graph() {
        super();
    }

    public void createFromTopics() {
        this.clear();
        Map<String, Node> nodeMap = new HashMap<>();

        for (Topic t : TopicManagerSingleton.get().getTopics()) {
            String tName = "T" + t.name;
            Node tNode = nodeMap.computeIfAbsent(tName, Node::new);

            for (Agent sub : t.subs) {
                String aName = "A" + sub.getName();
                Node aNode = nodeMap.computeIfAbsent(aName, Node::new);
                tNode.addEdge(aNode);
            }

            for (Agent pub : t.pubs) {
                String aName = "A" + pub.getName();
                Node aNode = nodeMap.computeIfAbsent(aName, Node::new);
                aNode.addEdge(tNode);
            }
        }
        this.addAll(nodeMap.values());
    }

    public boolean hasCycles() {
        for (Node node : this) {
            if (node.hasCycles()) {
                return true;
            }
        }
        return false;
    }
}

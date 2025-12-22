package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TopicManagerSingleton {

    public static TopicManager get() {
        return TopicManager.instance;
    }

    static class TopicManager {

        private static final TopicManager instance = new TopicManager();
        private final Map<String, Topic> topics;
        private TopicManager() {
            topics = new HashMap<>();
        }

        public Topic getTopic(String name) {
            Topic topic = topics.get(name);
            if (topic == null) {
                topic = new Topic(name);
                topics.put(name, topic);
            }
            return topic;
        }

        public Collection<Topic> getTopics() {
            return topics.values();
        }

        public void clear() {
            topics.clear();
        }
    }
}


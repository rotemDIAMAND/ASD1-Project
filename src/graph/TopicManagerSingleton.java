package graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TopicManagerSingleton {

    // המתודה שנדרשה במפורש
    public static TopicManager get() {
        return TopicManager.instance;
    }

    // ===== מחלקה פנימית =====
    static class TopicManager {

        // Singleton instance
        private static final TopicManager instance = new TopicManager();

        // מפת נושאים
        private final Map<String, Topic> topics;

        // בנאי פרטי
        private TopicManager() {
            topics = new HashMap<>();
        }

        // מחזיר Topic קיים או יוצר חדש
        public Topic getTopic(String name) {
            Topic topic = topics.get(name);
            if (topic == null) {
                topic = new Topic(name);
                topics.put(name, topic);
            }
            return topic;
        }

        // מחזיר את כל הנושאים
        public Collection<Topic> getTopics() {
            return topics.values();
        }

        // מנקה את המפה
        public void clear() {
            topics.clear();
        }
    }
}


package test;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TopicManager {

    // Singleton instance
    static final TopicManager instance = new TopicManager();

    // מפת נושאים: שם נושא → Topic
    private final Map<String, Topic> topics;

    // בנאי פרטי
    private TopicManager() {
        this.topics = new HashMap<>();
    }

    // החזרת Topic קיים או יצירת חדש
    public Topic getTopic(String name) {
        Topic topic = topics.get(name);
        if (topic == null) {
            topic = new Topic(name);
            topics.put(name, topic);
        }
        return topic;
    }

    // החזרת כל הנושאים
    public Collection<Topic> getTopics() {
        return topics.values();
    }

    // ניקוי כל המפה
    public void clear() {
        topics.clear();
    }
}


package test;

public class TopicManagerSingleton {
    public static TopicManager get() {
        return TopicManager.instance;
    }
}

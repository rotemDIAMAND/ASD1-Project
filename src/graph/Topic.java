package graph;
import java.util.ArrayList;
import java.util.List;

public class Topic {

    public final String name;
    public final List<Agent> subs;
    public final List<Agent> pubs;

    // בנאי בהרשאת package
    Topic(String name) {
        this.name = name;
        this.subs = new ArrayList<>();
        this.pubs = new ArrayList<>();
    }

    // רישום מאזין
    public void subscribe(Agent agent) {
        if (!subs.contains(agent)) {
            subs.add(agent);
        }
    }

    // הסרת מאזין
    public void unsubscribe(Agent agent) {
        subs.remove(agent);
    }

    // פרסום הודעה לכל המאזינים
    public void publish(Message message) {
        for (Agent agent : subs) {
            agent.callback(name, message);
        }
    }

    // הוספת מפרסם
    public void addPublisher(Agent agent) {
        if (!pubs.contains(agent)) {
            pubs.add(agent);
        }
    }

    // הסרת מפרסם
    public void removePublisher(Agent agent) {
        pubs.remove(agent);
    }
}



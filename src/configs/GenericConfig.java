package configs;

import graph.Agent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GenericConfig implements Config {
    private String fileName;
    private final List<Agent> agents = new ArrayList<>();

    public void setConfFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void create() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            if (lines.size() % 3 != 0) {
                return;
            }
            for (int i = 0; i < lines.size(); i += 3) {
                String className = lines.get(i);        // שורה 1: שם המחלקה
                String[] subs = lines.get(i + 1).split(","); // שורה 2: רשימת Subs
                String[] pubs = lines.get(i + 2).split(","); // שורה 3: רשימת Pubs
                String agentName = "Agent_" + (i / 3);

                try {
                    Class<?> clazz = Class.forName(className);
                    Agent realAgent = (Agent) clazz.getConstructor(String.class, String[].class, String[].class)
                            .newInstance(agentName, subs, pubs);
                    ParallelAgent pAgent = new ParallelAgent(realAgent);
                    agents.add(pAgent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void close() {
        for (Agent a : agents) {
            a.close();
        }
        agents.clear();
    }
}
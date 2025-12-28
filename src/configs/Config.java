package configs;

public interface Config {
    void setConfFile(String fileName);

    void create();
    String getName();
    int getVersion();
    void close();
}

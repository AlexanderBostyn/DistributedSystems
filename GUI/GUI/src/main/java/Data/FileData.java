package Data;

public class FileData {
    public String hash;
    public String name;
    public String origin;
    public String owner;
    public String status;

    public FileData(String hash, String name, String origin, String owner, String status) {
        this.hash = hash;
        this.name = name;
        this.origin = origin;
        this.owner = owner;
        this.status = status;
    }
}

package processing_stats.entities;

/**
 * Created by jan.murin on 12-Aug-16.
 */
public class Country implements Comparable<Country>{

    public int id;
    public String name;
    public String name_SK;

    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id +
                "\", \"name\":\"" + name + '\"' +
                ", \"name_SK\":\"" + name_SK + '\"' +
                "}";
    }

    @Override
    public int compareTo(Country o) {
        return name_SK.compareTo(o.name_SK);
    }
}

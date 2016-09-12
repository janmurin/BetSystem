package processing_stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan.murin on 12-Sep-16.
 */
public class StatistikaCompetitora implements Comparable<StatistikaCompetitora> {

    private int competitorID;
    private List<Integer> postupnost = new ArrayList<>();
    private int vyhier;
    private int remiz;
    private int prehier;

    public StatistikaCompetitora(int id) {
        competitorID = id;
    }

    public void pridajVitazstvo() {
        postupnost.add(1);
        vyhier++;
    }

    public void pridajRemizu() {
        postupnost.add(0);
        remiz++;
    }

    public void pridajPrehru() {
        postupnost.add(-1);
        prehier++;
    }

    public int getCompetitorID() {
        return competitorID;
    }

    @Override
    public int compareTo(StatistikaCompetitora o) {
        return Integer.compare(vyhier, o.vyhier);
    }

    @Override
    public String toString() {
        return "StatistikaCompetitora{" +
                "competitorID=" + competitorID +
                ", vyhier=" + vyhier +
                ", remiz=" + remiz +
                ", prehier=" + prehier +
                '}';
    }
}

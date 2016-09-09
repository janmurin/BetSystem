package data;

import data.*;
import data.CleanBetEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan.murin on 08-Sep-16.
 */
public class ComplexBetEvent {
    public List<CleanBetEvent> events=new ArrayList<>();
    public int id;

    @Override
    public String toString() {
        return "ComplexBetEvent{" +
                "events=" + events +
                ", id=" + id +
                '}';
    }
}

package processing_stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.CleanBetEvent;
import data.ComplexBetEvent;
import data.Databaza;
import data.TipovaciDen;
import make_cleanDB.Text2jsonParser;
import process_cleanDB_2.Simulacia_81_Kombinacii;
import processing_stats.entities.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jan.murin on 08-Sep-16.
 */
public class StatsGenerator {
    private List<Country> countries;
    private List<Sport> sports;
    private List<League> leagues;
    private List<EventType> eventTypes;
    private List<Competitor> competitors;

    List<UnsupportedBetEvent> unsupportedEvents = new ArrayList<>();
    Set<String> unsupportedSports = new HashSet<>();
    Set<String> unsupportedLeagues = new HashSet<>();
    Set<String> unsupportedEventTypes = new HashSet<>();
    Set<String> unsupportedCompetitors = new HashSet<>();
    List<ComplexBetEvent> unsupportedCompetitorsEvents=new ArrayList<>();

    /*
- prechadza celu databazu a z celej ponuky vytiahne podporovane udalosti aby na ne mohol program stavit podla internej alebo externej statistiky
- futbal, liga, zapas a okrem toho musi rozpoznat aj competitora


    */
    public static void main(String[] args) {
        new StatsGenerator().execute();
    }

    public void execute() {
        loadEntities();
        Databaza db = new Databaza();

        Iterator<TipovaciDen> iterator = db.iterator();
        TipovaciDen den;
        int vsetkychComplexEventov = 0;
        List<SupportedBetEvent> supportedEvents=new ArrayList<>();
        int counter = 0;
        while (iterator.hasNext()) {
            den = iterator.next();
            vsetkychComplexEventov += den.complexEventy.size();
            List<SupportedBetEvent> filteredEvents = filterEvents(den.complexEventy);
            System.out.println(den.den + ": pocet eventov: " + filteredEvents.size());
            supportedEvents.addAll(filteredEvents);

            System.out.println("");
            counter++;
//            if (counter == 10) {
//                break;
//            }
        }

        System.out.println("UNSUPPORTED");
        System.out.println("SPORTS " + unsupportedSports.size());
        for (String s : unsupportedSports) {
            System.out.println(s);
        }
        System.out.println();
//        System.out.println("LEAGUES " + unsupportedLeagues.size());
//        for (String s : unsupportedLeagues) {
//            System.out.println(s);
//        }
//        System.out.println();
//        System.out.println("EVENT TYPES " + unsupportedEventTypes.size());
//        for (String s : unsupportedEventTypes) {
//            System.out.println(s);
//        }
//        System.out.println();
        System.out.println("COMPETITORS " + unsupportedCompetitors.size());
        for (String c : unsupportedCompetitors) {
            System.out.println(c);
        }

        System.out.println("Vsetkych supported complex eventov: " + supportedEvents.size());
        System.out.println("Vsetkych complex eventov: " + vsetkychComplexEventov);
        System.out.println("unsupportedCompetitorsEvents: "+unsupportedCompetitorsEvents.size());
       // System.out.println(unsupportedCompetitorsEvents);

       //System.out.println(supportedEvents);
        Writer out= null;
        try{
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src\\main\\resources\\output.txt"), "UTF-8"));
            for(SupportedBetEvent sbe:supportedEvents){
                out.write(sbe.toString());
            }
            out.flush();
        } catch(UnsupportedEncodingException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch(FileNotFoundException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IOException ex) {
            Logger.getLogger(Text2jsonParser.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (out!= null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * vrati zoznam tych udalosti, ktore vyhovuju filtru z json suborov
     *
     * @param events
     */
    public List<SupportedBetEvent> filterEvents(List<ComplexBetEvent> events) {

        List<SupportedBetEvent> supportedEvents = new ArrayList<>();

        for (ComplexBetEvent complexEvent : events) {
            // hlavnym cielom je zistit ci tento event je podporovany a ak ano tak ho pridat do zoznamu eventov
            boolean supported = true;
            CleanBetEvent cbe = complexEvent.events.get(0);// podla prveho urcime vsetky jeho eventy
            Sport sport = getSportForBetEvent(cbe);
            League league = getLeagueForBetEvent(cbe);
            EventType eventType = getEventTypeForBetEvent(cbe);

            if (sport == null) {
                // ak sa nepodporuje dany sport tak ostatne nema zmysel riesit
                supported = false;
                unsupportedSports.add(cbe.getSport());
            } else {
                // mame sport, ak sa vyzaduje liga tak musime mat ligu, ak nie tak ligu neriesime
                if (league == null) {
                    // nemame ligu, bud taku ligu nemame v databaze alebo pre nas nie je relevantna napriklad pri tenise
                    if (sport.leagueRequired) {
                        supported = false;
                        unsupportedLeagues.add(sport.name_SK + " " + cbe.getLiga());
                    } else {
                        // ligu neriesime, spravime si defaultnu ligu
                        league = new League();
                        league.id = 0;
                    }
                } else {
                    // sport aj liga eventu su sparovane so sportom aj ligou v databaze
                }
            }
            if (supported) {
                // mame sport aj ligu podporovanu, zistime ci je aj eventtype podporovany
                if (eventType == null) {
                    supported = false;
                    unsupportedEventTypes.add(cbe.getTypEventu());
                } else {
                    // mame podporovany sport, ligu aj eventtype, uz len aby boli podporovani competitori a mozeme pridat do zoznamu event
                    Competitor[] competitors = getSupportedCompetitors(cbe, league, sport);
                    if (competitors[0].id == 0 || competitors[1].id == 0) { // musia mat id>0 inac su to defaultni competitori a nie ti z databazy
                        //supported=false; uz nam netreba pridavat lebo s tym viacej nepracujeme
                        if (competitors[0].id == 0) {
                            unsupportedCompetitors.add("sport=" + sport.name_SK + ", league=" + league.name_SK + ", competitor=" + competitors[0].name + "<-");
                        }
                        if (competitors[1].id == 0) {
                            unsupportedCompetitors.add("sport=" + sport.name_SK + ", league=" + league.name_SK + ", competitor=" + competitors[1].name + "<-");
                        }
                        unsupportedCompetitorsEvents.add(complexEvent);
                    } else {
                        supportedEvents.add(new SupportedBetEvent(sport, league, eventType, competitors[0], competitors[1], complexEvent));
                    }
                }
            }
        }

        return supportedEvents;
    }


    private void loadEntities() {
        ObjectMapper mapper = new ObjectMapper();

        sports = new ArrayList<>();
        leagues = new ArrayList<>();
        countries = new ArrayList<>();
        eventTypes = new ArrayList<>();

        try {
            countries = mapper.readValue(new File("src\\main\\resources\\countries.json"), new TypeReference<List<Country>>() {
            });
            sports = mapper.readValue(new File("src\\main\\resources\\sports.json"), new TypeReference<List<Sport>>() {
            });
            leagues = mapper.readValue(new File("src\\main\\resources\\leagues.json"), new TypeReference<List<League>>() {
            });
            eventTypes = mapper.readValue(new File("src\\main\\resources\\eventTypes.json"), new TypeReference<List<EventType>>() {
            });
            competitors = mapper.readValue(new File("src\\main\\resources\\competitors.json"), new TypeReference<List<Competitor>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(StatsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("sports size: " + sports.size() + " nacitane sports: " + sports);
        System.out.println("leagues size: " + leagues.size() + " nacitane leagues: " + leagues);
        System.out.println("countries size: " + countries.size() + " countries: " + countries);
        System.out.println("eventTypes size: " + eventTypes.size() + " eventTypes: " + eventTypes);
    }

    private Sport getSportFromID(League league) {
        for (Sport s : sports) {
            if (s.id == league.sport_ID) {
                return s;
            }
        }
        return null;
    }

    private String[] parseCompetitors(String competitors) {
        String s = competitors.split("    ")[0];
        int coutn = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-') {
                coutn++;
            }
        }
        if (coutn != 1) {
            System.out.println("ZLY POCET POMLCIEK: [" + s + "] pocet: " + coutn);
            throw new RuntimeException();
        }
        String[] comp = new String[2];
        s = s.substring(s.indexOf(" ")).trim();
        comp[0] = s.substring(0, s.indexOf("-")).trim();
        comp[1] = s.substring(s.indexOf("-") + 1).trim();
        return comp;
    }

    private Competitor[] getSupportedCompetitors(CleanBetEvent cbe, League league, Sport sport) {
        if (league == null || sport == null) {
            throw new NullPointerException("league alebo sport je NULL");
        }
        // vyparsujeme competitorov a zistime ci existuju nejaki neplatni
        String[] strings = parseCompetitors(cbe.getCompetitors());
        Competitor[] supported = new Competitor[2];
        // competitori sa porovnavaju iba podla name, id sportu a id ligy
        Competitor c1 = new Competitor();
        c1.name = strings[0];
        //c1.league_ID = league.id;
        c1.country_id=league.country_ID;
        c1.sport_ID = sport.id;
        Competitor c2 = new Competitor();
        c2.name = strings[1];
        //c2.league_ID = league.id;
        c2.country_id=league.country_ID;
        c2.sport_ID = sport.id;
        // nastavime hladaneho competitora aby ked sa nenajde sme vedeli jeho meno si zaznamenat aspon
        supported[0] = c1;
        supported[1] = c2;
        for (Competitor c : competitors) {
            if (c1.equals(c)) {
                supported[0] = c;
            }
            if (c2.equals(c)) {
                supported[1] = c;
            }
        }
        return supported;
    }

    private EventType getEventTypeForBetEvent(CleanBetEvent cbe) {
        for (EventType et : eventTypes) {
            if (et.name_SK.equals(cbe.getTypEventu())) {
                return et;
            }
        }
        return null;
    }

    private League getLeagueForBetEvent(CleanBetEvent cbe) {
        for (League lg : leagues) {
            if (lg.name_SK.equals(cbe.getLiga())) {
                return lg;
            }
        }
        return null;
    }

    private Sport getSportForBetEvent(CleanBetEvent sport) {
        for (Sport sp : sports) {
            if (sp.name_SK.equals(sport.getSport())) {
                return sp;
            }
        }
        return null;
    }
}

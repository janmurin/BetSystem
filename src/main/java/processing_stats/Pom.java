package processing_stats;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import processing_stats.entities.*;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jan.murin on 08-Sep-16.
 */
public class Pom {

    static List<Country> countries;
    static List<League> leagues;

    public static void main(String[] args) {
        //competitorGenerator();
    }

    public static void competitorGenerator() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            leagues = mapper.readValue(new File("src\\main\\resources\\leagues.json"), new TypeReference<List<League>>() {
            });
            countries = mapper.readValue(new File("src\\main\\resources\\countries.json"), new TypeReference<List<Country>>() {
            });
        } catch (IOException ex) {
            Logger.getLogger(StatsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        File file = new File("src\\main\\resources\\input.txt");
        Map<Integer, List<String>> competitori = new HashMap<>();
        for (int i = 0; i < leagues.size(); i++) {
            League league = leagues.get(i);
            if (i != league.id) {
                throw new RuntimeException("i!=id " + i);
            }
            competitori.put(i, new ArrayList<>());
        }

        BufferedReader f = null;
        try {
            f = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pom.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Pom.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            StringTokenizer st = null;
            try {
                String line = f.readLine();
                if (line == null) {
                    break;
                }
                line = line.replace("sport=Futbal, league=", "");
                String liga = line.substring(0, line.indexOf(','));
                String competitor = line.substring(line.indexOf("competitor="));
                competitor = competitor.replace("competitor=", "");
                competitor = competitor.substring(0, competitor.length() - 2);

                System.out.println("liga=[" + liga + "], competitor=[" + competitor + "]");
                for (League l : leagues) {
                    if (l.name_SK.equals(liga)) {
                        competitori.get(l.id).add(competitor);
                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(Pom.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }
        }

        int id = 0;
        // v ramci jednej krajiny identifikujem competitora podla nazvu
        Map<Integer,Set<String>> countryCompetitorsMap=new HashMap<>();
        for(Country c:countries){
            countryCompetitorsMap.put(c.id,new HashSet<>());
        }
        for(Integer key:competitori.keySet()){
            // pridame nejakej krajine competitorov z ligy
            countryCompetitorsMap.get(leagues.get(key).country_ID).addAll(competitori.get(key));
        }
        for(Integer key:countryCompetitorsMap.keySet()){
            Set<String> strings = countryCompetitorsMap.get(key);
            List<String> strs=new ArrayList<>(strings);
            Collections.sort(strs);
            for(String s:strs){
                Competitor competitor=new Competitor();
                competitor.id=id;
                competitor.name=s;
                competitor.country_id=key;
                competitor.sport_ID=1;
                System.out.println(competitor+",");
                id++;
            }
        }

//        for(Integer key:competitori.keySet()){
//            System.out.println("Ucastnici ligy: "+leagues.get(key));
//            List<String> strings = competitori.get(key);
//            Collections.sort(strings);
//            for(String s:strings){
//                System.out.println(s);
//            }
//            System.out.println();
//        }
    }

    public static void execute1() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            leagues = mapper.readValue(new File("src\\main\\resources\\leagues.json"), new TypeReference<List<League>>() {
            });
            countries = mapper.readValue(new File("src\\main\\resources\\countries.json"), new TypeReference<List<Country>>() {
            });
//            for(int i=0; i<leagues.size(); i++){
//                leagues.get(i).id=i;
//            }
//            System.out.println(leagues);
//            Collections.sort(countries);
//            for (int i = 0; i < countries.size(); i++) {
//                countries.get(i).id = i;
//            }
//
//            System.out.println(countries);
        } catch (IOException ex) {
            Logger.getLogger(StatsGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

//        Scanner sc = new Scanner(unsupportedLeagues);
//        while (sc.hasNextLine()) {
//            String line = sc.nextLine();
////            if (line.contains("pohár") || line.contains("Turnaj") || line.contains("zápasy") || line.contains("Kvalifikácia") || line.contains("Cup") || line.contains("baráž")) {
////
////            } else {
////                System.out.println(line.replaceAll("Futbal ", ""));
////            }
//            Country country = getCountry(line, countries);
//            League nova = new League();
//            nova.country_ID = country.id;
//            nova.country_name = country.name_SK;
//            nova.sport_ID = 1;
//            nova.name_SK = line;
//            System.out.println(nova+", ");
//            //System.out.println(line + " == " + country);
//        }
//        for(League l:leagues){
//            System.out.println(countries.get(l.country_ID).name_SK+" == "+l);
////            try {
////                Country country = getCountry(l.name_SK, countries);
////                l.country_ID = country.id;
////                System.out.println(l + ", "+country.name_SK);
////                //System.out.println(l + ", ");
////            }catch(Exception e){
////                System.out.println("liga: "+l);
////                Logger.getLogger(StatsGenerator.class.getName()).log(Level.SEVERE, null, e);
////            }
//        }

//        for(Country c:countries){
//            System.out.println("KRAJINA: "+c);
//            System.out.println("zoznam lig:");
//            for(League l:leagues){
//                if(l.country_ID==c.id){
//                    System.out.println("    "+l);
//                }
//            }
//            System.out.println();
//        }
    }

    public static Country getCountry(String s, List<Country> countries) {
        Country maxCountry = null;
        int max = 0;
        for (Country c : countries) {
            int lcs = lcs(c.name_SK, s).length();
            if (lcs > max) {
                max = lcs;
                maxCountry = c;
            }
        }

        return maxCountry;
    }

    public static String lcs(String a, String b) {
        int[][] lengths = new int[a.length() + 1][b.length() + 1];

        // row 0 and column 0 are initialized to 0 already

        for (int i = 0; i < a.length(); i++)
            for (int j = 0; j < b.length(); j++)
                if (a.charAt(i) == b.charAt(j))
                    lengths[i + 1][j + 1] = lengths[i][j] + 1;
                else
                    lengths[i + 1][j + 1] =
                            Math.max(lengths[i + 1][j], lengths[i][j + 1]);

        // read the substring out from the matrix
        StringBuffer sb = new StringBuffer();
        for (int x = a.length(), y = b.length();
             x != 0 && y != 0; ) {
            if (lengths[x][y] == lengths[x - 1][y])
                x--;
            else if (lengths[x][y] == lengths[x][y - 1])
                y--;
            else {
                assert a.charAt(x - 1) == b.charAt(y - 1);
                sb.append(a.charAt(x - 1));
                x--;
                y--;
            }
        }

        return sb.reverse().toString();
    }


    static String unsupportedLeagues = "1.Portugalsko\n" +
            "1.Singapur\n" +
            "2.Portugalsko\n" +
            "1.Cyprus\n" +
            "1.Nemecko-ženy\n" +
            "1.Izrael\n" +
            "3.Fínsko-juh\n" +
            "3.Česko\n" +
            "3.Francúzsko\n" +
            "Fortuna liga\n" +
            "2.Škótsko\n" +
            "1.Anglicko\n" +
            "1.Uruguay\n" +
            "1.Saudská Arábia\n" +
            "2.Taliansko\n" +
            "1.Malta\n" +
            "2.Anglicko\n" +
            "2.Slovensko-východ\n" +
            "1.Katar\n" +
            "2.Španielsko\n" +
            "2.Česko\n" +
            "3.Fínsko-západ\n" +
            "1.Salvádor\n" +
            "1.Turecko\n" +
            "3.Španielsko-sk.1\n" +
            "3.Španielsko-sk.3\n" +
            "3.Španielsko-sk.2\n" +
            "3.Španielsko-sk.4\n" +
            "1.Nemecko\n" +
            "1.Grécko\n" +
            "1.Holandsko\n" +
            "1.Česko-ženy\n" +
            "1.Bahrajn\n" +
            "3.Fínsko-sever\n" +
            "1.Arménsko\n" +
            "4.Škótsko\n" +
            "1.N.Zéland\n" +
            "1.Škótsko\n" +
            "2.Argentína\n" +
            "2.Holandsko\n" +
            "1.Argentína\n" +
            "3.Fínsko-východ\n" +
            "1.Sev.Írsko\n" +
            "1.Francúzsko\n" +
            "1.Španielsko\n" +
            "2.Izrael\n" +
            "1.SAE\n" +
            "1.Taliansko\n" +
            "1.Wales\n" +
            "1.Azerbajdžan\n" +
            "1.India\n" +
            "1.Bolívia\n" +
            "1.Austrália\n" +
            "3.Škótsko\n" +
            "2.Belgicko\n" +
            "4.Anglicko\n" +
            "2.Turecko\n" +
            "3.Slovensko-západ\n" +
            "3.Taliansko A\n" +
            "3.Taliansko B\n" +
            "2.Nemecko\n" +
            "3.Taliansko C";

}

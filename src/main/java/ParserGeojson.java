import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * GeoJSON parser
 */
public class ParserGeojson {

    final private static int LATITUDE = 0;
    final private static int LONGITUDE = 1;
    private List<Country> countries = new ArrayList<>();

    public List<Country> parse(String fileToParsePath) {

        /* JSON parser objet pour lire le fichier */
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(fileToParsePath)) {

            /* Lecture du fichier */
            Object obj = jsonParser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;

            /* Récupération des features */
            JSONArray features = (JSONArray) jsonObject.get("features");

            /* Pour chaque feature... */

            for (Object feature : features) {

                JSONObject jsonFeature = (JSONObject) feature;

                /* Récupération des properties */
                JSONObject properties = (JSONObject) jsonFeature.get("properties");
                String countryName = (String) properties.get("ADMIN");
                String countryShortName = (String) properties.get("ISO_A3");

                /* Récupération de geometry */
                JSONObject geometry = (JSONObject) jsonFeature.get("geometry");
                String polyType = (String) geometry.get("type");

                /* Récupération des coordinates */
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");

                /* Pour chaque paire de coordonnées... */
                List<List<StringPair>> coordinatesListList = new ArrayList<>();

                /* Traitement différent selon si il y a une ou plusieurs listes de coordonnées */
                if (polyType.equals("Polygon")) {
                    coordinatesListList = parsePolygon(coordinates);
                } else if (polyType.equals("MultiPolygon")) {
                    coordinatesListList = parseMultiPolygon(coordinates);
                }

                /* Création du pays */
                Country country = new Country(countryName, countryShortName, coordinatesListList);
                countries.add(country);

            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * Traite un pays ne possédant qu'une liste de coordonnées (polygone)
     *
     * @param coordinatesPair la liste des paires de coordonnées
     * @return une liste de listes de coordonnées
     */
    private static List<List<StringPair>> parsePolygon(JSONArray coordinatesPair) {

        /* Préparation des listes */
        List<StringPair> coordinatesList = new ArrayList<>();
        List<List<StringPair>> coordinatesListList = new ArrayList<>();

        /* Passage du premier niveau, invariable */
        JSONArray firstLevelArray = (JSONArray) coordinatesPair.get(0);

        /* Itération sur le second niveau contenant les coordonnées */
        for (Object coordinate : firstLevelArray) {
            JSONArray secondLevelArray = (JSONArray) coordinate;

            /* Création de la paire de coordonnées */
            StringPair pair = new StringPair(
                    Double.toString((double) secondLevelArray.get(LATITUDE)),
                    Double.toString((double) secondLevelArray.get(LONGITUDE)));

            /* Ajout dans la liste */
            coordinatesList.add(pair);
        }

        coordinatesListList.add(coordinatesList);

        return coordinatesListList;
    }

    /**
     * Traite un pays possédant plusieurs listes de coordonnées (multipolygone)
     *
     * @param coordinatesPair la liste des paires de coordonnées
     * @return une liste de listes de coordonnées
     */
    private static List<List<StringPair>> parseMultiPolygon(JSONArray coordinatesPair) {

        /* Préparation de la liste de liste */
        List<List<StringPair>> coordinatesListList = new ArrayList<>();

        /* Itération sur le premier niveau */
        for (Object cp : coordinatesPair) {

            JSONArray firstLevelArray = (JSONArray) cp;
            /* Passage au second niveau, invariable */
            JSONArray secondLevelArray = (JSONArray) firstLevelArray.get(0);

            List<StringPair> coordinatesList = new ArrayList<>();

            /* Itération sur le niveau trois contenant les coordonnées */
            for (Object coordinatePair : secondLevelArray) {
                JSONArray jsonArray = (JSONArray) coordinatePair;

                /* Création de la paire de coordonnées */
                StringPair pair = new StringPair(
                        Double.toString((double) jsonArray.get(LATITUDE)),
                        Double.toString((double) jsonArray.get(LONGITUDE)));

                /* Ajout dans la liste */
                coordinatesList.add(pair);
            }

            /* Ajout dans la liste de liste */
            coordinatesListList.add(coordinatesList);
        }

        return coordinatesListList;
    }
}

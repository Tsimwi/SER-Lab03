import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Parser {
    public static void main(String[] args) {

        //JSON parser object pour lire le fichier
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/main/resources/countries.geojson")) {

            /* Lecture du fichier */
            Object obj = jsonParser.parse(reader);

            JSONObject jsonObject = (JSONObject) obj;

            /* Récupération des features */
            JSONArray features = (JSONArray) jsonObject.get("features");
            features.forEach(featureObj->parseFeatures((JSONObject)featureObj));

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static void parseFeatures(JSONObject featureObj) {

        // Obtenir l'objet properties dans la liste
        JSONObject properties = (JSONObject) featureObj.get("properties");
        parseProperties(properties);

        // Obtenir l'objet properties dans la liste
        JSONObject geometry = (JSONObject) featureObj.get("geometry");
        parseGeometry(geometry);
    }

    private static void parseProperties(JSONObject propObj) {

        // Obtenir les détails
        String admin = (String) propObj.get("ADMIN");
        String iso = (String) propObj.get("ISO_A3");
    }

    private static void parseGeometry(JSONObject geometryObj) {

        // Obtenir les détails
        String type = (String) geometryObj.get("type");

        /* Récupération des coordinates */
        JSONArray coordinates = (JSONArray) geometryObj.get("coordinates");
        coordinates.forEach(coordObj->parseCoordinates((JSONArray) coordObj, type));
    }

    private static void parseCoordinates(JSONArray coordinatesObj, String type) {

        JSONArray jsonArray = (JSONArray) coordinatesObj;

        if (type.equals("Polygon")) {
            parsePolygon(jsonArray);
        } else if (type.equals("MultiPolygon")) {
            parseMultiPolygon(jsonArray);
        }
    }

    private static void parsePolygon(JSONArray polygonObj) {

        for (Object polyObj : polygonObj) {
            JSONArray lastArray = (JSONArray) polyObj;
            for (int i = 0; i < lastArray.size(); ++i) {
                double c = (double) lastArray.get(i);
                System.out.println(c);
            }
        }
    }

    private static void parseMultiPolygon(JSONArray polygonObj) {

        for (Object polyObj : polygonObj) {
            JSONArray beforeArray = (JSONArray) polyObj;

            for (Object multPolyObj : beforeArray) {
                JSONArray lastArray = (JSONArray) multPolyObj;

                //double c1 = (double) lastArray.get(0);

                for (int i = 0; i < lastArray.size(); ++i) {
                    double c = (double) lastArray.get(i);
                    System.out.println(c);
                }
            }
        }
    }
}

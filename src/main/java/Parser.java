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

            /* Pour chaque feature... */
            for (int iFeature = 0; iFeature < features.size(); ++iFeature) {

                JSONObject feature = (JSONObject) features.get(iFeature);

                /* Récupération des properties */
                JSONObject properties = (JSONObject) feature.get("properties");
                String admin = (String) properties.get("ADMIN");
                String iso = (String) properties.get("ISO_A3");

                /* Récupération de geometry */
                JSONObject geometry = (JSONObject) feature.get("geometry");
                String type = (String) geometry.get("type");

                /* Récupération des coordinates */
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");

                /* Pour chaque coordonnée... */
                for (int iCoordinate = 0; iCoordinate < coordinates.size(); ++iCoordinate) {

                    JSONArray coordinate = (JSONArray) coordinates.get(iCoordinate);
                    JSONArray coordArray = coordinate;

                    if (type.equals("Polygon")) {
                        parsePolygon(coordArray);
                    } else if (type.equals("MultiPolygon")) {
                        parseMultiPolygon(coordArray);
                    }

                }
            }
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

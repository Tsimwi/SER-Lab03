import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.parser.ParseException;

public class JSONParser {
    public static void main(String[] args) {

        //JSON parser object pour lire le fichier
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("src/jsonFile.json")) {

            // lecture du fichier
            Object obj = jsonParser.parse(reader);

            JSONArray personne = (JSONArray) obj;
            System.out.println(personne);

            // parcours du tableau de personnes
            personne.forEach(pers->parsePersonneObject((JSONObject)pers));

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

    private static void parsePersonneObject(JSONObject pers) {

        // Obtenir l'objet personne dans la liste
        JSONObject persObject = (JSONObject) pers.get("personne");

        // obtenir les détails ...
        String nom    = (String) persObject.get("nom");
        String prenom = (String) persObject.get("prenom");
        String email  = (String) persObject.get("email");

        // afficher le contenu
        System.out.println(nom);
        System.out.println(prenom);
        System.out.println(email);
    }
}

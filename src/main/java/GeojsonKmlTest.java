import java.util.List;

public class GeojsonKmlTest{
    public static void main(String[] args) {

        ParserGeojson parserGeojson = new ParserGeojson();
        List<Country> countries = parserGeojson.parse("src/main/resources/countries.geojson");

        for(Country country : countries)
            System.out.println(country);

        KMLWriter kmlWriter = new KMLWriter();
        kmlWriter.writeCountriesToKml(countries, "src/newKMLfile.kml");

    }
}

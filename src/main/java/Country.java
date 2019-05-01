import java.util.List;

public class Country {

    private String name;
    private String abbreviation;
    private List<List<StringPair>> coordinates;

    Country(String name, String abbreviation, List<List<StringPair>> coordinates) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.coordinates = coordinates;
    }

    String getName() {
        return this.name;
    }

    String getAbbreviation() {
        return this.abbreviation;
    }

    List<List<StringPair>> getCoordinates() {
        return this.coordinates;
    }

    @Override
    public String toString() {
        String result = "(" + this.abbreviation + ") " + this.name + "\n";

        for (List<StringPair> list : this.coordinates) {
            result += "      - " + list.size() + " coordinates\n";
        }

        return result;
    }
}

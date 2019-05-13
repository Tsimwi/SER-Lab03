public class StringPair {

    private String firstCoordinate;
    private String secondCoordinate;

    StringPair(String first, String second) {
        this.firstCoordinate = first;
        this.secondCoordinate = second;
    }

    @Override
    public String toString() {
        return firstCoordinate + "," +  secondCoordinate;
    }

    String getFirstCoordinate() {
        return this.firstCoordinate;
    }

    String getSecondCoordinate() {
        return this.secondCoordinate;
    }

    void setFirstCoordinate(String first) {
        this.firstCoordinate = first;
    }

    void setSecondCoordinate(String second) {
        this.secondCoordinate = second;
    }
}

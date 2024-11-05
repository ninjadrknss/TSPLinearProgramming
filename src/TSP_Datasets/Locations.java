// Alex Petry
// Traveling Salesman Problem #3
// Artificial Intelligence

package TSP_Datasets;

public class Locations {
    public enum Regions {
        AMERICAS,
        CANADA,
        CARIBBEAN,
        CENTRAL_AMERICA,
        EUROPE,
        NORTH_AMERICA,
        SOUTH_AMERICA,
        US
    }

    public static Region[] values(Regions loc) {
        return switch (loc) {
            case AMERICAS -> Americas.Country.values();
            case CANADA -> Canada.Province.values();
            case CARIBBEAN -> Caribbean.Country.values();
            case CENTRAL_AMERICA -> CentralAmerica.Country.values();
            case EUROPE -> Europe.Country.values();
            case NORTH_AMERICA -> NorthAmerica.Country.values();
            case SOUTH_AMERICA -> SouthAmerica.Country.values();
            case US -> US.State.values();
        };
    }

    public static String type(Regions loc) {
        return values(loc)[0].kind();
    }

    public static Region find(String s, Regions loc) {
        return switch (loc) {
            case AMERICAS -> Americas.find(s);
            case CANADA -> Canada.find(s);
            case CARIBBEAN -> Caribbean.find(s);
            case CENTRAL_AMERICA -> CentralAmerica.find(s);
            case EUROPE -> Europe.find(s);
            case NORTH_AMERICA -> NorthAmerica.find(s);
            case SOUTH_AMERICA -> SouthAmerica.find(s);
            case US -> US.find(s);
        };
    }
}

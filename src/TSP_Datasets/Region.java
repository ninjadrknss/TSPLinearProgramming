package TSP_Datasets;

public interface Region {

    // A class to represent general political subdivisions such as
    // countries, states, provinces, etc.

    static Region find(String name, Region[] regions) {
        for (Region region : regions) {
            if (name.equalsIgnoreCase(region.code())) return region;
            if (name.equalsIgnoreCase(region.name())) return region;
            if (name.equalsIgnoreCase(region.capital().name())) return region;
        }
        return null;
    }

    static String kind(Region[] regions) {
        return regions[0].kind();
    }

    int ordinal();  // Ordinal position of this region in its country/etc.

    String name();  // Full name of this region

    String code();  // ISO code for this region

    City capital(); // Capital city of this region

    String kind();  // State, Province, Country, etc
}

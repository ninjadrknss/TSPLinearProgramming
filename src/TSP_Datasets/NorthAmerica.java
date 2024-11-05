package TSP_Datasets;

public class NorthAmerica {

    public enum Country implements Region {
        
        Bermuda      ("BMU", "Bermuda",       "Hamilton",       32.2951, -67.7842, "UK"),
        Canada       ("CAN", "Canada",        "Ottawa",         45.4201, -75.7003),
        Greenland    ("GRL", "Greenland",     "Nuuk",           64.1743, -51.7373, "DK"),
        Mexico       ("MEX", "Mexico",        "Mexico Ciy",     19.4326, -99.1332),
        UnitedStates ("USA", "United States", "Washington DC",  38.9072, -77.0369);


        private final String code;
        private final String name;
        private final City capital;
        private final String sovereignty;

        Country(String code, String name, String capital, double lat, double lon, String sovereignty) {
            this.capital = new City(capital, this, lat, lon);
            this.code = code;
            this.name = name;
            this.sovereignty = sovereignty;
        }

        Country(String code, String name, String capital, double lat, double lon) {
            this(code, name, capital, lat, lon, null);
        }

        @Override
        public String code() {
            return this.code;
        }

        @Override
        public City capital() {
            return this.capital;
        }

        public String sovereignty() {
            return this.sovereignty;
        }

        @Override
        public String kind() {
            return "Country";
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static Country find(String name) {
        return (Country) Region.find(name, Country.values());
    }
}

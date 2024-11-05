package TSP_Datasets;

public record City(String name, Region region, double latitude, double longitude) {

    public double distance(City to) {
        final double earthRadius = 6335.439; // kilometers
        double phi1 = Math.toRadians(this.latitude);
        double phi2 = Math.toRadians(to.latitude);
        double lambda1 = Math.toRadians(this.longitude);
        double lambda2 = Math.toRadians(to.longitude);
        double deltaLambda = lambda2 - lambda1;

        double centralAngle = Math.acos(Math.sin(phi1) * Math.sin(phi2) +
                Math.cos(phi1) * Math.cos(phi2) * Math.cos(deltaLambda));

        return earthRadius * centralAngle;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

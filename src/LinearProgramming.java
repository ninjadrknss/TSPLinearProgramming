import TSP_Datasets.Locations;
import TSP_Datasets.Locations.Regions;
import TSP_Datasets.Region;
import TSP_Datasets.SouthAmerica;
import TSP_Datasets.US;
import TSP_Tools.EdgePlot;

public class LinearProgramming {
    // Solver: https://online-optimizer.appspot.com/

    private static final Regions loc = Regions.SOUTH_AMERICA;
    private static final Region[] regions = Locations.values(loc);

    public static void main(String[] args) {
        // Construct and limit all edges
        for (Region r_1 : regions) {
            for (Region r_2 : regions) {
                if (r_1 == r_2) break;

                System.out.printf("var edge_%s_%s >= 0;%n", r_1.code(), r_2.code());
                System.out.printf("subject to c1_%s_%s: edge_%s_%s <= 1; %n", r_1.code(), r_2.code(), r_1.code(), r_2.code());
            }
        }
        System.out.println();

        // Limit all vertices to have exactly two incident edges
        for (Region r_1 : regions) {
            boolean swap = false;
            System.out.printf("subject to c2_%s: ", r_1.code());
            String sep = "";
            for (Region r_2 : regions) {
                if (r_1 == r_2) {
                    swap = true;
                    continue;
                }

                if (!swap) System.out.printf("%sedge_%s_%s", sep, r_1.code(), r_2.code());
                else System.out.printf("%sedge_%s_%s", sep, r_2.code(), r_1.code());

                sep = " + ";
            }
            System.out.println(" = 2;");
        }
        System.out.println();

        // Sub-tour elimination constraints
        if (loc == Regions.SOUTH_AMERICA) {
            int n = 3;
            Region[][] sets = new Region[n][];

            sets[0] = new SouthAmerica.Country[]{SouthAmerica.Country.Guyana, SouthAmerica.Country.FrenchGuiana, SouthAmerica.Country.Suriname};
            sets[1] = new SouthAmerica.Country[]{SouthAmerica.Country.Chile, SouthAmerica.Country.Argentina, SouthAmerica.Country.Paraguay, SouthAmerica.Country.Uruguay};
            sets[2] = new SouthAmerica.Country[]{SouthAmerica.Country.Chile, SouthAmerica.Country.Argentina, SouthAmerica.Country.Uruguay};

            for (int i = 0; i < n; i++) subTourElimination(i + 1, sets[i]);
            System.out.println();
        } else if (loc == Regions.US) {
            int n = 16;
            Region[][] sets = new Region[n][];

            sets[0]  = new Region[]{US.State.Oklahoma, US.State.Texas, US.State.Louisiana, US.State.Arkansas, US.State.Mississippi};
            sets[1]  = new Region[]{US.State.NewYork, US.State.Vermont, US.State.Maine, US.State.NewHampshire, US.State.Massachusetts, US.State.RhodeIsland, US.State.Connecticut};
            sets[2]  = new Region[]{US.State.NewYork, US.State.Vermont, US.State.Maine, US.State.NewHampshire, US.State.Massachusetts, US.State.RhodeIsland, US.State.Connecticut, US.State.NewJersey};
            sets[3]  = new Region[]{US.State.NewYork, US.State.Vermont, US.State.Maine, US.State.NewHampshire, US.State.Massachusetts, US.State.RhodeIsland, US.State.Connecticut, US.State.NewJersey, US.State.Delaware, US.State.Pennsylvania};
            sets[4]  = new Region[]{US.State.NewYork, US.State.Vermont, US.State.Maine, US.State.NewHampshire, US.State.Massachusetts, US.State.RhodeIsland, US.State.Connecticut, US.State.NewJersey, US.State.Delaware, US.State.Maryland, US.State.Pennsylvania};
            sets[5]  = new Region[]{US.State.Maryland, US.State.Delaware, US.State.Virginia};
            sets[6]  = new Region[]{US.State.California, US.State.Oregon, US.State.Washington, US.State.Idaho, US.State.Nevada, US.State.Montana, US.State.Utah};
            sets[7]  = new Region[]{US.State.California, US.State.Oregon, US.State.Washington, US.State.Idaho, US.State.Montana, US.State.NorthDakota, US.State.SouthDakota, US.State.Wyoming, US.State.Colorado, US.State.NewMexico, US.State.Arizona, US.State.Utah, US.State.Nevada};
            sets[8]  = new Region[]{US.State.NorthDakota, US.State.SouthDakota, US.State.Minnesota};
            sets[9]  = new Region[]{US.State.Utah, US.State.Wyoming, US.State.Colorado, US.State.NewMexico, US.State.Arizona};
            sets[10] = new Region[]{US.State.California, US.State.Oregon, US.State.Washington, US.State.Idaho, US.State.Montana, US.State.Utah, US.State.Wyoming, US.State.Colorado, US.State.NewMexico, US.State.Arizona, US.State.Nevada};
            sets[11] = new Region[]{US.State.Nebraska, US.State.Iowa, US.State.Kansas};
            sets[12] = new Region[]{US.State.Alabama, US.State.Florida, US.State.SouthCarolina, US.State.Georgia};
            sets[13] = new Region[]{US.State.Iowa, US.State.Nebraska, US.State.Kansas, US.State.Oklahoma};
            sets[14] = new Region[]{US.State.Arkansas, US.State.Mississippi, US.State.Louisiana};
            sets[15] = new Region[]{US.State.California, US.State.Oregon, US.State.Washington, US.State.Montana, US.State.Idaho, US.State.Nevada};

            for (int i = 0; i < n; i++) subTourElimination(i + 1, sets[i]);
            System.out.println();
        }

        // Now minimize the cost of the sum of all edges multiplied by their edge weights
        System.out.print("minimize z: ");
        String sep = "";
        for (Region r_1 : regions) {
            for (Region r_2 : regions) {
                if (r_1 == r_2) break;
                System.out.printf("%s%f * edge_%s_%s", sep, r_1.capital().distance(r_2.capital()), r_1.code(), r_2.code());
                sep = "\n + ";
            }
        }
        System.out.println(";");
        System.out.println();

        System.out.println("solve;");

        // Print the values of the edges
        for (Region r_1 : regions) {
            for (Region r_2 : regions) {
                if (r_1 == r_2) break;
                String edge = String.format("edge_%s_%s", r_1.code(), r_2.code());
                System.out.printf("printf if %s > 0 then '%s = %%f\\n' else \"\", %s;%n", edge, edge, edge);
            }
        }
        System.out.println();

        System.out.println("end;");

        System.out.println("# Paste Plot:");
        EdgePlot.main(new String[0]);

        // US Optimal ~ 17,002 km
    }

    private static Region[] fillOther(Region[] setA) {
        int index = 0;
        Region[] setB = new Region[regions.length - setA.length];

        Outer:
        for (Region r : regions) {
            for (Region region : setA) {
                if (r.equals(region)) continue Outer; // If contains
            }

            setB[index++] = r;
        }

        return setB;
    }

    private static void subTourElimination(int n, Region[] setA) {
        Region[] setB = fillOther(setA);
        System.out.printf("subject to st%d: ", n);

        String sep = "";
        for (Region a : setA) {
            for (Region b : setB) {
                if (a.ordinal() > b.ordinal()) System.out.printf("%sedge_%s_%s", sep, a.code(), b.code());
                else System.out.printf("%sedge_%s_%s", sep, b.code(), a.code());
                sep = " + ";
            }
        }
        System.out.println(" = 2;");
    }
}
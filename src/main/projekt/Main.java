package projekt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int startingAnimals = Integer.parseInt(args[0]);
        int numOfSimulations = 1;
        int delay = Integer.parseInt(args[2]);
        if (numOfSimulations == 1) {
            try {
                WorldMap map = new WorldMap(new MapProperties(new FileReader("parameters.json")), startingAnimals);
                SimulationEngine simulationEngine = new SimulationEngine(map, delay);
                simulationEngine.simulate();

            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        } else if(numOfSimulations == 2){
            try {
                WorldMap map1 = new WorldMap(new MapProperties(new FileReader("parameters.json")), startingAnimals);
                WorldMap map2 = new WorldMap(new MapProperties(new FileReader("parameters.json")), startingAnimals);
                SimulationEngine simulationEngine1 = new SimulationEngine(map1,delay);
                SimulationEngine simulationEngine2 = new SimulationEngine(map2,delay);
                simulationEngine1.simulate();
                simulationEngine2.simulate();
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        } else {
            System.out.println("Number of simulations has to be equal 1 or 2");
        }
    }
}

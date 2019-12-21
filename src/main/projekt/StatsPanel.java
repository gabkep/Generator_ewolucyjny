package projekt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

public class StatsPanel extends JPanel{
    private WorldMap map;
    private SimulationEngine simulationEngine;
    public Animal trackedAnimal;

    public StatsPanel(WorldMap map, SimulationEngine simulationEngine) {
        this.map = map;
        this.simulationEngine = simulationEngine;
        this.trackedAnimal = null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize(simulationEngine.frame.getWidth()/2, simulationEngine.frame.getHeight());
        this.setLocation(0,0);
        g.drawString("Liczba zwierząt: "+map.animals.size(), 10, 20);
        g.drawString("Liczba roślin: "+map.plants.size(),10,50);
        g.drawString("Dominujący genotyp: ", 10, 80);
        g.drawString(map.getDominantGenotype().toString(),10, 110);

        Double energySum = 0.0;
        Double childrenSum = 0.0;

        Iterator<Animal> iterator = map.animals.values().iterator();
        while (iterator.hasNext()){
            Animal animal = iterator.next();
            energySum += animal.getEnergyLevel();
            childrenSum += animal.childrenCount;
        }

        Double avgEnergy = energySum / map.animals.size();
        Double avgChildren = childrenSum / map.animals.size();

        g.drawString("Średni poziom energii: "+ avgEnergy.toString(), 10, 140);
        g.drawString("Średnia długość życia martwych zwierząt: "+ map.avgLifeLength(), 10, 170);
        g.drawString("Średnia ilość dzieci żywych zwierząt: "+ avgChildren.toString(), 10, 200);
        JButton button= new JButton("Stop");
        button.setLocation(10, 230);
        this.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (simulationEngine.isStopped)
                    simulationEngine.restart();
                else simulationEngine.pause();
            }
        });

        if (trackedAnimal != null) {
            g.drawString("Liczba wszystkich dzieci: "+trackedAnimal.childrenCount,10, 230);
            g.drawString("Liczba wszystkich potomków: to be done", 10, 260);
            if (trackedAnimal.dayOfDeath != -1) {
                g.drawString("Dzień śmierci: "+ trackedAnimal.dayOfDeath,10, 290);
            }
        }

    }

}

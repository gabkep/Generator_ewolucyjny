package projekt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimulationEngine implements ActionListener{
    private int delay;
    private WorldMap map;
    public JFrame frame;
    private MapVisualisation mapVisualisation;
    public StatsPanel statsPanel;
    public Timer timer;
    public boolean isStopped;

    public SimulationEngine(WorldMap map, int delay)
    {
        this.delay = delay;
        this.map = map;
        timer = new Timer(delay, this);
        frame = new JFrame("Simulation");
        frame.setSize(1200,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        this.mapVisualisation = new MapVisualisation(map,this);
        mapVisualisation.setSize(new Dimension(1,1));
        frame.add(mapVisualisation);
        this.statsPanel = new StatsPanel(map, this);
        statsPanel.setSize(new Dimension(1,1));
        frame.add(statsPanel);
    }

    public void simulate() {
        isStopped = false;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            mapVisualisation.repaint();
            statsPanel.repaint();
            Thread.sleep((long)delay/2);
            map.currentDay++;
            map.deleteDeadAnimals();
            map.run();
            map.eat();
            map.breedAnimals();
            map.spawnPlants();
            Thread.sleep((long)delay/2);
        } catch (InterruptedException ex) {
            ;
        }
    }

    public void pause() {
        isStopped = true;
        this.timer.stop();
    }

    public void restart() {
        isStopped = false;
        this.timer.restart();
    }
}

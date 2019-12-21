package projekt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MapVisualisation extends JPanel implements MouseListener {
    private WorldMap map;
    private SimulationEngine simulationEngine;
    private int pixelHeight;
    private int pixelWidth;

    public MapVisualisation(WorldMap map, SimulationEngine simulationEngine)
    {
        this.map = map;
        this.simulationEngine = simulationEngine;
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setSize(simulationEngine.frame.getWidth()/2, simulationEngine.frame.getHeight());
        this.setLocation(this.simulationEngine.frame.getWidth()/2,0);
        this.pixelHeight = (int)Math.round((double)this.getHeight() / (double)map.height);
        this.pixelWidth = (int)Math.round((double)this.getWidth() / (double)map.width);
        g.setColor(new Color(225, 252, 189));
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        g.setColor(new Color(173, 252, 68));
        g.fillRect(map.jungleStart.x*pixelWidth,map.jungleStart.y*pixelHeight, map.jungleWidth*pixelWidth, map.jungleHeight*pixelHeight);

        g.setColor(new Color(98, 171, 3));
        Iterator<Vector2d> plantIterator = map.plants.values().iterator();
        while (plantIterator.hasNext()) {
            Vector2d vec = plantIterator.next();
            g.fillRect(vec.x*pixelWidth, vec.y*pixelHeight, pixelWidth,pixelHeight);
        }

        Iterator<ArrayList<Animal>> animalIterator = map.elements.values().iterator();
        while (animalIterator.hasNext()) {
            ArrayList<Animal> list = animalIterator.next();
            if(!list.isEmpty()) {
                Collections.sort(list,Collections.reverseOrder());
                g.setColor(list.get(0).getColor());
                g.fillRect(list.get(0).getPosition().x * pixelWidth,
                        list.get(0).getPosition().y*pixelHeight,pixelWidth,pixelHeight);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (simulationEngine.isStopped) {
            Vector2d position;
            int x = mouseEvent.getX() / this.pixelWidth;
            int y = mouseEvent.getY() /this.pixelHeight;
            System.out.println(x);
            System.out.println(y);
            if (x < map.width && y < map.height) {
                position = new Vector2d(x, y);
                if (map.isOccupied(position)) {
                    ArrayList<Animal> list = map.elements.get(map.positionNumber(position));
                    Collections.sort(list,Collections.reverseOrder());
                    simulationEngine.statsPanel.trackedAnimal = list.get(0);
                }
            } else {
                simulationEngine.statsPanel.trackedAnimal = null;
            }
        }
    }
    public void mousePressed(MouseEvent event){}

    public void mouseReleased(MouseEvent event){}

    public void mouseEntered(MouseEvent event){}

    public void mouseExited(MouseEvent event){}
}

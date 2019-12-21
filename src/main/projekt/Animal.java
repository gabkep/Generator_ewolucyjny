package projekt;


import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Animal implements Comparable{
    private Vector2d position;
    private MapDirection direction;
    private WorldMap map;
    private Genotype genotype;
    private Integer energyLevel;
    private int startEnergy;
    private List<IPositionChangeObserver> observers;
    private int daysAlive;
    public int childrenCount;
    public int dayOfDeath;

    public Animal(WorldMap map, Vector2d initialPosition, int startEnergy) {
        this.position = initialPosition;
        this.direction = MapDirection.getRandomDirection();
        this.map = map;
        this.genotype = new Genotype();
        this.energyLevel = startEnergy;
        this.startEnergy = startEnergy;
        this.daysAlive = 0;
        this.childrenCount = 0;
        this.dayOfDeath = -1;
        this.observers = new LinkedList<>();
        observers.add((IPositionChangeObserver)map);
    }

    public Animal(WorldMap map, Vector2d initialPosition, Genotype genotype, int startEnergy) {
        this.position = initialPosition;
        this.direction = MapDirection.getRandomDirection();
        this.map = map;
        this.genotype = genotype;
        this.startEnergy = startEnergy;
        this.energyLevel = startEnergy;
        this.observers = new LinkedList<>();
        observers.add((IPositionChangeObserver)map);
    }

    public Animal breedWith(Animal animal)
    {
        boolean freeSpace = false;
        MapDirection direction = MapDirection.N;
        for (int i = 0; i < 9; i++) {
            if (!map.isOccupied(this.position.add(direction.toUnitVector()))) {
                freeSpace = true;
                break;
            }
            direction = direction.changeDirection(1);
        }
        Vector2d adjacentPosition;
        if(freeSpace) {
            do {
                adjacentPosition = this.position.add(MapDirection.getRandomDirection().toUnitVector());
            } while (map.isOccupied(adjacentPosition));
        } else {
            adjacentPosition = this.position.add(MapDirection.getRandomDirection().toUnitVector());
        }
        int childEnergy = this.energyLevel / 4 + animal.getEnergyLevel() / 4;
        this.energyLevel = this.energyLevel - this.energyLevel / 4;
        animal.eat(-(animal.getEnergyLevel()/4));
        Genotype childGenotype = new Genotype(this.genotype, animal.getGenotype());
        this.childrenCount++;
        animal.childrenCount++;
        return new Animal(this.map, adjacentPosition, childGenotype, childEnergy);
    }

    public Color getColor()
    {
        if (this.energyLevel <= 0.25 * startEnergy) return new Color(191, 8, 38);
        if (this.energyLevel <= 0.5 * startEnergy) return new Color(237, 142, 26);
        if (this.energyLevel <= 0.75 * startEnergy) return new Color(189, 222, 27);
        return new Color(27, 222, 183);
    }

    public Vector2d getPosition()
    {
        return this.position;
    }

    public int getDaysAlive() {
        return daysAlive;
    }

    public String toString()
    {
        return this.direction.toString();
    }

    public void move(int moveEnergy)
    {
        this.direction = this.direction.changeDirection(this.genotype.drawRandomGene());
        Vector2d newPos = this.map.calculatePosition(this.position.add(direction.toUnitVector()));
        Vector2d oldPosition = this.position;
        this.position = newPos;
        this.energyLevel -= moveEnergy;
        this.daysAlive++;
        if (this.energyLevel < 0)
            this.dayOfDeath = map.currentDay+1;
        positionChanged(oldPosition);
    }

    public void eat(int plantEnergy)
    {
        this.energyLevel += plantEnergy;
    }

    public Integer getEnergyLevel()
    {
        return energyLevel;
    }

    public int compareTo(Object animal) {
        if (this == animal)
            return 0;
        return this.energyLevel.compareTo(((Animal)animal).getEnergyLevel());
    }

    public Genotype getGenotype()
    {
        return new Genotype(this.genotype);
    }

    void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, this.position, this);
        }
    }
}

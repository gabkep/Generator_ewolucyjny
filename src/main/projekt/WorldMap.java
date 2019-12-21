package projekt;

import java.lang.reflect.Array;
import java.util.*;
import java.lang.Math;

public class WorldMap implements IPositionChangeObserver {
    public Map<Integer, ArrayList<Animal>> elements;
    public HashMap<Integer, Animal> animals;
    public HashMap<Integer, Vector2d> plants;
    public int width;
    public int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    public int jungleWidth;
    public int jungleHeight;
    public Vector2d jungleStart;
    public Vector2d jungleEnd;
    private int mapArea;
    private int jungleArea;
    private int plantCount;
    private int junglePlantCount;
    private int deadCount;
    private int daysLived;
    public int currentDay;


    public WorldMap(MapProperties mapProperties, int startingAnimals) {
        this.width = mapProperties.getWidth();
        this.height = mapProperties.getHeight();
        this.startEnergy = mapProperties.getStartEnergy();
        this.moveEnergy = mapProperties.getMoveEnergy();
        this.plantEnergy = mapProperties.getPlantEnergy();
        double jungleRatio = mapProperties.getJungleRatio();

        this.mapArea = this.width * this.height;
        double jungleArea = (double)mapArea * jungleRatio;
        this.jungleWidth = (int) Math.sqrt(jungleArea);
        this.jungleHeight = (int) jungleArea / jungleWidth;
        this.jungleArea = this.jungleWidth * this.jungleHeight;
        this.jungleStart = new Vector2d(((width-1)/2 - jungleWidth/2), ((height-1)/2 - jungleHeight/2));
        this.jungleEnd = jungleStart.add(new Vector2d(jungleWidth-1,jungleHeight-1));

        this.deadCount = 0;
        this.daysLived = 0;
        this.plantCount = 0;
        this.junglePlantCount = 0;
        this.currentDay = 0;
        this.elements = new HashMap<>();
        this.animals = new HashMap<>();
        this.plants = new HashMap<>();

        if (startingAnimals > mapArea)
            startingAnimals = mapArea;
        Random rnd = new Random();
        while (startingAnimals > 0) {
            Vector2d position;
            do {
                position = new Vector2d(rnd.nextInt(width), rnd.nextInt(height));
            } while (isOccupied(position));
            Animal animal = new Animal(this, position, startEnergy);
            placeAnimal(animal);
            startingAnimals--;
        }


    }

    public void deleteDeadAnimals()
    {
        Iterator<Animal> iterator = animals.values().iterator();
        while (iterator.hasNext()) {
            Animal animal = iterator.next();
            if (animal.getEnergyLevel() <= 0) {
                removeElementFromPosition(animal, animal.getPosition());
                iterator.remove();
                this.deadCount++;
                this.daysLived += animal.getDaysAlive();
            }
        }
    }

    public void run()
    {
        for(Animal animal : animals.values()) {
            animal.move(this.moveEnergy);
        }
    }

    public void eat()
    {
        Iterator<Vector2d> iterator = plants.values().iterator();
        while (iterator.hasNext()) {
            Vector2d plantPosition = iterator.next();
            if(isOccupied(plantPosition)) {
                ArrayList<Animal> animalsAtPosition = elements.get(positionNumber(plantPosition));
                ArrayList<Animal> strongestAnimals = new ArrayList<>();
                int maxEnergy = 0;
                for (Animal animal: animalsAtPosition) {
                    if (animal.getEnergyLevel() > maxEnergy)
                        maxEnergy = animal.getEnergyLevel();
                }
                for (Animal animal : animalsAtPosition) {
                    if (animal.getEnergyLevel() == maxEnergy)
                        strongestAnimals.add(animal);
                }
                if (strongestAnimals.size() == 0)
                    return;
                int energy = plantEnergy / strongestAnimals.size();
                for (Animal animal : strongestAnimals)
                    animal.eat(energy);
                iterator.remove();
                this.plantCount--;
                if (jungleStart.precedes(plantPosition) && jungleEnd.follows(plantPosition))
                    this.junglePlantCount--;
            }
        }
    }

    public void spawnPlants()
    {
        if (this.plantCount - this.junglePlantCount < this.mapArea - this.jungleArea) {
            while(!placePlantOutsideJungle());
        }
        if (this.junglePlantCount < this.jungleArea) {
            while (!placePlantInJungle());
            this.junglePlantCount++;
        }
    }

    public void breedAnimals()
    {
        LinkedList<Animal> addedAnimals = new LinkedList<>();
        for (Integer position : this.elements.keySet()) {
            if (isOccupied(positionNumberToVec(position)) && elements.get(position).size() >= 2) {
                Collections.sort(elements.get(position),Collections.reverseOrder());
                ArrayList<Animal> animals = elements.get(position);
                if (animals.get(0).getEnergyLevel() > startEnergy/2 && animals.get(1).getEnergyLevel() > startEnergy/2)
                    addedAnimals.add(animals.get(0).breedWith(animals.get(1)));
            }
        }
        for (Animal animal  : addedAnimals) {
            placeAnimal(animal);
        }
    }

    public Genotype getDominantGenotype()
    {
        ArrayList<Genotype> list = new ArrayList<>();
        Iterator<Animal> iterator = animals.values().iterator();
        while (iterator.hasNext()){
            list.add(iterator.next().getGenotype());
        }
        Collections.sort(list);
        Genotype dominantGenotype = list.get(0);
        int maxOccurrences = 0;
        Genotype currentGenotype = dominantGenotype;
        int occurrences = 0;
        for (Genotype genotype : list) {
            if (!genotype.equals(currentGenotype)){
                if(occurrences > maxOccurrences) {
                    maxOccurrences = occurrences;
                    occurrences = 1;
                    dominantGenotype = currentGenotype;
                    currentGenotype = genotype;
                }
            } else {
                occurrences++;
            }
        }
        return dominantGenotype;
    }

    public boolean isOccupied(Vector2d position) {
        List elementsAtPosition = elements.get(positionNumber(position));
        return !((elementsAtPosition == null) ||elementsAtPosition.isEmpty());
    }

    public double avgLifeLength()
    {
        return (double)this.daysLived / (double)this.deadCount;
    }

    public Integer getAnimalsCount()
    {
        return animals.size();
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal animal)
    {
        removeElementFromPosition(animal, oldPosition);
        placeAnimal(animal);
    }

    public Vector2d calculatePosition(Vector2d position) {
        int x = position.x;
        int y = position.y;
        if (x < 0) x = this.width + x;
        if (y < 0) y = this.height + y;
        if (x >= this.width) x = x % width;
        if (y >= this.height) y = y % height;
        return new Vector2d(x,y);
    }

    private Vector2d placeAnimal(Animal animal) {
        if (!isOccupied(animal.getPosition())) {
            ArrayList<Animal> elementsAtPosition = new ArrayList<>();
            elementsAtPosition.add(animal);
            elements.put(positionNumber(animal.getPosition()), elementsAtPosition);
        } else {
            ArrayList<Animal> elementsAtPosition = elements.get(positionNumber(animal.getPosition()));
            elementsAtPosition.add(animal);
        }
        if(!animals.containsValue(animal))
            animals.put(animal.hashCode(), animal);
        return animal.getPosition();
    }

    private boolean placePlant(Vector2d plant)
    {
        if(plants.containsKey(positionNumber(plant)))
            return false;
        plants.put(positionNumber(plant), plant);
        this.plantCount++;
        return true;
    }

    private boolean placePlantInJungle()
    {
        Random rnd = new Random();
        int x = rnd.nextInt(this.jungleWidth) + this.jungleStart.x;
        int y = rnd.nextInt(this.jungleHeight) + this.jungleStart.y;
        return placePlant(new Vector2d(x,y));
    }

    private boolean placePlantOutsideJungle()
    {
        Random rnd = new Random();
        Vector2d vec;
        do {
            vec = new Vector2d(rnd.nextInt(width), rnd.nextInt(height));
        } while (jungleStart.precedes(vec) && jungleEnd.follows(vec));
        return placePlant(vec);
    }

    private boolean removeElementFromPosition(Animal animal, Vector2d position)
    {
            if(!isOccupied(position)) {
                return false;
            }
            ArrayList<Animal> elementsAtPosition = elements.get(positionNumber(position));
            if(!elementsAtPosition.contains(animal))
                return false;
            elementsAtPosition.remove(animal);
            return true;
    }

    public Integer positionNumber(Vector2d position)
    {
        return position.x + position.y * this.width;
    }

    private Vector2d positionNumberToVec(Integer positionNumber)
    {
        return new Vector2d(positionNumber%width, positionNumber/width);
    }
}

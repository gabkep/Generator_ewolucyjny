package projekt;

import java.util.Arrays;
import java.util.Random;

public class Genotype implements Comparable{
    private Integer[] geneCount;
    private Integer[] genes;

    public Genotype()
    {
        this.geneCount = new Integer[8];
        this.genes = new Integer[32];
        Random rnd = new Random();
        for (int i = 0; i < 32; i++){
            this.genes[i] = rnd.nextInt(8);
        }
        this.countGenes();
        this.addMissingGenes();
        this.sortGenes();
    }

    public Genotype(Genotype genotype)
    {
        this.geneCount = new Integer[8];
        this.genes = new Integer[32];
        Integer[] otherGeneCount = genotype.getGeneCountArray();
        Integer[] otherGenes = genotype.getGeneArray();
        for (int i = 0; i  < 8; i++) {
            this.geneCount[i] = otherGeneCount[i];
        }
        for (int i = 0; i < 32; i++) {
            this.genes[i] = otherGenes[i];
        }
    }

    public Genotype(Genotype firstParent, Genotype secondParent)
    {
        this.geneCount = new Integer[8];
        this.genes = new Integer[32];
        Random rnd = new Random();
        int[] indices = new int[4];
        indices[0] = 0;
        indices[1] = rnd.nextInt(30) + 1;
        indices[2] = rnd.nextInt(31 - indices[1]) + indices[1] + 1;
        indices[3] = 32;
        int part = rnd.nextInt(3);
        Integer[] firstParentGenes = firstParent.getGeneArray();
        Integer[] secondParentGenes = secondParent.getGeneArray();
        for (int i = 0; i < 3; i++) {
            if (i == part) {
                for (int j = indices[i]; j < indices[i + 1]; j++) {
                    this.genes[j] = secondParentGenes[j];
                }
            } else {
                for (int j = indices[i]; j < indices[i + 1]; j++) {
                    this.genes[j] = firstParentGenes[j];
                }
            }
        }
        this.countGenes();
        this.addMissingGenes();
        this.sortGenes();
    }

    public Genotype(Integer[] geneArray) // for testing purpose only
    {
        this.geneCount = new Integer[8];
        this.genes = new Integer[32];
        for (int i = 0; i < 32; i++) {
            this.genes[i] = geneArray[i];
        }
        this.countGenes();
        this.addMissingGenes();
        this.sortGenes();
    }

    public Integer drawRandomGene(){
        Random rnd = new Random();
        return this.genes[rnd.nextInt(32)];
    }

    public Integer[] getGeneArray() {
        Integer[] ret = new Integer[32];
        for (int i = 0 ; i < 32; i++)
            ret[i] = this.genes[i];
        return ret;
    }

    public Integer[] getGeneCountArray()
    {
        Integer[] ret = new Integer[8];
        for (int i = 0; i < 8; i++)
            ret[i] = this.geneCount[i];
        return ret;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(this.genes);
    }

    private void countGenes()
    {
        for (int i = 0; i < 8; i++)
           this.geneCount[i] = 0;
        for(int i = 0; i < 32; i++)
            this.geneCount[this.genes[i]]++;
    }

    private void sortGenes()
    {
        int startIndex = 0;
        for(int i = 0; i < 8; i++) {
            for (int j = startIndex; j < startIndex + this.geneCount[i]; j++) {
                this.genes[j] = i;
            }
            startIndex += this.geneCount[i];
        }
    }

    private void addMissingGenes()
    {
        Random rnd = new Random();
        int gene = 0;
        for (int i = 0; i < 8; i++) {
            if (this.geneCount[i] > 0)
                continue;
            do {
                gene = rnd.nextInt(8);
            } while (this.geneCount[gene] <= 1);
            this.geneCount[i]++;
            this.geneCount[gene]--;
            for (int j = 0; j < 32; j++) {
                if (!this.genes[j].equals(gene))
                    continue;
                this.genes[j] = i;
                break;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Genotype))
            return false;
        Genotype gen = (Genotype) obj;
        Integer[] geneCount = gen.getGeneCountArray();
        for (int i = 0; i < 8; i++){
            if (this.geneCount[i] != geneCount[i])
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int sum = 0;
        int positionValue = 1;
        for (int i = 0; i < 8; i++){
            sum += geneCount[i]*positionValue;
            positionValue *= 8;
        }
        return sum;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o)
            return 0;
        return (new Integer(this.hashCode()).compareTo(new Integer(((Genotype)o).hashCode())));
    }
}

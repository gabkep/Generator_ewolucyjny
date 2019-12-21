package projekt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {

    @Test
    void createGenotype()
    {
        Integer[] geneArray1 = new Integer[32];
        for (int i = 0; i < 32; i++)
            geneArray1[i] = 0;
        Genotype genotype1 = new Genotype(geneArray1);
        Integer[] geneCount1 = genotype1.getGeneCountArray();
        Integer[] genes1 = genotype1.getGeneArray();
        int sum = 0;
        for (Integer count : geneCount1) {
            assertNotEquals(0, count);
            sum += count;
        }
        assertEquals(32, sum);
        for (Integer gene : genes1) {
            geneCount1[gene]--;
        }
        for (Integer count : geneCount1)
            assertEquals(0, count);
        int current = 0;
        for (Integer gene : genes1) {
            assertFalse(current > gene);
            if (current < gene)
                current = gene;
        }
    }

    @Test
    void createRandomGenotype()
    {
        for (int i = 0; i < 10000000; i++) {
            Genotype genotype = new Genotype();
            Integer[] geneCount = genotype.getGeneCountArray();
            Integer[] genes = genotype.getGeneArray();
            int sum = 0;
            for (Integer count : geneCount) {
                assertNotEquals(0, count);
                sum += count;
            }
            assertEquals(32, sum);
            for (Integer gene : genes) {
                geneCount[gene]--;
            }
            for (Integer count : geneCount)
                assertEquals(0, count);
            int current = 0;
            for (Integer gene : genes) {
                assertFalse(current > gene);
                if (current < gene)
                    current = gene;
            }
        }
    }
}
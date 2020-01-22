/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic;

/**
 *
 * @author cn1adil
 */
public class Population {
    private Chromosome[] pop;
    int size;
    int qSize;
    
    public Population(int n, int q)
    {
        size = n;
        qSize = q;
        pop = new Chromosome[n];
        for (int i = 0; i < n; i++) {
            pop[i] = new Chromosome(q);
        }
    }
    
    public void setPopulation(Chromosome[] chrome)
    {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < qSize; j++) {
                pop[i].setGene(j, chrome[i].getGene(j) );
            }
        }
    }
    
    public Chromosome[] getPopulation()
    {
        return pop;
    }
}

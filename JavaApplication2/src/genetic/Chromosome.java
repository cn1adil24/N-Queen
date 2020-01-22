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
public class Chromosome {
    private int[] gene;
    public Chromosome(int n)
    {
        gene = new int[n];
    }
    
    public void setGene(int i, int n)
    {
        gene[i] = n;
    }
    public int getGene(int i)
    {
        return gene[i];
    }
}

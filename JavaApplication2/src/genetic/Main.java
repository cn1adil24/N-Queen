/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genetic;
import java.util.Random; 
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author cn1adil
 */
public class Main extends JPanel{
    static private final int N = 8;
    static private final int n = 8;
    static private final int popSize = 4;
    static private final int select = 3;
    
    static JButton[][] b = new JButton[n][n];
    static ImageIcon queen = new ImageIcon("queen.png");
    
    @Override
    public void paint(Graphics g){
        int k = 0;
        int l = 0;
        int x = 75;
        int y = 75;
        boolean flag=true;
        boolean nextFlag=true;
        for (int i = 0; i < n; i++) {
            x=75;
            for (int j = 0; j < n; j++) {
                if(flag) {
                    b[i][j].setBounds(x,y,75,75);
                    flag=false;
                }else{
                    b[i][j].setBounds(x,y,75,75);
                    b[i][j].setBackground(Color.BLACK);
                    flag=true;
                }
                x += 75;
            }
            y += 75;
            if(nextFlag) {
                nextFlag = false;
                flag = false;
            } else {
                nextFlag = true;
                flag = true;
            }
        }
    }
    
    public static void addConstraint(int[][] board, int row, int col)
    {                
        // Downward
        for (int i = row+1; i < N; i++) {
            board[i][col]++;
        }
        // Upward
        for (int i = row-1; i >= 0; i--) {
            board[i][col]++;
        }
        // Right
        for (int j = col+1; j < N; j++) {
            board[row][j]++;
        }
        
        // For bottom right diagonal
        for (int i = row+1, j = col+1; i < N && j < N; i++, j++) {
            board[i][j]++;
        }
        
        // For top right diagonal
        for (int i = row-1, j = col+1; i >= 0 && j < N; i--, j++) {
            board[i][j]++;
        }
    }
    public static Chromosome[] fitnessFunction(Population pop, ValuePair min_pair)
    {
        Chromosome[] oldFit = pop.getPopulation();
        Chromosome[] bestFit = new Chromosome[select];
        int[] attackingCount = new int[popSize];
        int maxConflict = 0;
        int minChromosome = 0;
        int minConflict = 5040; // (n-1)! possible conflicts
        int maxChromosome = -1;
        
        for (int i = 0; i < popSize; i++) {
            int[][] board = new int[N][N];
            // Span grids with queens' attacking space
            for (int j = 0; j < N; j++) {
                int row = oldFit[i].getGene(j);
                int col = j;
                addConstraint(board, row, col);
            }
            // Check if queen has a conflicting pair
            for (int j = 0; j < N; j++) {
                // Extract co-ordinates of placed queen
                int x = oldFit[i].getGene(j);
                int y = j;
                // Placed queen is conflicting
                if (board[x][y] != 0) {
                    attackingCount[i] += board[x][y];
                }
            }
            if (attackingCount[i] > maxConflict) {
                maxConflict = attackingCount[i];
                maxChromosome = i;
            }
            if (attackingCount[i] < minConflict) {
                minConflict = attackingCount[i];
                minChromosome = i;
            }
        }
        for (int i = 0, j = 0; i < popSize && j < select; i++) {
            if (i == maxChromosome) continue;
            else {
                bestFit[j] = oldFit[i];
                j++;
            }
        }
        min_pair.setIndex(minChromosome);
        min_pair.setValue(minConflict);
        return bestFit;
    }
    public static Chromosome[] Crossover(Chromosome[] parent)
    {
        Chromosome[] child = new Chromosome[popSize];
        for (int i = 0; i < popSize; i++) {
            child[i] = new Chromosome(N);
        }        
        // Using uniform crossover
        // heads(1) means copy element by swapping the parents' elements
        // tails(0) means copy elements of parent without any changes
        int coin;
        Random rand = new Random();
        // Crossover on parent #1 and #2
        for (int i = 0; i < N; i++) {
            coin = rand.nextInt(2);
            // Swap the elements
            if (coin == 1) {
                child[0].setGene(i, parent[1].getGene(i));
                child[1].setGene(i, parent[0].getGene(i));
            }
            // Copy as it is
            else {
                child[0].setGene(i, parent[0].getGene(i));
                child[1].setGene(i, parent[1].getGene(i));
            }
        }
        // Crossover on parent #1 and #3
        for (int i = 0; i < N; i++) {
            coin = rand.nextInt(2);
            // Swap the elements
            if (coin == 1) {
                child[2].setGene(i, parent[2].getGene(i));
                child[3].setGene(i, parent[0].getGene(i));
            }
            // Copy as it is
            else {
                child[2].setGene(i, parent[0].getGene(i));
                child[3].setGene(i, parent[2].getGene(i));
            }
        }
        return child;
    }
    
    public static Chromosome[] Mutation(Chromosome[] child)
    {        
        for (int i = 0; i < popSize; i++) {
            Random rand = new Random();
            int n = rand.nextInt(N);
            int target = rand.nextInt(N);
            child[i].setGene(target, n);
            int[] dups = new int[N];
            for (int j = 0; j < N; j++) {
                if ( dups[child[i].getGene(j)] != 0 ) {
                    child[i].setGene(j, -1);
                } else {
                    dups[child[i].getGene(j)]++;
                }
            }
            for (int j = 0; j < N; j++) {
                if( child[i].getGene(j) == -1 ) {
                    for (int k = 0; k < N; k++) {
                        if ( dups[k] == 0 ) {
                            child[i].setGene(j, k);
                            dups[k]++;
                            break;
                        }
                    }
                }
            }
        }
        return child;
    }
    
    public static void main(String[] args) {
        // GUI initialization
        JFrame frame = new JFrame(); 
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = new JButton();
                frame.add(b[i][j]);
            }
        }
        frame.setSize(750,800);
        frame.getContentPane().add(new Main());
        frame.setLocationRelativeTo(null);
        frame.setBackground(Color.LIGHT_GRAY);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        final int maxIterations = 1000;
        int prev_min = -1;
        Population pop = new Population(popSize, N);
        Random rand = new Random();
        Chromosome[] chrome = new Chromosome[popSize];

        for (int i = 0; i < popSize; i++) {
            chrome[i] = new Chromosome(N);
            for (int j = 0; j < N; j++) {
                int n = rand.nextInt(N);
                chrome[i].setGene(j, n);
            }
        }
        pop.setPopulation(chrome);
        Chromosome[] selectedChrome;
        ValuePair min_pair = new ValuePair();
        min_pair.setValue(Integer.MAX_VALUE);
        min_pair.setIndex(Integer.MAX_VALUE);
        int k = 0;
        int i = 0;
        while(min_pair.getValue() > 0 && i < maxIterations)
        {
            selectedChrome = fitnessFunction(pop, min_pair);
            if(min_pair.getValue() == 0) break;
            chrome = Crossover(selectedChrome);
            chrome = Mutation(chrome);
            pop.setPopulation(chrome);
            if (min_pair.getValue() == prev_min) {
                i++;
            } else{
                i = 0;
            }
            prev_min = min_pair.getValue();
            k++;
        }
        for (int j = 0; j < N; j++) {
            
            System.out.println("Queen# " + j + " : (" + chrome[min_pair.getIndex()].getGene(j) + ", " + j + ")");
            b[chrome[min_pair.getIndex()].getGene(j)][j].setIcon(queen);
        }
        System.out.println("Attacking pairs: " + min_pair.getValue());
        System.out.println("No. of iterations: " + k);
    }
}

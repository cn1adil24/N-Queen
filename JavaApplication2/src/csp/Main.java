/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csp;

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
    static private final int n = 8;
    static final int delay = 100;
    
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
    
    public static void addConstraints(Queen q, int row, int col)
    {
        // For its own block
        q.addGrid(row, col);
        
        // Downward
        for (int i = row+1; i < n; i++) {
            q.addGrid(i, col);
        }
        // Upward
        for (int i = row-1; i >= 0; i--) {
            q.addGrid(i, col);
        }
        // Right
        for (int j = col+1; j < n; j++) {
            q.addGrid(row, j);
        }
        // Left
        for (int j = col-1; j >= 0; j--) {
            q.addGrid(row, j);
        }
        
        
        // For bottom right diagonal
        for (int i = row+1, j = col+1; i < n && j < n; i++, j++) {
            q.addGrid(i, j);
        }
        
        // For top right diagonal
        for (int i = row-1, j = col+1; i >= 0 && j < n; i--, j++) {
            q.addGrid(i, j);
        }
        
        // For bottom left diagonal
        for (int i = row+1, j = col-1; i < n && j >= 0; i++, j--) {
            q.addGrid(i, j);
        }
        
        // For top left diagonal
        for (int i = row-1, j = col-1; i >= 0 && j >= 0; i--, j--) {
            q.addGrid(i, j);
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
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
        
        // total no. of queens placed
        int placedQueens = 0;
        // helps in backtracking position of previous queen's row
        int prev_row;
        Queen[] queens = new Queen[n];
        for (int i = 0; i < n; i++) {
            queens[i] = new Queen();
        }
        
        // Starting from random row
        Random rand = new Random();
        int row = rand.nextInt(n);
        int col = 0;
        queens[0].setX(row);
        queens[0].setY(col);
        addConstraints(queens[0], row, col);
        placedQueens++;
        b[row][col].setIcon(queen);
        int count = 0;

        // for placing queen in column
        for (int j = 1; j < n; j++) {
            
            // check if queen is already placed or not
            if (queens[placedQueens].getX() != -1 && queens[placedQueens].getY() != -1) {
                int x = queens[placedQueens].getX();
                int y = queens[placedQueens].getY();
                b[x][y].setIcon(null);
                Thread.sleep(delay);
                queens[placedQueens].removeGrids();
                if (placedQueens == 0)
                    prev_row = (queens[placedQueens].getX() + 1) % n;
                else
                    prev_row = queens[placedQueens].getX() + 1;
                queens[placedQueens].setX(-1);
                queens[placedQueens].setY(-1);
            }
            else prev_row = 0;
            // if current queen is placed or not
            boolean queenPlaced = false;
            // for placing queen in row
            for (int i = prev_row; i < n; i++) {
                b[i][j].setIcon(queen);
                Thread.sleep(delay);
                boolean isBlocked = false;
                // check if any placed queen has put constraint
                for (int k = 0; k < placedQueens; k++) {
                    if ( queens[k].isGridBlocked(i, j) ) {
                        isBlocked = true;
                        break;
                    }
                }
                // grid is not constrained by any placed queen
                if (!isBlocked) {
                    queens[placedQueens].setX(i);
                    queens[placedQueens].setY(j);
                    addConstraints(queens[placedQueens], i, j);
                    placedQueens++;
                    queenPlaced = true;
                    break;
                }
                else {
                    b[i][j].setIcon(null);
                    Thread.sleep(delay);
                }
            }
            // no block available in column, need to backtrack
            if (!queenPlaced) {
                j = j-2;
                placedQueens--;
            }
            count++;
        }
        System.out.println("No. of iterations: " + count);
    }
}

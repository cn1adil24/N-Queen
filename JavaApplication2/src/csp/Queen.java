/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csp;
import java.util.LinkedList;

/**
 *
 * @author cn1adil
 */
public class Queen {
    private int x;
    private int y;
    private LinkedList<BlockedGrid> constraints;

    public Queen()
    {
        x = -1;
        y = -1;
        constraints = new LinkedList<BlockedGrid>();
    }
    
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void addGrid(int x, int y)
    {
        constraints.add(new BlockedGrid(x, y));
    }
    
    public void removeGrids()
    {
        constraints.clear();
    }
    
    public boolean isGridBlocked(int x, int y) {
        boolean flag = false;
        for (BlockedGrid constraint : constraints) {
            if(x == constraint.getX() && y == constraint.getY()) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    
    public void printGrids()
    {
        for (BlockedGrid constraint : constraints) {
            System.out.println("(" + constraint.getX() + ", " + constraint.getY() + ")");
        }
    }
}

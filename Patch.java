import java.util.Comparator;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
/**
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 * 
 * @author Nikola Sotirov
 * @author Cihan Guleryuz
 * assignment group 193
 * 
 * assignment copyright Kees Huizing
 */

class Patch {
    private boolean isC;
    private double score;

    //A dynamic array of patches (the neighbors)
    private Vector<Patch> neighbors = new Vector<>();

    private int x;
    private int y;
    private int width;
    private Color patchColor;

    //The constructor of the class
    public Patch(int x, int y, int width){
        this.x = x;
        this.y = y;
        this.width = width;
        this.isC = PlayingField.random.nextBoolean();
        this.setColor();
        score = 0;
    }

    private void setColor(){
        if (isC){
            this.patchColor = Color.BLUE;
        }
        else{
            this.patchColor = Color.RED;
        }
    }
    
    void draw(Graphics g){
        g.setColor(this.patchColor);
        g.fillRect(x, y, width, width);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, width);
        this.setColor();
    }

    // returns true if and only if patch is cooperating
    boolean isCooperating() {
        return isC;
    }
    
    Vector<Patch> getNeighbors(){
        return this.neighbors;
    }

    // set strategy to C if isC is true and to D if false
    void setCooperating(boolean isC) {
        this.isC = isC;
    }
    
    // change strategy from C to D and vice versa
    void toggleStrategy() {
        this.isC = !isC;

        if (isC){
            this.patchColor = new Color(153, 204, 255);
        }
        else{
            this.patchColor = Color.ORANGE;
        }
    }

    void execute(){
        double maxScore = this.neighbors.stream().max(new Comparator<Patch>(){
			@Override
			public int compare(Patch o1, Patch o2) {
                return Double.compare(o1.getScore(), o2.getScore());
			} 
        }).get().getScore();

        var maxNeighbors = this.neighbors.stream().filter((p) -> {
            return p.getScore() == maxScore;
        }).collect(Collectors.toList());

        Patch randomMaxPatch = maxNeighbors.get(PlayingField.random.nextInt(maxNeighbors.size()));

        if (randomMaxPatch.isC != this.isC){
            this.toggleStrategy();
        }
    }
    
    void calculateScore(double alpha){
        long cooperatorsCount = this.neighbors.stream().filter((p) -> {
            return p.isC;
        }).count();

        if (this.isC){
            this.score += cooperatorsCount; 
        }
        else{
            this.score += alpha * cooperatorsCount;
        }
    }

    // return score of this patch in current round
    double getScore() {
        return score;
    }
}


/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 * 
 * @author Nikola Sotirov
 * @author Cihan Guleryuz
 * assignment group 193
 * 
 * assignment copyright Kees Huizing
 */

import java.util.Random;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

class PlayingField extends JPanel implements ActionListener, ChangeListener {

    private Patch[][] grid;
    public static int GRID_DIMENSION_SIZE = 50;
    private int patchWidth = 8;
    private int fieldWidth = GRID_DIMENSION_SIZE * patchWidth;
    private int fieldHeight = GRID_DIMENSION_SIZE * patchWidth;

    private double alpha = 1.5; // defection award factor

    private Timer timer;
    private int timerStep = 250;
    private boolean isTimerRunning = false;

    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    public static final Random random = new Random(SEED);

    public PlayingField() {
        // call the initialization function
        setPreferredSize(new Dimension(fieldWidth, fieldHeight));
        initializeGrid();
        initializeTimer(this.timerStep);

        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                grid[e.getX()/patchWidth][e.getY()/patchWidth].toggleStrategy();
                repaint();
            }
        });
    }

    public void setCountScore(boolean countOwnScore){
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
                grid[i][j].setCountOwnScore(countOwnScore);
            }
        }
    }

    private void initializeTimer(int timerStep){
        this.timer = new Timer(timerStep, (e) -> {
            step();
            this.repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < grid.length; i++) {
            for (Patch item : grid[i]) {
                item.draw(g);
            }
        }
    }

    // the grid initialization function
    private void initializeGrid() {
        grid = new Patch[GRID_DIMENSION_SIZE][GRID_DIMENSION_SIZE];

        for (int i = 0; i < grid.length; i++) {
            // initialize grid with patches with random values
            // at their positions
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Patch(i * patchWidth, j * patchWidth, patchWidth);
            }
        }

        for (int i = 0; i < grid.length; i++) {
            int indexTop = (i - 1);
            int indexBottom = (i + 1) % GRID_DIMENSION_SIZE;

            if (indexTop < 0) {
                indexTop = Math.floorMod(indexTop, GRID_DIMENSION_SIZE);
            }

            for (int j = 0; j < grid[i].length; j++) {
                int indexRight = (j + 1) % GRID_DIMENSION_SIZE;
                int indexLeft = (j - 1);

                if (indexLeft < 0) {
                    indexLeft = Math.floorMod(indexLeft, GRID_DIMENSION_SIZE);
                }

                grid[i][j].getNeighbors().add(grid[indexBottom][j]);
                grid[i][j].getNeighbors().add(grid[indexTop][j]);

                grid[i][j].getNeighbors().add(grid[i][indexRight]);
                grid[i][j].getNeighbors().add(grid[i][indexLeft]);

                grid[i][j].getNeighbors().add(grid[indexBottom][indexRight]);
                grid[i][j].getNeighbors().add(grid[indexTop][indexLeft]);

                grid[i][j].getNeighbors().add(grid[indexBottom][indexLeft]);
                grid[i][j].getNeighbors().add(grid[indexTop][indexRight]);
            }
        }
    }

    /**
     * calculate and execute one step in the simulation
     */

    public void step() {
        for (int i = 0; i < grid.length; i++) {
            for (Patch item : grid[i]) {
                item.calculateScore(this.alpha);
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (Patch item : grid[i]) {
                item.execute();
            }
        }
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getAlpha() {
        return alpha;
    }

    public int getTimerStep(){
        return this.timerStep;
    }

    public Patch[][] getPatchGrid() {
        return this.grid;
    }

    // return grid as 2D array of booleans
    // true for cooperators, false for defectors
    // precondition: grid is rectangular, has non-zero size and elements are
    // non-null
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[grid.length][grid[0].length];

        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                resultGrid[x][y] = grid[x][y].isCooperating();
            }
        }

        return resultGrid;
    }

    // sets grid according to parameter inGrid
    // a patch should become cooperating if the corresponding
    // item in inGrid is true
    public void setGrid(boolean[][] inGrid) {
        for (int i = 0; i < inGrid.length; i++) {
            for (int j = 0; j < inGrid[i].length; i++) {
                grid[i][j].setCooperating(inGrid[i][j]);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            JButton b = (JButton) e.getSource();
            b.setText("Pause");
            b.setActionCommand("Stop");
            this.timer.start();
            isTimerRunning = true;
        } else if (e.getActionCommand().equals("Stop")) {
            this.timer.stop();
            isTimerRunning = false;
            JButton button = (JButton) e.getSource();
            button.setText("Go");
            button.setActionCommand("Start");
        } else if (e.getActionCommand().equals("Reset")){
            this.initializeGrid();
            this.repaint();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider)e.getSource();

        if (slider.getName().equals("alphaSlider")){
           this.alpha = (double)slider.getValue()/100; //The slider values can range from 0 to 300, and therefore the '/100'.
        }
        else if (slider.getName().equals("paceSlider")){
            this.timerStep = PrisonersDilemma.maximumTime/slider.getValue();
            this.timer.stop();
            this.timer.setDelay(timerStep);

            if (isTimerRunning){
                this.timer.start();
            }
        }
    }
}


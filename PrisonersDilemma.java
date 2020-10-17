/**
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * main class
 * 
 * @author Nikola Sotirov
 * @author Cihan Guleryuz
 * assignment group 193
 * 
 * assignment copyright Kees Huizing
 */

import java.awt.*;
import javax.swing.*;

class PrisonersDilemma {
    JFrame frame;
    JButton triggerButton;
    JButton resetButton;
    JSlider alphaSlider;
    JSlider paceSlider;
    PlayingField field = new PlayingField();
    JPanel slidersPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    int panelSizeX = 80;
    int panelSizeY = 80;
    static int maximumTime = 1000; //this variable is static and public. It represents the timer's maximum time.
    int alphaMax = 300;
    
    void buildGUI() {
        SwingUtilities.invokeLater( () -> {
            //initialize the frame
            frame = new JFrame("Prisoner's dilema simulation");
            frame.setSize(new Dimension(panelSizeX * 9, panelSizeY * 9));
            
            //craete the "GO" button and add it to the west side of the buttons panel
            triggerButton = new JButton("Go");
            triggerButton.setActionCommand("Start");
            triggerButton.addActionListener(field);
            buttonsPanel.add(triggerButton, BorderLayout.WEST);

            //create the "Reset" button and add it to the east side of the buttons panel
            resetButton = new JButton("Reset");
            resetButton.addActionListener(field);
            resetButton.setActionCommand("Reset");
            buttonsPanel.add(resetButton, BorderLayout.EAST);

            //create the defection coeficient slider and add it to the south side of the sliders panel
            alphaSlider = new JSlider(JSlider.HORIZONTAL, 0, alphaMax, alphaMax/2);
            alphaSlider.setName("alphaSlider");
            alphaSlider.addChangeListener(field);
            var alphaSliderLabel = new JLabel(String.format("Defection factor: %.2f", field.getAlpha()), JLabel.LEFT);
            alphaSlider.addChangeListener(l -> {
                alphaSliderLabel.setText(String.format("Defection factor: %.2f", field.getAlpha()));
            });
            slidersPanel.add(alphaSliderLabel);
            slidersPanel.add(alphaSlider, BorderLayout.NORTH);


            //create and add the simulation pace slider to the southern part of the sliders panel
            paceSlider = new JSlider(JSlider.HORIZONTAL, 1, maximumTime, maximumTime/4);
            paceSlider.setName("paceSlider");
            paceSlider.addChangeListener(field);
            var paceSliderLabel = new JLabel(String.format("Frequency: %.2f", (double)maximumTime/paceSlider.getValue()), JLabel.LEFT);
            paceSlider.addChangeListener(l -> {
                paceSliderLabel.setText(String.format("Frequency: %.2f", (double)maximumTime/paceSlider.getValue()));
            });
            slidersPanel.add(paceSliderLabel);
            slidersPanel.add(paceSlider, BorderLayout.CENTER);
            
            //checkbox for whether a player's score should be counted in its own calculation
            var patchInvolvementCheckBox = new JCheckBox("Patches involved in neighbor calculations: ", true);
            patchInvolvementCheckBox.addChangeListener(l -> {
                field.setCountScore(patchInvolvementCheckBox.isSelected());
            });
            slidersPanel.add(patchInvolvementCheckBox, BorderLayout.SOUTH);

            //set the sliders panel's size to be fixed
            slidersPanel.setPreferredSize(new Dimension(panelSizeX, panelSizeY));

            //add all the panels to the frame and pack it
            frame.add(buttonsPanel, BorderLayout.NORTH);
            frame.add(field, BorderLayout.CENTER);
            frame.add(slidersPanel, BorderLayout.PAGE_END);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } );
    }
    
    public static void main( String[] a ) {
        (new PrisonersDilemma()).buildGUI();
    }
}

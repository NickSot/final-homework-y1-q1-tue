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

    void buildGUI() {
        SwingUtilities.invokeLater( () -> {
            frame = new JFrame("Prisoner's dilema simulation");
            triggerButton = new JButton("Go");
            triggerButton.setActionCommand("Start");
            triggerButton.addActionListener(field);
            buttonsPanel.add(triggerButton, BorderLayout.WEST);

            resetButton = new JButton("Reset");
            resetButton.addActionListener(field);
            resetButton.setActionCommand("Reset");
            buttonsPanel.add(resetButton, BorderLayout.EAST);

            alphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, 150);
            alphaSlider.setName("alphaSlider");
            alphaSlider.addChangeListener(field);
            var alphaSliderLabel = new JLabel(String.format("defection factor: %.2f", field.getAlpha()), JLabel.LEFT);
            alphaSlider.addChangeListener(l -> {
                alphaSliderLabel.setText(String.format("defection factor: %.2f", field.getAlpha()));
            });
            slidersPanel.add(alphaSliderLabel);
            slidersPanel.add(alphaSlider, BorderLayout.SOUTH);


            paceSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 250);
            paceSlider.setName("paceSlider");
            paceSlider.addChangeListener(field);
            var paceSliderLabel = new JLabel(String.format("Frequency: %.2f", (double)field.getTimerStep()), JLabel.LEFT);
            paceSlider.addChangeListener(l -> {
                paceSliderLabel.setText(String.format("Frequency: %.2f", (double)field.getTimerStep()));
            });
            slidersPanel.add(paceSliderLabel);
            slidersPanel.add(paceSlider, BorderLayout.SOUTH);
            
            slidersPanel.setPreferredSize(new Dimension(50, 50));

            frame.add(buttonsPanel, BorderLayout.NORTH);
            frame.add(field, BorderLayout.CENTER);
            frame.add(slidersPanel, BorderLayout.SOUTH);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } );
    }
    
    public static void main( String[] a ) {
        (new PrisonersDilemma()).buildGUI();
    }
}

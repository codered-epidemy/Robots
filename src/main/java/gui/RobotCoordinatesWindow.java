package gui;


import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


public class RobotCoordinatesWindow extends JInternalFrame implements Observer {
    private final JLabel xLabel;
    private final JLabel yLabel;
    private final GameState gameState;


    public RobotCoordinatesWindow(GameState gameState) {
        super("Координаты робота", true, true, true, true);
        this.gameState = gameState;
        this.gameState.addObserver(this);

        xLabel = new JLabel();
        xLabel.setLocation(0, 0);
        xLabel.setSize(100, 20);

        xLabel.setText("X: ");

        yLabel = new JLabel();
        yLabel.setLocation(0, 10);
        yLabel.setSize(100, 20);
        yLabel.setText("Y: ");

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(xLabel);
        panel.add(yLabel);
        getContentPane().add(panel);
        pack();
    }


    @Override
    public void update(Observable o, Object arg) {
        updateWindowContent();
    }

    public void updateWindowContent() {
        xLabel.setText("X: " + (int) gameState.getRobot().getRobotPositionX());
        yLabel.setText("Y: " + (int) gameState.getRobot().getRobotPositionY());
    }
}
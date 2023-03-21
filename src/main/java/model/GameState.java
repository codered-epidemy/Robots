package model;


import java.util.Observable;

public class GameState extends Observable {
    private final Robot robot;
    private RobotMovement robotCommand;

    public GameState(double robotX, double robotY, double robotDir) {
        robot = new Robot(robotX, robotY, robotDir);

    }

    public void updateGameState() {
        if (robotCommand != null) {
            setChanged();
            notifyObservers();
            robotCommand.onModelUpdateEvent(robot);
            clearChanged();
        }

    }

    public void setRobotCommand(RobotMovement robotCommand) {
        this.robotCommand = robotCommand;
    }

    public Robot getRobot() {
        return this.robot;
    }
}
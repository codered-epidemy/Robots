package presenter;

import gui.GameVisualizer;
import model.*;

import java.awt.event.MouseEvent;
import java.util.TimerTask;

public class GamePresenter {
    public GamePresenter(GameVisualizer visualizer){
        visualizer.addMouseEventListener(this::setNewTarget);
        visualizer.addTaskOnUpdatePanel(new TimerTask(){
            @Override
            public void run(){
                updateModel();
            }
        });
    }

    private final GameState gameState = new GameState(100, 100, 0);
    private Target target = new Target((int) gameState.getRobot().getRobotPositionX(), (int) gameState.getRobot().getRobotPositionY());

    public void updateModel(){
        gameState.updateGameState();
    }

    public void setNewTarget(MouseEvent event){
        target = new Target(event.getX(), event.getY());
        gameState.setRobotCommand(new RobotMovement(target));
    }

    public GameState getGameState() {
        return gameState;
    }

    public Robot getRobotInfo(){
        return gameState.getRobot();
    }

    public Target getTarget(){
        return target;
    }
}

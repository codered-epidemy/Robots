package gui;

import model.Robot;
import model.Target;
import presenter.GamePresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();
    private final GamePresenter presenter;

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public GameVisualizer() {
        presenter = new GamePresenter(this);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        setDoubleBuffered(true);
    }

    public GamePresenter getPresenter() {
        return presenter;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    public void addTaskOnUpdatePanel(TimerTask task) {
        m_timer.schedule(task, 0, 10);
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    public void addMouseEventListener(Consumer<MouseEvent> listener) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.accept(e);
                repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Robot robot = presenter.getRobotInfo();
        Target target = presenter.getTarget();
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, round(robot.getRobotPositionX()), round(robot.getRobotPositionY()), robot.getRobotDirection());
        drawTarget(g2d, target.getTargetPositionX(), target.getTargetPositionY());
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        int robotCenterX = round(x);
        int robotCenterY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
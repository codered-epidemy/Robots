package model;

public class RobotMovement {
    public final Target target;

    private static final double maxAngularVelocity = 0.001;
    private static final double maxVelocity = 0.1;

    public RobotMovement(Target target) {
        this.target = target;
    }

    public Target getTarget() {
        return this.target;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent(Robot robot) {
        double distance = distance(target.getTargetPositionX(), target.getTargetPositionY(),
                robot.getRobotPositionX(), robot.getRobotPositionY());
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(robot.getRobotPositionX(), robot.getRobotPositionY(), target.getTargetPositionX(), target.getTargetPositionY());
        double angularVelocity = 0;
        if (angleToTarget > robot.getRobotDirection()) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robot.getRobotDirection()) {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(robot, velocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    private void moveRobot(Robot robot, double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robot.getRobotPositionX() + velocity / angularVelocity *
                (Math.sin(robot.getRobotDirection() + angularVelocity * duration) -
                        Math.sin(robot.getRobotDirection()));
        if (!Double.isFinite(newX)) {
            newX = robot.getRobotPositionX() + velocity * duration * Math.cos(robot.getRobotDirection());
        }
        double newY = robot.getRobotPositionY() - velocity / angularVelocity *
                (Math.cos(robot.getRobotDirection() + angularVelocity * duration) -
                        Math.cos(robot.getRobotDirection()));
        if (!Double.isFinite(newY)) {
            newY = robot.getRobotPositionY() + velocity * duration * Math.sin(robot.getRobotDirection());
        }
        robot.setRobotPositionX(newX);
        robot.setRobotPositionY(newY);
        double newDirection = asNormalizedRadians(robot.getRobotDirection() + angularVelocity * duration);
        robot.setRobotDirection(newDirection);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
package wumpusworld;

/**
 * Contains starting code for creating your own Wumpus World agent. Currently
 * the agent only make a random decision each turn.
 *
 * @author Johan Hagelbäck
 */
public class MyAgent implements Agent {

    int rnd;
    private World w;
    protected QLearningAgent agent;
    private Position currentPosition;

    /**
     * Creates a new instance of your solver agent.
     *
     * @param world Current world state
     */
    public MyAgent(World world) {
        w = world;
        agent = new QLearningAgent();
        currentPosition = new Position(w.getPlayerX(), w.getPlayerY());
    }

    @Override
    public void train() {
        Position tempPosition = currentPosition;
        String move = this.agent.getRandomMove();
        execute(move);
        this.agent.updateQTable(tempPosition, move);
    }

    /**
     * Asks your solver agent to execute an action.
     */
    @Override
    public void doAction() {
        String move = agent.getBestMove(currentPosition);
        execute(move);

        //Location of the player
        int cX = w.getPlayerX();
        int cY = w.getPlayerY();

        if (w.hasWumpus(cX, cY)) {
            System.out.println("Wampus is here");
            w.doAction(World.A_SHOOT);
        } else if (w.hasGlitter(cX, cY)) {
            System.out.println("Agent won");
            w.doAction(World.A_GRAB);
        } else if (w.hasPit(cX, cY)) {
            System.out.println("Fell in the pit");
        }

//        //Basic action:
//        //Grab Gold if we can.
//        if (w.hasGlitter(cX, cY)) {
//            w.doAction(World.A_GRAB);
//            return;
//        }
//
//        //Basic action:
//        //We are in a pit. Climb up.
//        if (w.isInPit()) {
//            w.doAction(World.A_CLIMB);
//            return;
//        }
//
//        //Test the environment
//        if (w.hasBreeze(cX, cY)) {
//            System.out.println("I am in a Breeze");
//        }
//        if (w.hasStench(cX, cY)) {
//            System.out.println("I am in a Stench");
//        }
//        if (w.hasPit(cX, cY)) {
//            System.out.println("I am in a Pit");
//        }
//        if (w.getDirection() == World.DIR_RIGHT) {
//            System.out.println("I am facing Right");
//        }
//        if (w.getDirection() == World.DIR_LEFT) {
//            System.out.println("I am facing Left");
//        }
//        if (w.getDirection() == World.DIR_UP) {
//            System.out.println("I am facing Up");
//        }
//        if (w.getDirection() == World.DIR_DOWN) {
//            System.out.println("I am facing Down");
//        }
//
//        //decide next move
//        rnd = decideRandomMove();
//        if (rnd == 0) {
//            w.doAction(World.A_TURN_LEFT);
//            w.doAction(World.A_MOVE);
//        }
//
//        if (rnd == 1) {
//            w.doAction(World.A_MOVE);
//        }
//
//        if (rnd == 2) {
//            w.doAction(World.A_TURN_LEFT);
//            w.doAction(World.A_TURN_LEFT);
//            w.doAction(World.A_MOVE);
//        }
//
//        if (rnd == 3) {
//            w.doAction(World.A_TURN_RIGHT);
//            w.doAction(World.A_MOVE);
//        }
    }

    /**
     * Generates a random instruction for the Agent.
     */
    public int decideRandomMove() {
        return (int) (Math.random() * 4);
    }

    private void execute(String move) {

        Position nextPosition = agent.getNextPosition(move, currentPosition);
        
        //evaluate position
        agent.evaluate(w, currentPosition);

        if (w.isValidPosition(nextPosition.getX(), nextPosition.getY())) {
            switch (move) {
                case "UP":
                    moveUp();
                    break;
                case "DOWN":
                    moveDown();
                    break;
                case "LEFT":
                    moveLeft();
                    break;
                default:
                    moveRight();
                    break;
            }

            currentPosition = nextPosition;
        } 
//        else {
//            System.out.println("Invalid move :(");
//        }
    }

    private void moveUp() {
        switch (w.getDirection()) {
            case World.DIR_LEFT:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_RIGHT:
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_DOWN:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            default:
                w.doAction(World.A_MOVE);
                break;
        }
    }

    private void moveDown() {
        switch (w.getDirection()) {
            case World.DIR_LEFT:
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_RIGHT:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_UP:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            default:
                w.doAction(World.A_MOVE);
                break;
        }
    }

    private void moveLeft() {
        switch (w.getDirection()) {
            case World.DIR_UP:
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_RIGHT:
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_DOWN:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            default:
                w.doAction(World.A_MOVE);
                break;
        }
    }

    private void moveRight() {
        switch (w.getDirection()) {
            case World.DIR_LEFT:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_UP:
                w.doAction(World.A_TURN_RIGHT);
                w.doAction(World.A_MOVE);
                break;
            case World.DIR_DOWN:
                w.doAction(World.A_TURN_LEFT);
                w.doAction(World.A_MOVE);
                break;
            default:
                w.doAction(World.A_MOVE);
                break;
        }
    }

    public void printQTable() {
        this.agent.print();
    }
}

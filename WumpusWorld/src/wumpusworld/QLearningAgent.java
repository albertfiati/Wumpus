/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusworld;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Optimistic
 */
public class QLearningAgent implements LearningAgent {

    /**
     * defining all the possible moves at a said position
     */
    public static final String[] MOVE = {"LEFT", "RIGHT", "UP", "DOWN"};

    /**
     * defining default maximum score of every action
     */
    public static final double MAXACTIONSCORE = -1;

    /**
     * defining training iteration
     */
    public static final int ITERATIONS = 100000;

    /*
    *   defining rewards for pit, gold, wumpus, shoot arrow
    **/
    public static final int PITREWARD = -1;
    public static final int WUMPUSREWARD = -1;
    public static final int GOLDREWARD = 1;
    public static final int SHOOTREWARD = -1;

    /**
     * defining the cost of performing an action, learning rate, dicount factor,
     * and probability of the agent making a bad move
     */
    public static final double COSTOFACTION = -0.04;
    public static final double LEARNINGRATE = 0.5;
    public static final double DISCOUNTFACTOR = 0.8;
    public static final double PROBABILITYOFBADMOVE = 0.2;

    /**
     * define and initialize a reward table for qlearning
     */
    public static double[][] qTable = new double[16][5];

    /**
     * get next safe position
     *
     * @param currentPosition
     *
     * @return String
     */
    public String getBestMove(Position currentPosition) {
        String move = null;
        double maxActionReward = MAXACTIONSCORE;

        // get the current row index of the qTable
        int currentQRowIndex = this.getQTableRowIndex(currentPosition);

        //loop through the various rewards of the the actions taken by the agent to select the best
        for (int i = 1; i < 5; i++) {
            if (qTable[currentQRowIndex][i] > maxActionReward) {
                move = MOVE[i - 1];
                maxActionReward = qTable[currentQRowIndex][i];
            }
        }

        return move;
    }

    /**
     * generate a random move for the agent
     *
     * @return String
     */
    public String getRandomMove() {
        Random rand = new Random();

        int moveIndex = rand.nextInt(4);
        String move = MOVE[moveIndex];

        return move;
    }

    /**
     * get the index of qTable related to a given state
     *
     * @param position
     *
     * @return int
     */
    private int getQTableRowIndex(Position position) {
        int index = -1;

        if (position.getX() < 5 && position.getY() < 5) {
            switch (position.getY()) {
                case 1:
                    index = position.getX() - position.getY();
                    break;
                case 2:
                    index = position.getX() + position.getY() + 1;
                    break;
                case 3:
                    index = position.getX() + position.getY() + 4;
                    break;
                case 4:
                    index = position.getX() + position.getY() + 7;
                    break;
                default:
                    index = -1;
                    break;
            }
        }

        return index;
    }

    /**
     * update the qTable with the reward of a particular action using the
     * Bellman equation
     *
     * @param currentPosition
     * @param move
     *
     */
    public void updateQTable(Position currentPosition, String move) {
        int currentQTableRowIndex = this.getQTableRowIndex(currentPosition);
        int nextQTableRowIndex = this.getQTableRowIndex(this.getNextPosition(move, currentPosition));

        int action = Arrays.asList(MOVE).indexOf(move) + 1;

        if (nextQTableRowIndex > -1) {
            qTable[currentQTableRowIndex][action] += LEARNINGRATE * (qTable[currentQTableRowIndex][0] + (DISCOUNTFACTOR * this.getMaxReward(qTable[nextQTableRowIndex])) - qTable[currentQTableRowIndex][action] + COSTOFACTION);
        } else {
            qTable[currentQTableRowIndex][action] += LEARNINGRATE * COSTOFACTION;
        }
    }

    /**
     * Get the maximum reward of all actions of a state
     *
     * @param qRow
     *
     * @return double
     *
     */
    private double getMaxReward(double[] qRow) {
        double max = MAXACTIONSCORE;

        for (int i = 1; i < 5; i++) {
            if (qRow[i] > max) {
                max = qRow[i];
            }
        }

        return max;
    }

    /**
     * Generate the next position based on a given move and current position
     *
     * @param move
     * @param currentPosition
     *
     * @return Position
     *
     */
    public Position getNextPosition(String move, Position currentPosition) {
        Position newPosition = new Position();

        switch (move) {
            case "UP":
                newPosition.setX(currentPosition.getX());
                newPosition.setY(currentPosition.getY() + 1);
                break;
            case "DOWN":
                newPosition.setX(currentPosition.getX());
                newPosition.setY(currentPosition.getY() - 1);
                break;
            case "LEFT":
                newPosition.setX(currentPosition.getX() - 1);
                newPosition.setY(currentPosition.getY());
                break;
            default:
                newPosition.setX(currentPosition.getX() + 1);
                newPosition.setY(currentPosition.getY());
                break;
        }

        return newPosition;
    }

    /**
     * evaluate the position and assign its value
     *
     * @param world
     * @param currentPosition
     *
     */
    public void evaluate(World world, Position currentPosition) {
        int x = world.getPlayerX();
        int y = world.getPlayerY();

        int currentQRowIndex = this.getQTableRowIndex(currentPosition);

        if (world.gameOver() && world.hasWumpus(x, y)) {
            qTable[currentQRowIndex][0] = WUMPUSREWARD;
        } else if (world.hasPit(x, y)) {
            qTable[currentQRowIndex][0] = PITREWARD;
            world.doAction(World.A_CLIMB);
        } else if (world.hasGlitter(x, y)) {
            qTable[currentQRowIndex][0] = GOLDREWARD;
            world.doAction(World.A_GRAB);
        }
    }

    /**
     * print the values of the qTable
     */
    public void print() {
        System.out.println(String.format("%s%15s%15s%15s%15s%15s", "ROW#", "VALUE", "LEFT", "RIGHT", "UP", "DOWN"));

        for (int i = 0; i < 16; i++) {
            System.out.println(String.format("%s%20f%17f%15f%15f%15f", i, qTable[i][0], qTable[i][1], qTable[i][2], qTable[i][3], qTable[i][4]));
        }
    }
}

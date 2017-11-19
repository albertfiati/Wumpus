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

    public static final String[] MOVE = {"LEFT", "RIGHT", "UP", "DOWN"};
    public static final double MAXACTIONSCORE = -1;
    public static final int ITERATIONS = 100000;

    public static final int PITREWARD = -1;
    public static final int WUMPUSREWARD = -1;
    public static final int GOLDREWARD = 1;
    public static final double COSTOFACTION = -0.04;
    public static final int SHOOTREWARD = -1;

    public static final double LEARNINGRATE = 0.5;
    public static final double DISCOUNTFACTOR = 0.8;

//    public static final double PROBABILITYOFBADMOVE = 0.2;

    /*
    *   
     */
    public static double[][] qTable = new double[16][5];

    //get next safe position
    public String getBestMove(Position currentPosition) {
        String move = null;
        double maxActionReward = MAXACTIONSCORE;
        int currentQRowIndex = this.getQTableRowIndex(currentPosition);

        for (int i = 1; i < 5; i++) {
            if (qTable[currentQRowIndex][i] > maxActionReward) {
                move = MOVE[i - 1];
                maxActionReward = qTable[currentQRowIndex][i];
            }
        }

        return move;
    }

    public String getRandomMove() {
        Random rand = new Random();

        int moveIndex = rand.nextInt(4);
        String move = MOVE[moveIndex];

        return move;
    }

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

    private double getMaxReward(double[] qRow) {
        double max = MAXACTIONSCORE;

        for (int i = 1; i < 5; i++) {
            if (qRow[i] > max) {
                max = qRow[i];
            }
        }

        return max;
    }

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

    public void evaluate(World w, Position currentPosition) {
        int x = w.getPlayerX();
        int y = w.getPlayerY();

        String what = "Nothing";

        int currentQRowIndex = this.getQTableRowIndex(currentPosition);

        if (w.gameOver() && w.hasWumpus(x, y)) {
            what = "Wumpus";
            qTable[currentQRowIndex][0] = WUMPUSREWARD;
        } else if (w.hasPit(x, y)) {
            what = "Pit";
            qTable[currentQRowIndex][0] = PITREWARD;
            w.doAction(World.A_CLIMB);
        } else if (w.hasGlitter(x, y)) {
            what = "Gold";
            qTable[currentQRowIndex][0] = GOLDREWARD;
            w.doAction(World.A_GRAB);
        }

        //System.out.println("What is here? Ans: " + what);
    }

    public void print() {
        System.out.println(String.format("%s%15s%15s%15s%15s%15s", "ROW#", "VALUE", "LEFT", "RIGHT", "UP", "DOWN"));

        for (int i = 0; i < 16; i++) {
            System.out.println(String.format("%s%20f%17f%15f%15f%15f", i, qTable[i][0], qTable[i][1], qTable[i][2], qTable[i][3], qTable[i][4]));
        }
    }
}

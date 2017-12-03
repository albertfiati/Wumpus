# Wumpus World
This project is part of the Applied Artifical Intelligence (DV2557) course at Blekinge Institute of Technology. The skeleton application is written by Johan Hagelbäck. We have implemented a learning agent that solves the world through Q Learning algorithm.

## Solution
The agent uses reinforcement learning; more specifically Q Learning. The implementation revolves around Q Table and Q Values. Q Values are state-action utility and state utility values associated with every state and every possible action at that state. The table that hold all these values is Q Table.

### Learning
The agent keeps on taking  random actions and checks the utility for each action taken. As per the core idea of Q Learning these utility values are propagated back to the previous state. When learning is finished, the agent will choose the action based on best value at every state.

## Implementation
Some implementation related points are as follows.

* There are possible 16 states and each state has a utility.
* Also for every of 4 actions at that particular state, each action has a specific utility. These actions are moving right, up, left and down respectively.
* All these utility values are the Q Values and  are stored in 2-d array of length [16][5] which is Q Table for this implementation. During the process of learning, Q Values and ultimately Q Table keeps on updating.
* The first dimension of the 2-d array represents the state where state 1,1 is at 0 index. Similarly state 2, 1 is at 1 index and so on.
* The second dimension holds the utilities. The 0 index hold state utility and consecutive indices hold utilitis for moving left, up, right and down respectively.

## Running the program
The program is tested through NetBeans.
* Build and Run the project.
* Click on "Train Agent" to perform the learning at the given map.
* Wait until the learning is complete. The completion of learnig will change the "Train Agent" to normal from highlighted.
* Start pressing “Run Solving Agent” button to follow step by step actions to the goal state for the given map.
* Rerun for any other map.

### Group Members
* Albert Fiati
* Abdullah Amjad
* Kushang Patel


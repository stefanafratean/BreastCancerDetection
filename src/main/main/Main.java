package main;

import learning.LearningStarter;
import userInterface.ConsoleInterface;

public class Main {
    public static void main(String[] args) {
        LearningStarter learningStarter = new LearningStarter();
        ConsoleInterface consoleUI = new ConsoleInterface(learningStarter);
        consoleUI.run();
    }
}

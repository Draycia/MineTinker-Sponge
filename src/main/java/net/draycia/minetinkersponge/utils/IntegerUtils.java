package net.draycia.minetinkersponge.utils;

public class IntegerUtils {

    public static int getModifierCostFromLevel(int desiredLevel) {
        if (desiredLevel < 0) {
            throw new IllegalArgumentException("Inputs must be positive!");
        }

        if (desiredLevel == 1) {
            return 1;
        }

        return (int) Math.pow(2, desiredLevel - 1);
    }

    public static int getRemainingModifierCost(int currentLevel, int desiredLevel) {
        int costOfCurrent = getModifierCostFromLevel(currentLevel);
        int costOfDesired = getModifierCostFromLevel(desiredLevel);

        return costOfDesired - costOfCurrent;
    }

}

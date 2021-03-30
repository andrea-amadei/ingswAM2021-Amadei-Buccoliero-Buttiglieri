package it.polimi.ingsw.model;

import it.polimi.ingsw.gamematerials.ResourceType;
import it.polimi.ingsw.gamematerials.ResourceTypeSingleton;
import it.polimi.ingsw.model.storage.Shelf;

import java.util.Arrays;
import java.util.List;

public final class GameParameters {
    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_USERNAME_LENGTH = 2;

    public static final int MAX_CARD_LEVEL = 3;
    public static final int MIN_CARD_LEVEL = 1;

    public static final List<String> BASE_CUPBOARD_SHELF_NAMES = Arrays.asList("BottomShelf", "MiddleShelf", "TopShelf");
    public static final List<ResourceType> BASE_CUPBOARD_SHELF_TYPES = Arrays.asList(
            ResourceTypeSingleton.getInstance().getAnyResource(),
            ResourceTypeSingleton.getInstance().getAnyResource(),
            ResourceTypeSingleton.getInstance().getAnyResource());
    public static final List<Integer> BASE_CUPBOARD_SHELF_SIZES = Arrays.asList(3, 2, 1);

    public static int MAX_FLAG_LEVEL = 3;

    public static final int UPGRADABLE_CRAFTING_NUMBER = 3;

    public static final int FAITH_CHECKPOINT_NUMBER = 3;

    private GameParameters() {}
}

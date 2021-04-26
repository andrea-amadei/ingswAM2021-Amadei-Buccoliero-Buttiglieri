package it.polimi.ingsw.model.fsm;

import it.polimi.ingsw.common.Message;
import it.polimi.ingsw.exceptions.FSMTransitionFailedException;
import it.polimi.ingsw.model.actions.*;

import java.util.List;

public interface ActionHandler {
    /**
     * It handles the specific action required. By default it throws an FSMTransitionFailedException.
     * If a specific state needs to handle the action, this method will be overrode.
     * If the action is handled, this method will set the nextState.
     * @param activateLeaderAction the action to be executed
     * @return the list of messages to be sent to the client
     * @throws NullPointerException if the action is null
     * @throws FSMTransitionFailedException if the action cannot be handled
     */
    default List<Message> handleAction(ActivateLeaderAction activateLeaderAction) throws FSMTransitionFailedException {
        if(activateLeaderAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(BuyFromMarketAction buyFromMarketAction) throws FSMTransitionFailedException {
        if(buyFromMarketAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(ConfirmTidyAction confirmTidyAction) throws FSMTransitionFailedException {
        if(confirmTidyAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectConversionsAction selectConversionsAction) throws FSMTransitionFailedException{
        if(selectConversionsAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(BackAction backAction) throws FSMTransitionFailedException{
        if(backAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectCardFromShopAction selectCardFromShopAction) throws FSMTransitionFailedException{
        if(selectCardFromShopAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectResourcesAction selectResourcesAction) throws FSMTransitionFailedException{
        if(selectResourcesAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }


    default List<Message> handleAction(ConfirmAction confirmAction) throws FSMTransitionFailedException{
        if(confirmAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectCraftingAction selectCraftingAction) throws FSMTransitionFailedException{
        if(selectCraftingAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectCraftingOutputAction selectCraftingOutputAction) throws FSMTransitionFailedException{
        if(selectCraftingOutputAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(MoveFromBasketToShelfAction moveFromBasketToShelfAction) throws FSMTransitionFailedException{
        if(moveFromBasketToShelfAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(ResourcesMoveAction resourcesMoveAction) throws FSMTransitionFailedException{
        if(resourcesMoveAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(SelectPlayAction selectPlayAction) throws FSMTransitionFailedException{
        if(selectPlayAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(DiscardLeaderAction discardLeaderAction) throws FSMTransitionFailedException{
        if(discardLeaderAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(EndGameAction endGameAction) throws FSMTransitionFailedException{
        if(endGameAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(LorenzoMoveAction lorenzoMoveAction) throws FSMTransitionFailedException{
        if(lorenzoMoveAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    default List<Message> handleAction(NextTurnAction nextTurnAction) throws FSMTransitionFailedException{
        if(nextTurnAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }
    default List<Message> handleAction(PreliminaryPickAction preliminaryPickAction) throws FSMTransitionFailedException{
        if(preliminaryPickAction == null)
            throw new NullPointerException();
        throw new FSMTransitionFailedException("Cannot execute this command now");
    }

    //TODO: add the other actions
}

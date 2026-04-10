package com.foodorder.command;

import org.springframework.stereotype.Component;
import java.util.Stack;

@Component
public class Manager {
    private final Stack<ICommand> undoStack = new Stack<>();
    private final Stack<ICommand> redoStack = new Stack<>();

    public void executeCommand(ICommand command){
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undoLastAction(){
        if (!undoStack.isEmpty()) {
            ICommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("No actions to undo.");
        }
    }

    public void redoLastAction(){
        if (!redoStack.isEmpty()) {
            ICommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        } else {
            System.out.println("No actions to redo.");
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
/**
 * The type ElevatorSystemGUIThread.
 * Thread that runs the GUI
 */
public class ElevatorSystemGUIThread implements Runnable {

    private final ElevatorSystemGUI gui;

    /**
     * Instantiates a new ElevatorSystemGUIThread.
     *
     * @param gui      the gui instance being used
     */
    public ElevatorSystemGUIThread(ElevatorSystemGUI gui) {
        this.gui = gui;
    }

    @Override
    public void run() {
        gui.createGUI();
    }
}

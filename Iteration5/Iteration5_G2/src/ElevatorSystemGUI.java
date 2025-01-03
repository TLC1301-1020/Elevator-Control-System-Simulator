import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * The type ElevatorSystemGUI.
 * Creates and updates the GUI accordingly
 */
public class ElevatorSystemGUI {
    private final ArrayList<JPanel> elevatorPanels;
    private int numberOfElevators;
    private final int DOORS_STATUS_INDEX = 1;
    private final int CURRENT_FLOOR_INDEX = 2;
    private final int DEST_FLOOR_STATUS_INDEX = 3;
    private final int DIRECTION_INDEX = 4;
    private final int OCCUPANTS_INDEX = 5;
    private final int FAULT_STATUS_INDEX = 6;

    /**
     * Instantiates a new ElevatorSystemGUI.
     *
     * @param numberOfElevators the number of elevators to create
     */
    public ElevatorSystemGUI(int numberOfElevators) {
        this.numberOfElevators = numberOfElevators;
        this.elevatorPanels = new ArrayList<>();
    }

    /**
     * Creates the initial GUI with base data.
     */
    public void createGUI() {
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        JFrame frame = new JFrame("Elevator System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        // wrapper panel
        JPanel elevatorsWrapperPanel = new JPanel();
        elevatorsWrapperPanel.setLayout(new GridLayout(0, this.numberOfElevators));
        frame.add(elevatorsWrapperPanel, BorderLayout.CENTER);

        // creates panels based on how many elevators there are
        for (int i = 0; i < this.numberOfElevators; i++) {
            // panel to hold info about elevator
            JPanel elevatorPanel = new JPanel();
            elevatorPanel.setLayout(new BoxLayout(elevatorPanel, BoxLayout.Y_AXIS));
            elevatorPanel.setOpaque(true);
            elevatorPanel.setBackground(Color.DARK_GRAY);
            elevatorPanel.setPreferredSize(new Dimension(400, 250));

            // label to represent elevator number
            JLabel elevatorNumLabel = new JLabel("Elevator #: " + i, SwingConstants.CENTER);
            elevatorNumLabel.setForeground(Color.LIGHT_GRAY);
            elevatorPanel.add(elevatorNumLabel);

            // label to represent door state
            JLabel doorsStatusLabel = new JLabel("Doors: Closed", SwingConstants.CENTER);
            doorsStatusLabel.setForeground(Color.WHITE);
            elevatorPanel.add(doorsStatusLabel);

            // label to represent current elevator floor
            JLabel elevatorFloorLabel = new JLabel("Current Floor: 0", SwingConstants.CENTER);
            elevatorFloorLabel.setForeground(Color.WHITE);
            elevatorPanel.add(elevatorFloorLabel);

            // label to represent destination floor
            JLabel destFloorStatusLabel = new JLabel("Destination Floor: ", SwingConstants.CENTER);
            destFloorStatusLabel.setForeground(Color.WHITE);
            elevatorPanel.add(destFloorStatusLabel);

            // label to represent direction
            JLabel directionLabel = new JLabel("Direction: Idle", SwingConstants.CENTER);
            directionLabel.setForeground(Color.WHITE);
            elevatorPanel.add(directionLabel);

            // label to represent faults
            JLabel occupantsLabel = new JLabel("Occupants: 0", SwingConstants.CENTER);
            occupantsLabel.setForeground(Color.WHITE);
            elevatorPanel.add(occupantsLabel);

            // label to represent faults
            JLabel faultLabel = new JLabel("Fault: None ", SwingConstants.CENTER);
            faultLabel.setForeground(Color.WHITE);
            elevatorPanel.add(faultLabel);

            configureLabel(elevatorNumLabel, labelFont);
            configureLabel(elevatorFloorLabel, labelFont);
            configureLabel(doorsStatusLabel, labelFont);
            configureLabel(destFloorStatusLabel, labelFont);
            configureLabel(directionLabel, labelFont);
            configureLabel(occupantsLabel, labelFont);
            configureLabel(faultLabel, labelFont);

            elevatorsWrapperPanel.add(elevatorPanel);
            elevatorPanels.add(elevatorPanel);
        }

        frame.add(elevatorsWrapperPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Updates the door information
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateDoors(int elevatorID, String status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel doorsStatusLabel = (JLabel) panel.getComponent(DOORS_STATUS_INDEX);
        SwingUtilities.invokeLater(() -> {
            doorsStatusLabel.setText("Doors: " + status);
        });
    }

    /**
     * Updates the current floor information
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateCurrentFloor(int elevatorID, int status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel currentFloorLabel = (JLabel) panel.getComponent(CURRENT_FLOOR_INDEX);
        SwingUtilities.invokeLater(() -> {
            currentFloorLabel.setText("Current Floor: " + status);
        });
    }

    /**
     * Updates the current direction information
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateDirection(int elevatorID, String status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel directionLabel = (JLabel) panel.getComponent(DIRECTION_INDEX);
        SwingUtilities.invokeLater(() -> {
            directionLabel.setText("Direction: " + status);
        });
    }

    /**
     * Updates the destination floor information
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateDestFloorStatus(int elevatorID, int status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel destFloorStatusLabel = (JLabel) panel.getComponent(DEST_FLOOR_STATUS_INDEX);
        SwingUtilities.invokeLater(() -> {
            destFloorStatusLabel.setText("Destination Floor: " + status);
        });
    }

    /**
     * Updates the occupant count
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateOccupantCount(int elevatorID, int status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel occupantsLabel = (JLabel) panel.getComponent(OCCUPANTS_INDEX);
        SwingUtilities.invokeLater(() -> {
            occupantsLabel.setText("Occupants: " + status);
        });
    }

    /**
     * Updates the fault status information
     *
     * @param elevatorID   the elevator number
     * @param status       the new information to show
     */
    public void updateFaultStatus(int elevatorID, String status) {
        JPanel panel = elevatorPanels.get(elevatorID);
        JLabel faultStatusLabel = (JLabel) panel.getComponent(FAULT_STATUS_INDEX);
        SwingUtilities.invokeLater(() -> {
            faultStatusLabel.setText("Fault: " + status);
        });
    }

    /**
     * Configures the label passed in
     *
     * @param label   the label to edit
     * @param font    the font to use
     */
    private static void configureLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    /**
     * Updates the GUI with the new information given by the newest packet received, msg, and the scheduler context attributes
     *
     * @param msg         the newest packet received
     * @param context     the scheulder context
     */
    public void updateGUI(InfoPacket msg, SchedulerContext context) {

        int elevatorID = msg.getElevatorNum();
        int currentDirection = context.getElevatorDirection()[elevatorID];

        // update for faults
        String fault = "None";
        if (context.getDoorFaultStatus()[elevatorID]) {
            fault = "Door Fault";
        } else if (currentDirection == -1) {
            fault = "Floor Fault";
        }
        this.updateFaultStatus(elevatorID, fault);
        context.resetDoorFaultStatus(elevatorID);

        // update for current floor number
        int[] currentFloor = context.getElevatorFloor();
        this.updateCurrentFloor(elevatorID, currentFloor[elevatorID]);

        // update direction
        String currentDirectionString = "Idle";
        if (currentDirection == 0) {
            currentDirectionString = "Down";
        } else if (currentDirection == 1) {
            currentDirectionString = "Up";
        }
        this.updateDirection(elevatorID, currentDirectionString);

        // update destination floor
        int destinationFloor = context.getElevatorDestination()[elevatorID];
        this.updateDestFloorStatus(elevatorID, destinationFloor);

        // update occupants
        int occupantCount = context.getElevatorOccupants()[elevatorID];
        this.updateOccupantCount(elevatorID, occupantCount);

        // update doors
        if (msg.getType() == 14) {
            this.updateDoors(elevatorID, "Open");
        } else if(msg.getType() == 13) {
            this.updateDoors(elevatorID, "Closed");
        }
    }
}

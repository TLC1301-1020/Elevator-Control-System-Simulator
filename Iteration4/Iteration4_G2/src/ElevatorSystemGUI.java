import javax.swing.*;
import java.awt.*;
import javax.swing.SwingUtilities;

public class ElevatorSystemGUI {

    private static JPanel floorsPanel;
    private static JLabel doorsStatusLabel;
    private static JLabel destFloorStatusLabel;

    private static JLabel originFloorStatusLabel;
    private static JLabel direction;
    public static void createGUI() {
        int numberOfFloors = 7;
        Font labelFont = new Font("Arial", Font.BOLD, 16);

        JFrame frame = new JFrame("Elevator System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        // wrapper panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(frame.getBackground());

        // panel to hold info about elevator
        floorsPanel = new JPanel();
        floorsPanel.setLayout(new BoxLayout(floorsPanel, BoxLayout.Y_AXIS));
        floorsPanel.setOpaque(true);
        floorsPanel.setBackground(Color.DARK_GRAY);
        floorsPanel.setPreferredSize(new Dimension(400, 250));

        // label to represent door state
        doorsStatusLabel = new JLabel("Doors: ", SwingConstants.CENTER);
        doorsStatusLabel.setForeground(Color.WHITE);
        floorsPanel.add(doorsStatusLabel);

        // label to represent destination floor
        destFloorStatusLabel = new JLabel("Destination Floor: ", SwingConstants.CENTER);
        destFloorStatusLabel.setForeground(Color.WHITE);
        floorsPanel.add(destFloorStatusLabel);

        // label to represent origin floor
        originFloorStatusLabel = new JLabel("Origin Floor: ", SwingConstants.CENTER);
        originFloorStatusLabel.setForeground(Color.WHITE);
        floorsPanel.add(originFloorStatusLabel);

        // label to represent direction
        direction = new JLabel("Direction: ", SwingConstants.CENTER);
        direction.setForeground(Color.WHITE);
        floorsPanel.add(direction);

        configureLabel(doorsStatusLabel, labelFont);
        configureLabel(destFloorStatusLabel, labelFont);
        configureLabel(originFloorStatusLabel, labelFont);
        configureLabel(direction, labelFont);

        wrapperPanel.add(floorsPanel);

        frame.add(wrapperPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ElevatorSystemGUI::createGUI);
    }

    public void updateDoors(String status) {
        SwingUtilities.invokeLater(() -> {
            doorsStatusLabel.setText("Doors: " + status);
        });
    }

    public void updateDirection(String status) {
        SwingUtilities.invokeLater(() -> {
            direction.setText("Direction: " + status);
        });
    }

    public void updateDestFloorStatus(int status) {
        SwingUtilities.invokeLater(() -> {
            destFloorStatusLabel.setText("Destination Floor: " + status);
        });
    }

    public void updateOriginFloorStatus(int status) {
        SwingUtilities.invokeLater(() -> {
            originFloorStatusLabel.setText("Origin Floor: " + status);
        });
    }

    private static void configureLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }
}

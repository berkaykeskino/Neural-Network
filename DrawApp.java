import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class DrawApp extends JFrame {

    private static final int PIXEL_SIZE = 20; // 20x zoom so 28x28 is visible
    private static final int GRID_SIZE = 28;
    private float[] inputGrid = new float[GRID_SIZE * GRID_SIZE];
    private NeuralNetwork neuralNetwork;
    private JLabel resultLabel;
    private DrawingPanel drawingPanel;

    public DrawApp() {
        setTitle("MNIST Java Predictor");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Try to Load the Model
        neuralNetwork = ModelHandler.loadModel("trained_mnist_model.net");
        if (neuralNetwork == null) {
            JOptionPane.showMessageDialog(this, "Model not found! Run App.java to train first.");
            System.exit(0);
        }

        // 2. Setup Drawing Area
        drawingPanel = new DrawingPanel();
        add(drawingPanel, BorderLayout.CENTER);

        // 3. Setup Controls
        JPanel controlPanel = new JPanel();
        JButton btnClear = new JButton("Clear");
        JButton btnEvaluate = new JButton("Evaluate");
        resultLabel = new JLabel("Draw a digit...");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));

        btnClear.addActionListener(e -> {
            Arrays.fill(inputGrid, 0);
            drawingPanel.repaint();
            resultLabel.setText("Draw a digit...");
        });

        btnEvaluate.addActionListener(e -> evaluateDrawing());

        controlPanel.add(btnClear);
        controlPanel.add(btnEvaluate);
        controlPanel.add(resultLabel);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void evaluateDrawing() {
        // 1. Visualize BEFORE predicting
        printDebugGrid(inputGrid);

        // 2. Predict
        neuralNetwork.SetInputData(inputGrid);
        float[] output = neuralNetwork.GetOutput();
        int guess = App.getArgMax(output);

        System.out.println("Prediction: " + guess);
        System.out.println("Confidence Scores: " + Arrays.toString(output));
        resultLabel.setText("Prediction: " + guess);
    }

    // --- Custom Panel for Drawing ---
    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            // Handle Mouse Dragging
            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    paintAt(e.getX(), e.getY());
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    paintAt(e.getX(), e.getY());
                }
            };
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private void paintAt(int x, int y) {
            int gridX = x / PIXEL_SIZE;
            int gridY = y / PIXEL_SIZE;

            if (gridX >= 0 && gridX < GRID_SIZE && gridY >= 0 && gridY < GRID_SIZE) {
                // 1. Center Pixel (Full Intensity)
                setPixel(gridX, gridY, 1.0f);

                // 2. Immediate Neighbors (Cross Shape) - Medium Intensity
                // This connects the lines without making them huge blocks
                setPixel(gridX + 1, gridY, 0.5f);
                setPixel(gridX - 1, gridY, 0.5f);
                setPixel(gridX, gridY + 1, 0.5f);
                setPixel(gridX, gridY - 1, 0.5f);

                repaint();
            }
        }

        private void setPixel(int x, int y, float intensity) {
            if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
                int index = y * GRID_SIZE + x;
                // Add intensity (cap at 1.0)
                inputGrid[index] = Math.min(inputGrid[index] + intensity, 1.0f);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw the grid
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int x = 0; x < GRID_SIZE; x++) {
                    float val = inputGrid[y * GRID_SIZE + x];
                    // Grayscale color (1.0 = White, 0.0 = Black)
                    // MNIST is actually White text on Black background, but usually inputs are 0-1.
                    // Let's draw it visually as Black on White for the user, but keep data 0-1
                    int colorVal = 255 - (int) (val * 255);
                    g.setColor(new Color(colorVal, colorVal, colorVal));
                    g.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

                    // Grid lines
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                }
            }
        }
    }

    // Paste this helper method inside DrawApp class
    private void printDebugGrid(float[] grid) {
        System.out.println("\n--- What the Network Sees ---");
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                float val = grid[y * 28 + x];
                if (val > 0.5f)
                    System.out.print(" #"); // Heavy Ink
                else if (val > 0.1f)
                    System.out.print(" ."); // Light Ink
                else
                    System.out.print("  "); // Background
            }
            System.out.println();
        }
        System.out.println("-----------------------------");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawApp::new);
    }
}
import java.io.*;

public class ModelHandler {

    public static void saveModel(NeuralNetwork neuralNetwork, String filePath) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(neuralNetwork);
            System.out.println("Model saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork loadModel(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            NeuralNetwork net = (NeuralNetwork) in.readObject();
            System.out.println("Model loaded from " + filePath);
            return net;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing model found at " + filePath);
            return null;
        }
    }
}
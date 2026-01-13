import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class App {
    public static void main(String[] args) throws Exception {

        int inputVectorSize = 784;
        int outputVectorSize = 10;
        int trainSamples = 10000;
        int epochs = 5;

        float[][] trainInputs = new float[trainSamples][inputVectorSize];
        float[][] trainOutputs = new float[trainSamples][outputVectorSize];

        loadMnist(trainInputs, trainOutputs, trainSamples, "xTrain.bin", "yTrain.bin");

        String modelPath = "trained_mnist_model.net";
        NeuralNetwork neuralNetwork = ModelHandler.loadModel(modelPath);

        if (neuralNetwork == null) {
            System.out.println("Training new model...");
            neuralNetwork = new NeuralNetwork(inputVectorSize, outputVectorSize);
            Trainer trainer = new Trainer(trainInputs, trainOutputs, 0.05f);

            for (int i = 0; i < epochs; i++) {
                System.out.println("Epoch: " + i);
                trainer.Train(neuralNetwork);
            }
            ModelHandler.saveModel(neuralNetwork, modelPath);
        } else {
            System.out.println("Skipping training, using loaded model.");
        }

        System.out.println("\n--- Starting Evaluation on Test Set ---");

        int testSamples = 10000;
        float[][] testInputs = new float[testSamples][inputVectorSize];
        float[][] testOutputs = new float[testSamples][outputVectorSize];

        loadMnist(testInputs, testOutputs, testSamples, "xTest.bin", "yTest.bin");

        int correctGuesses = 0;

        for (int i = 0; i < testSamples; i++) {
            neuralNetwork.SetInputData(testInputs[i]);
            float[] prediction = neuralNetwork.GetOutput();
            int guessedLabel = getArgMax(prediction);
            int actualLabel = getArgMax(testOutputs[i]);
            if (guessedLabel == actualLabel) {
                correctGuesses++;
            }
        }

        double accuracy = (double) correctGuesses / testSamples * 100;
        System.out.println("------------------------------------------------");
        System.out.println("Final Accuracy on " + testSamples + " unseen images: " + accuracy + "%");
        System.out.println("Correctly guessed: " + correctGuesses + "/" + testSamples);
    }

    private static void loadMnist(float[][] inputs, float[][] outputs, int count, String xPath, String yPath) {
        try (DataInputStream xIn = new DataInputStream(new BufferedInputStream(new FileInputStream(xPath)));
             DataInputStream yIn = new DataInputStream(new BufferedInputStream(new FileInputStream(yPath)))) {

            for (int i = 0; i < count; i++) {
                // 1. Read Image
                for (int j = 0; j < 784; j++) {
                    inputs[i][j] = (xIn.readByte() & 0xFF) / 255.0f;
                }
                
                // 2. Read Label
                int label = yIn.readByte() & 0xFF;
                
                // 3. CRITICAL FIX: Reset the output array for this sample
                // (Though 'new float[]' initializes to 0, this is safer for reuse logic)
                Arrays.fill(outputs[i], 0.0f);
                
                if (label < outputs[i].length) {
                    outputs[i][label] = 1.0f;
                }
            }
            System.out.println("Successfully loaded " + count + " samples from " + xPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getArgMax(float[] array) {
        int maxIndex = -1;
        float maxValue = -Float.MAX_VALUE;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > maxValue) {
                maxValue = array[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
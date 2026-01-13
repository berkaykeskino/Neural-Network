import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class Trainer {

    private float[][] inputData;
    private float[][] outputData;
    private float learningRate;
    private List<Integer> indices;

    public Trainer(float[][] inputData, float[][] outputData, float learningRate) {
        this.inputData = inputData;
        this.outputData = outputData;
        this.learningRate = learningRate;

        this.indices = new ArrayList<>();
        for (int i = 0; i < inputData.length; i++) {
            indices.add(i);
        }
    }

    public void Train(NeuralNetwork neuralNetwork) {
        Collections.shuffle(indices);

        float totalEpochError = 0;

        for (int i = 0; i < indices.size(); i++) {
            int dataRowIndex = indices.get(i);

            float[] data = inputData[dataRowIndex];
            float[] actualOutputs = outputData[dataRowIndex];

            neuralNetwork.SetInputData(data);
            float[] answer = neuralNetwork.GetOutput();

            for (int predictedValueIndex = 0; predictedValueIndex < answer.length; predictedValueIndex++) {
                float predicted = answer[predictedValueIndex];
                float actual = actualOutputs[predictedValueIndex];

                float lossDerivative = LossFunction.GetLossDerivative(predicted, actual);
                totalEpochError += Math.abs(predicted - actual);

                float errorSignal = lossDerivative * learningRate;
                neuralNetwork.GetOutputPerceptrons()[predictedValueIndex].BackPropogate(errorSignal);
            }
            
            if ((i + 1) % 20 == 0) {
                System.out.println("Sample " + (i + 1) + " learned");
            }
            
        }
        System.out.println("Average Error: " + (totalEpochError / inputData.length));
    }
}
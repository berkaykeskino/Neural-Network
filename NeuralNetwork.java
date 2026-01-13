import java.io.Serializable;

public class NeuralNetwork implements Serializable{
    private static final long serialVersionUID = 1L;

    private IPerceptron[] inputPerceptrons;
    private IPerceptron[] outputPerceptrons;
    private int inputSize;

    public NeuralNetwork(int inputSize, int outputSize){
        this.inputSize = inputSize;
        CreateNeuralNetwork(inputSize, outputSize);
    }

    public void SetInputData(float[] data) {
        if (data.length != inputPerceptrons.length) {
            throw new Error("Data Length (" + data.length + ") - Input Perceptron Size (" + inputPerceptrons.length
                    + ") mismatch");
        }
        for (IPerceptron p : outputPerceptrons) {
            p.Reset();
        }
        for (int i = 0; i < data.length; i++) {
            ((InputLayerPerceptron)inputPerceptrons[i]).SetData(data[i]);
        }
    }

    public float[] GetOutput() {
        float[] answer = new float[outputPerceptrons.length];
        for (int i = 0; i < outputPerceptrons.length; i++) {
            answer[i] = outputPerceptrons[i].GetValue();
        }
        return answer;
    }

    private static int GetFormerLayerSize(int currentLayerSize, int inputSize) {
        if (currentLayerSize * 5 < inputSize) {
            return currentLayerSize * 5;
        }
        return inputSize;
    }

    private IPerceptron[] FillLayer(int size) {
        if (size != inputSize) {
            int formerLayerSize = GetFormerLayerSize(size, this.inputSize);
            Perceptron[] perceptrons = new Perceptron[size];
            for (int i = 0; i < size; i++) {
                perceptrons[i] = new Perceptron(formerLayerSize, ActivationFunctionTypeEnum.SIGMOID);
            }
            return perceptrons;
        } else {
            InputLayerPerceptron[] perceptrons = new InputLayerPerceptron[size];
            for (int i = 0; i < size; i++) {
                perceptrons[i] = new InputLayerPerceptron(0);
            }
            return perceptrons;
        }
    }

    private void ConnectLayers(IPerceptron[] formerLayer, IPerceptron[] nextLayer){
        for (int i = 0; i < nextLayer.length; i++){
            IPerceptron toPerceptron = nextLayer[i];
            for (int j = 0; j < formerLayer.length; j++) {
                toPerceptron.Register(formerLayer[j], j);
            }
        }
    }

    private void CreateNeuralNetwork(int inputSize, int outputSize){
        int size = outputSize;
        outputPerceptrons = FillLayer(size);
        IPerceptron[] nextLayer = outputPerceptrons;
        while (GetFormerLayerSize(size, inputSize) != inputSize){
            size = GetFormerLayerSize(size, inputSize);
            IPerceptron[] formerLayer = FillLayer(size);
            ConnectLayers(formerLayer, nextLayer);
            nextLayer = formerLayer;
        }
        size = GetFormerLayerSize(size, inputSize);
        inputPerceptrons = FillLayer(size);
        ConnectLayers(inputPerceptrons, nextLayer);
    }

    public IPerceptron[] GetOutputPerceptrons(){
        return outputPerceptrons;
    }
}

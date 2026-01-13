import java.io.Serializable;
import java.util.Random;

public class Perceptron implements IPerceptron, Serializable{
    private static final long serialVersionUID = 1L;

    private Connection[] backwardConnections;
    private ActivationFunctionTypeEnum activationFunctionTypeEnum;
    private float value;
    private float bias;
    private Random random = new Random();
    private boolean isCalculated = false;

    public Perceptron(int formerLayerSize, ActivationFunctionTypeEnum type) {
        backwardConnections = new Connection[formerLayerSize];
        this.activationFunctionTypeEnum = type;
    }
    
    @Override
    public float GetValue() {
        if (isCalculated) {
            return value;
        }
        float answer = bias;
        for (Connection connection : backwardConnections) {
            answer += connection.GetValue();
        }
        value = ActivationFunction.CalculateActivation(
                activationFunctionTypeEnum.getActivationFunctionType(),
                answer);
        isCalculated = true;
        return value;
    }

    @Override
    public float GetLastCalculatedValue(){
        return this.value;
    } 

    @Override
    public void Register(IPerceptron fromPerceptron, int index){
        float randomWeight = (random.nextFloat() * 2) - 1;
        backwardConnections[index] = new Connection(fromPerceptron, this, randomWeight);
    }

    @Override
    public void BackPropogate(float errorSignalFromNextLayer){
        float derivative = ActivationFunction.CalculateDerivative(
                activationFunctionTypeEnum.getActivationFunctionType(),
                this.value
        );
        float myGradient = errorSignalFromNextLayer * derivative;
        this.bias -= myGradient;
        for (Connection connection : backwardConnections) {
            float errorToPropagate = myGradient * connection.GetWeight();
            float weightChange = myGradient * connection.fromPerceptron.GetLastCalculatedValue();
            connection.SetWeight(connection.GetWeight() - weightChange);
            connection.fromPerceptron.BackPropogate(errorToPropagate);
        }
    }

    @Override
    public void Reset() {
        if (!isCalculated)
            return;
        isCalculated = false;
        for (Connection connection : backwardConnections) {
            connection.fromPerceptron.Reset();
        }
    }
}

import java.io.Serializable;

public class InputLayerPerceptron implements IPerceptron, Serializable{
    private static final long serialVersionUID = 1L;
    
    private float inputData;

    public InputLayerPerceptron(float inputData){
        this.inputData = inputData;
    }

    @Override
    public float GetValue(){
        return inputData;
    }

    public void SetData(float data){
        this.inputData = data;
    }

    @Override
    public void Register(IPerceptron fromPerceptron, int index){
    }

    @Override
    public void BackPropogate(float moveRate) {
    }

    public float GetLastCalculatedValue() {
        return GetValue();
    }

    @Override
    public void Reset() {
    }
}

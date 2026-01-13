import java.io.Serializable;

public class Connection implements Serializable{
    private static final long serialVersionUID = 1L;

    public IPerceptron fromPerceptron;
    public IPerceptron toPerceptron;
    private float weight;

    public Connection(IPerceptron fromPerceptron, IPerceptron toPerceptron, float weight){
        this.fromPerceptron = fromPerceptron;
        this.toPerceptron = toPerceptron;
        this.weight = weight;
    }

    public float GetValue() {
        return fromPerceptron.GetValue() * weight;
    }

    public void SetWeight(float weight) {
        this.weight = weight;
    }

    public float GetWeight(){
        return weight;
    }
}

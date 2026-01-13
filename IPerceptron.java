public interface IPerceptron {
    float GetValue();
    void Register(IPerceptron fromPerceptron, int index);
    void BackPropogate(float moveRate);
    float GetLastCalculatedValue();
    void Reset();
}

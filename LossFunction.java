public class LossFunction {
    public static float GetLossDerivative(float predicted, float actual){
        return predicted - actual;
    }
}

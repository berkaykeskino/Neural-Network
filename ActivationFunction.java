public class ActivationFunction {
    public static float CalculateActivation(String type, float x) {
        if (ActivationFunctionTypeEnum.RELU.getActivationFunctionType().equals(type)) {
            return CalculateActivationRelu(x);
        }else if(ActivationFunctionTypeEnum.SIGMOID.getActivationFunctionType().equals(type)){
            return CalculateActivationSigmoid(x);
        }
        return 0;
    }

    private static float CalculateActivationRelu(float x) {
        if (x <= 0)
            return 0;
        return x;
    }

    private static float CalculateActivationSigmoid(float x){
        return (float)( 1 / (1 + Math.exp(-x)));
    }

    public static float CalculateDerivative(String type, float x){
        if (ActivationFunctionTypeEnum.RELU.getActivationFunctionType().equals(type)) {
            return CalculateDerivativeRelu(x);
        } else if (ActivationFunctionTypeEnum.SIGMOID.getActivationFunctionType().equals(type)) {
            return CalculateDerivativeSigmoid(x);
        }
        return 0;
    }

    private static float CalculateDerivativeRelu(float x){
        if (x <= 0)
            return 0;
        return 1;
    }

    private static float CalculateDerivativeSigmoid(float x) {
        return x * (1 - x);
    }
}

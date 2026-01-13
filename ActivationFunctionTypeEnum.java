public enum ActivationFunctionTypeEnum {
    SIGMOID("SIGMOID"),
    RELU("RELU");

    private String activationFunctionType;

    private ActivationFunctionTypeEnum(String type) {
        this.activationFunctionType = type;
    }

    public String getActivationFunctionType() {
        return activationFunctionType;
    }
}

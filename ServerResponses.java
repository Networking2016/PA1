
import java.util.HashMap;
import java.util.Map;

/* Inspiration and partial code from Jon Skeet per http://stackoverflow.com/a/22305652 */

public enum ServerResponses {
    INVALID_OPERATION(-1, "Incorrect operation command"),
    TOO_FEW_INPUTS(-2, "Number of inputs is less than two"),
    TOO_MANY_INPUTS(-3, "Number of inputs is greater than four"),
    NON_NUMERIC_INPUT(-4, "One or more of the inputs contain(s) non-number(s)"),
    EXIT(-5, "Exit");

    private static final Map<Integer, ServerResponses> VALUE_TO_ENUM_MAP;
    private final int value;
    private final String description;
    
    static {
        VALUE_TO_ENUM_MAP = new HashMap<>();
        for (ServerResponses type : values()) {
            VALUE_TO_ENUM_MAP.put(type.value, type);
        }
    }
    
    private ServerResponses(int value, String description) {
        this.value = value;
        this.description = description;
    }
    public int getValue() {
        return value;
    }
        
    public String getDescription() {
        return description;
    }
    
    public static ServerResponses forValue(int value) {
        return VALUE_TO_ENUM_MAP.get(value);
    }
}

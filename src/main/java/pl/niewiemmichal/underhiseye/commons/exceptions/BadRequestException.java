package pl.niewiemmichal.underhiseye.commons.exceptions;

public class BadRequestException extends RuntimeException {

    private final String resource;
    private final String field;
    private final String value;
    private final String reason;

    public BadRequestException(String resource, String field, String value, String reason) {
        super(resource + " with " + field + "=" + value + " " + reason);
        this.resource = resource;
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public String getReason() {return reason; }
}

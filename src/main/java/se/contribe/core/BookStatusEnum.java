package se.contribe.core;

/**
 * Created by benny on 2018-01-17.
 */
public enum BookStatusEnum {
    OK(0),
    NOT_IN_STOCK(1),
    DOES_NOT_EXIST(2);

    private int value;

    BookStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

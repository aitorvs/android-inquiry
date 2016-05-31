package com.heinrichreimer.inquiry.demo.model;

import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;

import java.util.Arrays;
import java.util.Random;

@Table
public class Stuff {
    @Column private byte primitiveByte;
    @Column private short primitiveShort;
    @Column private int primitiveInt;
    @Column private long primitiveLong;
    @Column private float primitiveFloat;
    @Column private double primitiveDouble;
    @Column private boolean primitiveBoolean;
    @Column private char primitiveChar;

    @Column private Byte objectByte;
    @Column private Short objectShort;
    @Column private Integer objectInt;
    @Column private Long objectLong;
    @Column private Float objectFloat;
    @Column private Double objectDouble;
    @Column private Boolean objectBoolean;
    @Column private Character objectChar;

    @Column private String objectString;
    @Column private char[] primitiveChars;
    @Column private Character[] objectChars;

    public Stuff() {
    }

    public Stuff(Random random) {
        byte[] bytes = new byte[1];
        random.nextBytes(bytes);
        primitiveByte = bytes[0];
        primitiveShort = (short) random.nextInt(Short.MAX_VALUE);
        primitiveInt = random.nextInt();
        primitiveLong = random.nextLong();
        primitiveFloat = random.nextFloat();
        primitiveDouble = random.nextDouble();
        primitiveBoolean = random.nextBoolean();
        primitiveChar = (char) random.nextInt();

        random.nextBytes(bytes);
        objectByte = bytes[0];
        objectShort = (short) random.nextInt(Short.MAX_VALUE);
        objectInt = random.nextInt();
        objectLong = random.nextLong();
        objectFloat = random.nextFloat();
        objectDouble = random.nextDouble();
        objectBoolean = random.nextBoolean();
        objectChar = (char) random.nextInt();

        objectString = String.valueOf(randomCharArray(random));
        primitiveChars = randomCharArray(random);
        objectChars = randomCharacterArray(random);
    }

    @Override
    public String toString() {
        return "Stuff{" +
                "primitiveByte=" + primitiveByte +
                ", primitiveShort=" + primitiveShort +
                ", primitiveInt=" + primitiveInt +
                ", primitiveLong=" + primitiveLong +
                ", primitiveFloat=" + primitiveFloat +
                ", primitiveDouble=" + primitiveDouble +
                ", primitiveBoolean=" + primitiveBoolean +
                ", primitiveChar=" + primitiveChar +
                ", objectByte=" + objectByte +
                ", objectShort=" + objectShort +
                ", objectInt=" + objectInt +
                ", objectLong=" + objectLong +
                ", objectFloat=" + objectFloat +
                ", objectDouble=" + objectDouble +
                ", objectBoolean=" + objectBoolean +
                ", objectChar=" + objectChar +
                ", objectString='" + objectString + "'" +
                ", primitiveChars='" + Arrays.toString(primitiveChars) + "'" +
                ", objectChars='" + Arrays.toString(objectChars) + "'" +
                '}';
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) primitiveByte;
        result = 31 * result + (int) primitiveShort;
        result = 31 * result + primitiveInt;
        result = 31 * result + (int) (primitiveLong ^ (primitiveLong >>> 32));
        result = 31 * result + (primitiveFloat != +0.0f ? Float.floatToIntBits(primitiveFloat) : 0);
        temp = Double.doubleToLongBits(primitiveDouble);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (primitiveBoolean ? 1 : 0);
        result = 31 * result + (int) primitiveChar;
        result = 31 * result + (objectByte != null ? objectByte.hashCode() : 0);
        result = 31 * result + (objectShort != null ? objectShort.hashCode() : 0);
        result = 31 * result + (objectInt != null ? objectInt.hashCode() : 0);
        result = 31 * result + (objectLong != null ? objectLong.hashCode() : 0);
        result = 31 * result + (objectFloat != null ? objectFloat.hashCode() : 0);
        result = 31 * result + (objectDouble != null ? objectDouble.hashCode() : 0);
        result = 31 * result + (objectBoolean != null ? objectBoolean.hashCode() : 0);
        result = 31 * result + (objectChar != null ? objectChar.hashCode() : 0);
        result = 31 * result + (objectString != null ? objectString.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(primitiveChars);
        result = 31 * result + Arrays.hashCode(objectChars);
        return result;
    }

    private static char[] randomCharArray(Random random) {
        char[] chars = new char[random.nextInt(10)];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) random.nextInt();
        }
        return chars;
    }

    private static Character[] randomCharacterArray(Random random) {
        Character[] chars = new Character[random.nextInt(10)];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) random.nextInt();
        }
        return chars;
    }
}

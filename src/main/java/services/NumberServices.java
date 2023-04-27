package services;

public class NumberServices {

    public static double roundDouble(double number) {
        return  Math.round(number* 100.0) / 100.0;
    }
}

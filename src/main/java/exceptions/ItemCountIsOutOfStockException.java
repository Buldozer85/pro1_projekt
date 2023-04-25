package exceptions;

public class ItemCountIsOutOfStockException extends Exception{
    @Override
    public String getMessage() {
        return "Daný počet položek již není na skladě";
    }
}

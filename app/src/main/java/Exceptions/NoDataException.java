package Exceptions;

/**
 * Created by Paris on 14/4/2015.
 */
public class NoDataException extends Exception {
    public NoDataException(){
        super("data not available");
    }
}

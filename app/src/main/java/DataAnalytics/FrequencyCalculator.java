package DataAnalytics;

import android.util.Log;

import java.util.LinkedList;

import Exceptions.NotEnoughDataException;

/**
 * Created by Paris on 6/4/2015.
 */
public class FrequencyCalculator {
    LinkedList<Long> buffer=new LinkedList<Long>();
    int size = 2;

    public FrequencyCalculator(int size) {
        if (size >= 2) {
            this.size = size;
        } else Log.d("FreqCalculator", "Bad Buffer size");
    }

    public float CalculateFrequency(float strength) throws NotEnoughDataException {
        buffer.add(System.currentTimeMillis());
        if (buffer.size() > size) {
            buffer.removeFirst();

            return ((float)1000.0/((buffer.peekLast() - buffer.peekFirst()) / (size - 1)));
        }throw new NotEnoughDataException();
    }

}

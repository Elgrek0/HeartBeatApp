package dataAnalytics;

import android.util.Log;

import java.util.LinkedList;

import exceptions.NotEnoughDataException;

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
            buffer.remove(0);

            return ((float)1/((buffer.getLast() - buffer.getFirst()) / (1000.0f*(size - 1))));
        }throw new NotEnoughDataException();
    }

}

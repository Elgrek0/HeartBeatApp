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
        float rfreq=0;
        if (buffer.size() > size) {
            buffer.remove(0);
            for(int i=1;i<size;i++)
                rfreq+=buffer.get(i)-buffer.get(i-1);
            rfreq/=(size-1);
            rfreq=1000/rfreq;
            return (rfreq);
        }throw new NotEnoughDataException();
    }

}

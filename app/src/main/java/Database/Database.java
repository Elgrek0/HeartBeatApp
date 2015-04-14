package database;

/**
 * Created by George on 4/5/2015.
 */
public interface Database {

    //Commit new sample from heart beat monitor to Database
    public void commitNewHBSample(float sampleValue);

    //Get (x = num) Data from beginning
    public float[] getData(int num) throws Exception;

    //Get Data from start to end
    public float[] getDataRange(int start, int end);

    //Get Data Up to end ,where end is index
    public float[] getDataBefore(int end);

    //Get Data from start up to database length,where start is index
    public float[] getDataAfter(int start);

    //Get Specific Data. p.x: 0,5,234,543,ktlp wtf?
    public float getSpecificData(int pos)throws Exception;

}

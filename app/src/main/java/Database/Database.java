package Database;

/**
 * Created by George on 4/5/2015.
 */
public interface Database {

    //Commit new sample from heart beat monitor to Database
    public void commitNewHBSample(float sampleValue);

    public int[] getData(int a);

}

package Database;

/**
 * Created by George on 4/5/2015.
 */
public interface Database {

    //Commit new sample from heart beat monitor to Database
    public void commitNewHBSample(float sampleValue);

	//Get (x = num) Data from beginning
    public int[] getData(int num);
	//Get Data from start to end
	public int[] getDataRange(int start,int end);
	//Get Data Up to end ,where end is index
	public int[] getDataBefore(int end);
	//Get Data from start up to database length,where start is index
	public int[] getDataAfter(int start);
	//Get Specific Data. p.x: 0,5,234,543,ktlp
	public int[] getSpecificData(int... pos);
	
}

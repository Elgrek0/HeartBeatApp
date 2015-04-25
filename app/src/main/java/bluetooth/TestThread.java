package bluetooth;

import java.util.Random;

import database.DatabaseAccess;

/**
 * Created by George on 4/20/2015.
 */
public class TestThread extends Thread {

    DatabaseAccess dba;

    @Override
    public void run() {

        Random rand = new Random();

        while(true) {

            int sample = rand.nextInt()%150 + 30;
            dba.commitNewHBSample(sample);

            try {
                sleep(1000,0);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

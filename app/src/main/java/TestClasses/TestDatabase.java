package testClasses;



import android.util.Log;


import database.DatabaseAccess;

import static java.lang.Math.sin;

/**
 * Created by Paris on 6/4/2015.
 */
public class TestDatabase {

        DatabaseAccess database;



        public TestDatabase(DatabaseAccess dbs) {
            database=dbs;
            Log.d("Test Database","test1");
            testaddlinearsignal();
            //Log.d("Test Database","test2");
           // testaddsinsignal();
            Log.d("Test Database","test3");
            readdatabase();
        }


        public void testaddlinearsignal() {
           for(int i=0;i<10;i++) {
               database.commitNewHBSample(1);
               try {
                   synchronized(this){
                   wait(100);}
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        }

        public void testaddsinsignal() {
            for(int i=0;i<360;i++) {
                database.commitNewHBSample(1);
                try {
                    synchronized(this){
                    wait((int)sin(2*3.14*1/22));}
                } catch (InterruptedException e) {
                    System.out.println("insufisient data");
                }
            }
        }
    public void readdatabase() {
        try {
            Float[] data=(Float[])database.getData(5,"Beats",Float.class,"frequency");
            for(int i=0;i<5;i++)
                System.out.println(data[i]);

        } catch (Exception e) {
            System.out.println(e);
        }

    }
    }


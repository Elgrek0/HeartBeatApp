package database;

import exceptions.NoSuchTypeException;

/**
 * Created by Paris on 23/4/2015.
 */
public class TypeHandler {
    public static String convert(Object obj){
        Class c=obj.getClass();
        if(c==String.class)
            return((String)obj);
        if(c==Long.class)
            return(Long.toString((Long) obj));
        if(c==Integer.class)
            return(Integer.toString((Integer)obj));
        if(c==Float.class)
            return(Float.toString((Float)obj));
        if(c==Integer.class)
            return(Double.toString((Double)obj));
        else
            return("");
    }
    public static Object rconvert(Class c, String s) throws NoSuchTypeException {

        if(c==String.class)
            return(s);
        if(c==Long.class)
            return((long)Integer.parseInt(s));
        if(c==Integer.class)
            return(Integer.parseInt(s));
        if(c==Float.class)
            return(Float.parseFloat(s));
        if(c==Double.class)
            return((double)Float.parseFloat(s));

            System.out.println("no such type");
        throw(new NoSuchTypeException());
    }

    public static String finddbname(Object obj) throws Exception {
        Class c=obj.getClass();
        if(c==String.class)
            return("STRING");
        if(c==Long.class||c==Integer.class)
            return("INTEGER");
        if(c==Float.class||c==Integer.class)
            return("REAL");
        else
           throw(new Exception("unknown datatype"));
    }
    public static String finddbname(Class c) throws Exception {

        if(c==String.class)
            return("STRING");
        if(c==Long.class||c==Integer.class)
            return("INTEGER");
        if(c==Float.class||c==Integer.class)
            return("REAL");
        else
            throw(new Exception("unknown datatype"));
    }

}

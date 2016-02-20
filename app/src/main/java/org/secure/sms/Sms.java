package org.secure.sms;

/**
 * Created by mohsen raeisi on 20/02/2016.
 */
public class Sms {
    String body;
    String from;
    String date;
    String id;
    public Sms(String body, String from, String date , String id) {
        this.body = body;
        this.from = from;
        this.date = date;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id +"  "+this.body + "  " + this.from + "  " + this.date;
    }
}

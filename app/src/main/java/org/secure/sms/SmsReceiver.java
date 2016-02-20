package org.secure.sms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver 
{

	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    public static final String ID = "_id";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;

    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;
	

	public void onReceive( Context context, Intent intent ) 
	{
        Bundle extras = intent.getExtras();
        
        String messages = "";

        if ( extras != null )
        {
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
            ContentResolver contentResolver = context.getContentResolver();
            Log.e("size : ",smsExtra.length +"");
            for ( int i = 0; i < smsExtra.length; ++i )
            {
            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
            	
            	String body = sms.getMessageBody().toString();
            	String address = sms.getOriginatingAddress();
                
                messages += "SMS from " + address + " :\n";                    
                messages += body + "\n";
                

                
                putSmsToDatabase( context,contentResolver, sms );
            }


            Intent intent2 = new Intent(context,SecureMessagesActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);

        }

	}
	
	private void putSmsToDatabase(Context context, ContentResolver contentResolver, SmsMessage sms )
	{
        ContentValues values = new ContentValues();
        values.put( DATE, sms.getTimestampMillis() );
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, sms.getStatus() );
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put(SEEN, MESSAGE_IS_NOT_SEEN);
        try
        {
            if(sms.getMessageBody().contains("esgi")) {
                values.put(BODY, "sms is changed!!!");
                values.put( ADDRESS, "00000" );
            }

            else {
                values.put(BODY, sms.getMessageBody());
                values.put(ADDRESS, sms.getOriginatingAddress());
            }



                Toast.makeText(context , values.get(BODY).toString(), Toast.LENGTH_SHORT ).show();

        }
        catch ( Exception e ) 
        { 
        	e.printStackTrace(); 
    	}

        contentResolver.insert(Uri.parse(SMS_URI), values);
	}
}

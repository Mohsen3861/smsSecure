package org.secure.sms;

import java.util.ArrayList;

import org.secure.sms.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SecureMessagesActivity extends Activity implements OnClickListener,OnItemClickListener
{
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setTheme(android.R.style.Theme_Light);
        setContentView(R.layout.main);

		update();
        
        this.findViewById( R.id.UpdateList ).setOnClickListener( this );
    }

    ArrayList<Sms> smsList = new ArrayList<Sms>();
    ArrayList<String> smsString = new ArrayList<String>();


    public void onClick( View v )
	{
		update();
	}

	public void update () {
		ContentResolver contentResolver = getContentResolver();
		Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);

		int indexBody = cursor.getColumnIndex(SmsReceiver.BODY);
		int indexAddr = cursor.getColumnIndex( SmsReceiver.ADDRESS );
        int indexDate = cursor.getColumnIndex( SmsReceiver.DATE );
        int indexId = cursor.getColumnIndex( SmsReceiver.ID );

        if ( indexBody < 0 || !cursor.moveToFirst() ) return;

		smsList.clear();
        smsString.clear();

		do
		{
			String str = "Sender: " + cursor.getString( indexAddr ) + "\n" + cursor.getString( indexBody )+"\n" +  cursor.getString(indexDate) ;
            smsString.add(str);
            Sms sms = new Sms(cursor.getString( indexBody ),cursor.getString( indexAddr ),cursor.getString(indexDate),cursor.getString(indexId));
			smsList.add( sms );
		}
		while( cursor.moveToNext() );


		ListView smsListView = (ListView) findViewById( R.id.SMSList );
        smsListView.setOnItemClickListener(this);
		smsListView.setAdapter( new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, smsString) );
	}


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Sms sms = smsList.get(position);
        Log.e("sms clicked : " , smsList.get(position).toString());
        deleteSms(sms);
    }
    public static final String SMS_URI = "content://sms";

    public void deleteSms(Sms sms){

        Context context = getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();

        String where = "_id=?";
        String[] args = new String[] { sms.id };

        contentResolver.delete(Uri.parse(SMS_URI),where,args);

        update();

    }


}

/*
$ telnet
$ open localhost 5555
$ sms send 1234 bojour
 */
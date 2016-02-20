package org.secure.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by mohsen raeisi on 19/02/2016.
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

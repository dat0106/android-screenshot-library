package pl.polidea.asl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.view.*;
import android.widget.*;

public class ScreenshotDemo extends Activity {

	/*
	 * The ImageView used to display taken screenshots.
	 */
	private ImageView imgScreen;

	private ServiceConnection aslServiceConn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			aslProvider = IScreenshotProvider.Stub.asInterface(service);
		}
	};
	private IScreenshotProvider aslProvider = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        imgScreen = (ImageView)findViewById(R.id.imgScreen);
        Button btn = (Button)findViewById(R.id.btnTakeScreenshot);
        btn.setOnClickListener(btnTakeScreenshot_onClick);

        // connect to ASL service
        String className = IScreenshotProvider.class.getName();
        bindService (new Intent(className), aslServiceConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
    	unbindService(aslServiceConn);
    }


    private View.OnClickListener btnTakeScreenshot_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (aslProvider == null)
				Toast.makeText(ScreenshotDemo.this, R.string.n_a, Toast.LENGTH_SHORT).show();
			else {
				String file = null;
				try {
					file = aslProvider.takeScreenshot();
				} catch (RemoteException e) {
					// squelch
				}
				if (file == null)
					Toast.makeText(ScreenshotDemo.this, R.string.screenshot_error, Toast.LENGTH_SHORT).show();
			}

		}
	};
}
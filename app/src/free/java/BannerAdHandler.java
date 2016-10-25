import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.manleysoftware.michael.discgolfapp.R;

/**
 * Created by Michael on 10/25/2016.
 */

public class BannerAdHandler {

	public static View generateBannerAd(Context context, int resourseID){
		AdView adView = (AdView) context.findViewById(resourseID);
		AdRequest request = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();
		adView.loadAd(request);
	}
}

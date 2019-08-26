package org.npl.biomet.mmsim;

import com.google.common.eventbus.Subscribe;
import org.micromanager.Studio;
import org.micromanager.data.DataProvider;
import org.micromanager.data.DataProviderHasNewImageEvent;
import org.micromanager.data.internal.DefaultNewImageEvent;
import org.micromanager.magellan.imagedisplay.NewImageEvent;

public class AcquisitionLister {
	public AcquisitionLister(Studio studio_) {
		System.out.println("Init AL");
	}

	@Subscribe
	void onNewImage(DefaultNewImageEvent e){
		System.out.println("New image");
	}

	@Subscribe
	void onNewImage(DataProviderHasNewImageEvent e){
		System.out.println("New image");
	}

	@Subscribe
	void onDataProviderHasNewImageEvent(DataProviderHasNewImageEvent e){
		System.out.println("New image");
	}

}

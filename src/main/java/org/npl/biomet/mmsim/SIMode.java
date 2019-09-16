package org.npl.biomet.mmsim;

import com.google.common.eventbus.Subscribe;
import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.DataProvider;
import org.micromanager.data.DataProviderHasNewImageEvent;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.display.DataViewer;
import org.micromanager.events.AcquisitionStartedEvent;
import org.micromanager.events.DatastoreClosingEvent;
import org.micromanager.events.NewDisplayEvent;
//import org.micromanager.magellan.imagedisplay.NewImageEvent;

	import java.io.IOException;
//import org.micromanager.magellan.imagedisplay.NewImageEvent;
//import org.micromanager.magellan.imagedisplay.NewImageEvent;

public class SIMode {
	private final Studio studio_;
	Datastore datastore;

	public SIMode(Studio studio) {
		studio_ = studio;
	}

	@Subscribe
	public void onNewDisplayEvent(NewDisplayEvent e){
//		DataProvider new_provider = e.getDisplay().getDataProvider();
		System.out.println("NewDisplayEvent");
	}

//	@Subscribe
//	public void onDisplayAboutToShowEvent(DisplayAboutToShowEvent e){
////		DataProvider new_provider = e.getDisplay().getDataProvider();
//		System.out.println("NewDisplayEvent");
//	}

	@Subscribe
	public void onDatastoreClosingEvent(DatastoreClosingEvent e){
//		DataProvider new_provider = e.getDisplay().getDataProvider();
		System.out.println("DatastoreClosingEvent");
	}



	@Subscribe
	public void onAcquisitionStarted(AcquisitionStartedEvent ase){
		SequenceSettings settings_ = studio_.acquisitions().getAcquisitionSettings();
//		channels =
		studio_.acquisitions().attachRunnable(-1,-1,-1,-1,this.runnable());
		System.out.println("Acquisition started");
		Datastore datastore = ase.getDatastore();
//		DataViewer display = studio_.displays().getActiveDataViewer();
//		System.out.println(display);
//		DataProvider provider = display.getDataProvider();
		System.out.println(datastore.getMaxIndices());
//		studio_.displays().createDisplay(provider);
//		try {
//			Image image = datastore.getAnyImage();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//		studio_.displays().
////		ase.getDatastore().getData
//		DataProvider provider = new DataStoreProviderConverter(studio_,datastore);
////		ase.getSource()
//		datastore.registerForEvents(new AcquisitionLister(studio_));
//		datastore.registerForEvents(this);
//		provider.registerForEvents(this);
//		provider.registerForEvents(new AcquisitionLister(studio_));
////		DataProvider fft_provider = new FFTProvider(studio_,datastore);
////		new FFTThread(studio_,fft_provider);
	}

	private Runnable runnable() {
		System.out.println(datastore.getMaxIndices());
		return null;
	}

	@Subscribe
	public void onDataProviderHasNewImageEvent(DataProviderHasNewImageEvent e){
		System.out.println("OO");
	}


}

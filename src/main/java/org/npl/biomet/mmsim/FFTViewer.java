package org.npl.biomet.mmsim;

//import com.sun.org.apache.xpath.internal.operations.Bool;
import org.micromanager.SnapLiveManager;
import org.micromanager.Studio;
import org.micromanager.data.*;
import org.micromanager.display.DisplayWindow;
import org.micromanager.events.LiveModeEvent;
import org.micromanager.display.DataViewer;
import com.google.common.eventbus.Subscribe;
import org.micromanager.data.ImageJConverter;

import java.io.IOException;

//import org.micromanager.internal.SnapLiveManager;
public class FFTViewer{
	private final Studio studio_;
	public Datastore store;
	private DisplayWindow liveWindow;
	private ImageJConverter ijconverter;

	public FFTViewer(Studio studio) {
		this(studio, null);
	}
	public FFTViewer(Studio studio, Datastore store){
		studio_ = studio;
		ijconverter = studio_.data().getImageJConverter();
		store = studio_.data().createRAMDatastore();
	}

	@Subscribe
	public void onAcquisitionStartedEvent(LiveModeEvent ase) {
		boolean isOnLive = ase.getIsOn();
		SnapLiveManager live_ = studio_.live();
		if (live_.getIsLiveModeOn()) {
			System.out.println("Live on");
			studio_.live().getDisplay().duplicate();
//			Coords.CoordsBuilder coordsbuilder = Coordinates.builder();
//			Metadata.Builder metaDataDuilder = studio_.data().getMetadataBuilder();

//			DisplayWindow live_display = live_.getDisplay();
//			live_display.duplicate();
//			live_display.getDataProvider();
//			try {
//
//				DataProvider dataProvider_ = live_display.getDataProvider();
//
//				DisplayWindow live_displayprovider = studio_.displays().createDisplay(dataProvider_);
//				live_displayprovider.setDisplaySettings(live_display.getDisplaySettings());
////				DisplayWindow liveFFT = studio_.displays().createDisplay(live_displayprovider);
//
//				System.out.println(dataProvider_.getAxes());
//				System.out.println(dataProvider_.getMaxIndices());
//				Image current_image = dataProvider_.getAnyImage();
//				studio_.displays().show(current_image);
//				store.putImage(current_image);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (DatastoreFrozenException e) {
//				e.printStackTrace();
//			} catch (DatastoreRewriteException e) {
//				e.printStackTrace();
//			} catch (NullPointerException e) {
//				e.printStackTrace();
//			}
//			Image current_image = ijconverter.createImage(live_display.getImagePlus().getProcessor(),
//				coordsbuilder.build(),
//				metaDataDuilder.build());
//			live_display.getImagePlus();
		} else {
			System.out.println("Live off");
		}
	}

	public Image doFFT(Image image){
		Image fft = image;
		return fft;
	}
}

package org.npl.biomet.mmsim;


import com.google.common.eventbus.Subscribe;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.MontageMaker;
import ij.process.ImageProcessor;
import mmcorej.CMMCore;
import mmcorej.TaggedImage;
import org.micromanager.Studio;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.*;
import org.micromanager.display.DisplayWindow;
import org.micromanager.events.AcquisitionEndedEvent;
import org.micromanager.events.AcquisitionStartedEvent;
import org.micromanager.internal.utils.imageanalysis.ImageUtils;

import java.io.IOException;
import java.util.List;

public class SIMMode{
	int SIMMAGES = 9;
	private final Studio studio_;
	private final ImageJConverter ij_converter;
	private final MontageMaker montager;
	private final CMMCore mmc;

	private Datastore datastore_;
	private Coords datastore_coords_;
	private List<DisplayWindow> mda_displays;
	private Datastore mda_montage;
	private DisplayWindow display = null;
	private DisplayWindow mda_montage_display;
	private Coords mda_coords;
	private Image current_image;
	private Metadata metadata;
	private ImagePlus montage;
	private Image montage_image;
	private ImageStack sim_stack;
	private boolean runningState = false;
	private long CAMERA_HEIGHT;
	private long CAMERA_WIDTH;

//   private ImageProcessor montage_ip;

	public SIMMode(Studio studio) {
		studio_ = studio;
		mmc = studio_.core();
		ij_converter = studio_.data().getImageJConverter();
		montager = new MontageMaker();
	}

	public void newFrame() {
		if(runningState) {
			try {
				TaggedImage tImg;
				//         ImageUtils imageutils = new ImageUtils();
         System.out.println("Runnable");
				//%TODO FIX WITHS AND HEIGHTS
				CAMERA_HEIGHT = mmc.getImageHeight();
				CAMERA_WIDTH = mmc.getImageWidth();
				System.out.println(CAMERA_HEIGHT);

				ImageProcessor current_image_processor = ImageUtils.makeProcessor(mmc.getTaggedImage());
				sim_stack = new ImageStack((int) CAMERA_WIDTH, (int) CAMERA_HEIGHT);
//				sim_stack.addSlice(current_image_processor);
				mmc.startSequenceAcquisition(SIMMAGES - 1, 0, false);
				while (mmc.isSequenceRunning() || mmc.getRemainingImageCount() > 0) {
					if (mmc.getRemainingImageCount() > 0) {
						tImg = mmc.popNextTaggedImage();
						ImageProcessor proc0 = ImageUtils.makeProcessor(tImg);
						sim_stack.addSlice(proc0);
					}
				}
//      montage.show();
//      ImageProcessor montage_ip = montage.getProcessor();
			} catch (Exception e) {
				e.printStackTrace();
				montage = null;
				sim_stack = null;
			}
		}
	}

	@Subscribe
	public void onAcquisitionStarted(AcquisitionStartedEvent e) {
//		e.getSettings();
//		studio_.acquisitions().getAcquisitionSettings().
		datastore_ = e.getDatastore();
		datastore_.registerForEvents(this);
		System.out.println("AcquisitionStartedEvent");
		SequenceSettings settings_ = studio_.acquisitions().getAcquisitionSettings();
		SummaryMetadata summarymetadata = studio_.acquisitions().generateSummaryMetadata();
		mda_montage = studio_.data().createRAMDatastore();
		mda_montage_display = studio_.getDisplayManager().createDisplay(mda_montage);
		CAMERA_HEIGHT = mmc.getImageHeight();
		CAMERA_WIDTH = mmc.getImageWidth();

	}

	@Subscribe
	public void onDataProviderHasNewImageEvent(DataProviderHasNewImageEvent e){

		System.out.println("DataProviderHasNewImageEvent");

		studio_.acquisitions().setPause(true);
//      DataProvider mda_provider = e.getDataProvider();
		mda_coords = e.getCoords();
		current_image = e.getImage();
		metadata = current_image.getMetadata();
		System.out.println(mda_coords);
//		Coords.Builder mda_coords_builder = mda_coords.copyBuilder();
//		mda_coords_builder.index("slm",0);
////      studio_.acquisitions().
		int count = 0;
		int maxTries = 10;
		while(true) {
			try {
				//			ImageProcessor current_image_processor = ij_converter.createProcessor(current_image);
				//			sim_stack.addSlice(current_image_processor);
				ImagePlus sim_stack_plus = new ImagePlus("Stack", sim_stack);
				montage = montager.makeMontage2(sim_stack_plus, SIMMAGES / 3, SIMMAGES / 3, 1.00, 1, SIMMAGES, 1, 0, false);
				montage_image = ij_converter.createImage(montage.getProcessor(), mda_coords, metadata);
				mda_montage.putImage(montage_image);
				break;
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
				if (++count == maxTries) break;
			} catch (IOException ex) {
				ex.printStackTrace();
				if (++count == maxTries) break;
			}
		}
		studio_.acquisitions().setPause(false);
	}

	@Subscribe
	public void onAcquisitionEndedEvent(AcquisitionEndedEvent e){
//		DataProvider new_provider = e.getDisplay().getDataProvider();
		String acquisition_path = datastore_.getSavePath();
//      studio_.acquisitions().getAcquisitionSettings().;
		System.out.println(acquisition_path);
		if(acquisition_path!=null){
			try {
				//TODO determine savemode and mimic
				mda_montage.save(Datastore.SaveMode.MULTIPAGE_TIFF,acquisition_path.concat("_SIM"));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("AcquisitionEndedEvent");
//      System.out.println(mda_montage);
//      mda_montage.setSavePath("/Users/craggles/Desktop");
//      try {
////         datastore_coords_ = datastore_.getMaxIndices();
////         current_image = datastore_.getImage(datastore_coords_);
////         mda_montage.putImage(current_image);
////         mda_montage.save(Datastore.SaveMode.MULTIPAGE_TIFF,"/Users/craggles/Desktop/ok/");
//      } catch (IOException ex) {
//         ex.printStackTrace();
//      }

	}

	public void running(boolean runningState) {
		this.runningState = runningState;
	}
}

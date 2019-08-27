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

import java.util.List;

public final class runnable implements Runnable {

   private final Studio studio_;
   private final ImageJConverter ij_converter;
   private final CMMCore mmc;

//   private SequenceSettings settings_;
   private Datastore datastore_;
   private Coords datastore_coords_;
//   private List<DisplayWindow> mda_displays;
//   private DataProvider mda_provider;
//   private Coords mda_coords;
   private Datastore mda_montage;
//   private Image current_image;
   private DisplayWindow display = null;
   DisplayWindow mda_montage_display;

   private ImageProcessor montage_ip;

   public runnable(Studio studio) {
      studio_ = studio;
      mmc = studio_.core();
      ij_converter = studio_.data().getImageJConverter();

   }

   @Override
   public void run() {
   }

   @Subscribe
   public void onAcquisitionStarted(AcquisitionStartedEvent e) {

      datastore_ = e.getDatastore();
      datastore_.registerForEvents(this);
      System.out.println("AcquisitionStartedEvent");
//      settings_ = studio_.acquisitions().getAcquisitionSettings();
      SummaryMetadata summarymetadata = studio_.acquisitions().generateSummaryMetadata();
      mda_montage = studio_.data().createRAMDatastore();
      mda_montage_display = studio_.getDisplayManager().createDisplay(mda_montage);

   }

   @Subscribe
   public void onDataProviderHasNewImageEvent(DataProviderHasNewImageEvent e){

      System.out.println("DataProviderHasNewImageEvent");

      studio_.acquisitions().setPause(true);

//      mda_provider = e.getDataProvider();
	   Coords mda_coords = e.getCoords();
	   Image current_image = e.getImage();
       Metadata metadata = current_image.getMetadata();
      System.out.println(mda_coords);
////      studio_.acquisitions().
      try {
         ImageProcessor current_image_processor = ij_converter.createProcessor(current_image);
         System.out.println("Runnable");
         ImageStack stack = new ImageStack( current_image_processor.getWidth(),  current_image_processor.getHeight());
         stack.addSlice(current_image_processor);
         studio_.core().startSequenceAcquisition(8, 0, false);
         while (mmc.isSequenceRunning() || mmc.getRemainingImageCount() > 0) {
            if (mmc.getRemainingImageCount() > 0) {
               TaggedImage tImg = mmc.popNextTaggedImage();
               ImageProcessor proc0 = ImageUtils.makeProcessor(tImg);
               stack.addSlice(proc0);
            }
         }
         ImagePlus imagestack = new ImagePlus("Stack", stack);
         MontageMaker montager = new MontageMaker();
         ImagePlus montage = montager.makeMontage2(imagestack, 3, 3, 1.00, 1, 9, 1, 0, false);
         montage_ip = montage.getProcessor();
         Image montage_image = ij_converter.createImage(montage_ip, mda_coords, metadata);
         mda_montage.putImage(montage_image);
      } catch (Exception ex) {
         ex.printStackTrace();
      }

      studio_.acquisitions().setPause(false);
   }

   @Subscribe
   public void onAcquisitionEndedEvent(AcquisitionEndedEvent e){
      System.out.println("AcquisitionEndedEvent");
      System.out.println(mda_montage);
   }
}

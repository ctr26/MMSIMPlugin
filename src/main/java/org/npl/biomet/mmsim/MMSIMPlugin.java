/**
 * Binding to ClearVolume 3D viewer View Micro-Manager datasets in 3D
 *
 * AUTHOR: Nico Stuurman COPYRIGHT: Regents of the University of California,
 * 2015
 * LICENSE: This file is distributed under the BSD license. License text is
 * included with the source distribution.
 *
 * This file is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.
 *
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 */

package org.npl.biomet.mmsim;

import com.google.common.eventbus.Subscribe;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.MontageMaker;
import ij.process.ImageProcessor;
import mmcorej.CMMCore;
import mmcorej.MMCoreJ;
import mmcorej.TaggedImage;
import org.micromanager.MenuPlugin;
import org.micromanager.Studio;

//import org.micromanager.api.DisplayWindow;
//import org.micromanager.display.InspectorPanel;
//import org.micromanager.display.InspectorPlugin;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.*;
import org.micromanager.display.DisplayWindow;
import org.micromanager.events.AcquisitionEndedEvent;
import org.micromanager.events.AcquisitionStartedEvent;
import org.micromanager.events.NewDisplayEvent;
import org.micromanager.internal.utils.imageanalysis.ImageUtils;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;

import java.awt.peer.ComponentPeer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * More or less boiler plate code to become a Micro-Manager 2.0 plugin
 * Most of the action happens in the CVViewer class
 * @author nico
 */
@Plugin(type = MenuPlugin.class)
public class MMSIMPlugin implements MenuPlugin, SciJavaPlugin {

   private Studio studio_;
   static public final String VERSION_INFO = "0.0.1";
   static private final String COPYRIGHT_NOTICE = "Copyright by UCSF, 2015-2017";
   static private final String DESCRIPTION = "MMSIMPlugin";
   static private final String NAME = "MMSIMPlugin";

   @Override
   public void setContext(Studio studio) {
      studio_ = studio;
   }

   @Override
   public String getSubMenu() {
      return "";
   }

   @Override
   public void onPluginSelected() {
         try {
            //Add GUI Here
            Runnable runnable = new runnable(studio_);
            studio_.events().registerForEvents(runnable);
            studio_.acquisitions().attachRunnable(-1,-1,-1,-1, runnable);
            System.out.println(studio_);
//            SIMode simmode = new SIMode(studio_);
//            studio_.events().registerForEvents(simmode);
         } catch (Exception ex) {
            if (studio_ != null) {
               studio_.logs().logError(ex);
            }
         }
      }

   //   }
//}
   @Override
   public String getCopyright() {
      return COPYRIGHT_NOTICE;
   }

   @Override
   public String getHelpText() {
      return DESCRIPTION;
   }

   @Override
   public String getName() {
      return NAME;
   }

   @Override
   public String getVersion() {
      return VERSION_INFO;
   }


}

class runnable implements Runnable {
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

//   private ImageProcessor montage_ip;

   public runnable(Studio studio) {
      studio_ = studio;
      mmc = studio_.core();
      ij_converter = studio_.data().getImageJConverter();
      montager = new MontageMaker();
   }

//   private ImagePlus getMontage() throws Exception {
////      ImageProcessor current_image_processor = ij_converter.createProcessor(current_image);
////         a = current_image;
//
////      return montage;
//
////         datastore_.putImage(montage_image);
//   }

   @Override
   public void run() {
      try {
         TaggedImage tImg;
         //         ImageUtils imageutils = new ImageUtils();
//         System.out.println("Runnable");
         sim_stack = new ImageStack( 512,  512);
//      stack.addSlice(current_image_processor);
         studio_.core().startSequenceAcquisition(SIMMAGES-1, 0, false);
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

   @Subscribe
   public void onAcquisitionStarted(AcquisitionStartedEvent e) {
      datastore_ = e.getDatastore();
      datastore_.registerForEvents(this);
      System.out.println("AcquisitionStartedEvent");
      SequenceSettings settings_ = studio_.acquisitions().getAcquisitionSettings();
      SummaryMetadata summarymetadata = studio_.acquisitions().generateSummaryMetadata();
      mda_montage = studio_.data().createRAMDatastore();
      mda_montage_display = studio_.getDisplayManager().createDisplay(mda_montage);

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
////      studio_.acquisitions().
      try {
         ImageProcessor current_image_processor = ij_converter.createProcessor(current_image);
         sim_stack.addSlice(current_image_processor);
         ImagePlus sim_stack_plus = new ImagePlus("Stack", sim_stack);
         montage = montager.makeMontage2(sim_stack_plus, SIMMAGES/3, SIMMAGES/3, 1.00, 1, SIMMAGES, 1, 0, false);
         montage_image = ij_converter.createImage(montage.getProcessor(), mda_coords, metadata);
         mda_montage.putImage(montage_image);
      } catch (IOException ex) {
         ex.printStackTrace();
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
}
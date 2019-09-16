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
import org.micromanager.AutofocusPlugin;
import java.text.ParseException;
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
import org.micromanager.internal.utils.AutofocusBase;
import org.micromanager.internal.utils.MMException;
import org.micromanager.internal.utils.NumberUtils;
import org.micromanager.internal.utils.PropertyItem;
import org.micromanager.internal.utils.imageanalysis.ImageUtils;
import org.micromanager.quickaccess.SimpleButtonPlugin;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;

import javax.swing.*;
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

   static final private String SIM_IMAGES = "SIM Images";
   static final private String SIM_MODE = "SIM Mode";
   static final private String MONTAGE_ROW = "Montage Rows";
   static final private String MONTAGE_COLUMN = "Montage Columns";

   private int simmode = 0;
   private int simages = 9;
   private int montage_rows = 3;
   private int montage_columns = 3;
   private Runnable runnable;


//   MMSIMPlugin() {
////      this.createProperty(SIM_MODE, NumberUtils.intToDisplayString(simmode));
////      super.createProperty(SIM_IMAGES, NumberUtils.intToDisplayString(simages));
////      super.createProperty(MONTAGE_ROW, NumberUtils.intToDisplayString(montage_rows));
////      super.createProperty(MONTAGE_COLUMN, NumberUtils.intToDisplayString(montage_columns));
//
//   }

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
         new simGUI(studio_);
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
//
//   @Override
//   public void applySettings() {
//      try {
//         simmode = NumberUtils.displayStringToInt(getPropertyValue(SIM_MODE));
//         simages = NumberUtils.displayStringToInt(getPropertyValue(SIM_IMAGES));
//         montage_rows = NumberUtils.displayStringToInt(getPropertyValue(MONTAGE_ROW));
//         montage_columns = NumberUtils.displayStringToInt(getPropertyValue(MONTAGE_COLUMN));
//         if(simmode==1){
//            simRunnable = new simRunnable(studio_);
//            studio_.events().registerForEvents(simRunnable);
//            studio_.acquisitions().attachRunnable(-1, -1, -1, -1, simRunnable);
//         }else{
//            simRunnable =null;
//         }
//
////         cropFactor = clip(0.01, cropFactor, 1.0);
////         channel = getPropertyValue(CHANNEL);
////         exposure = NumberUtils.displayStringToDouble(getPropertyValue(EXPOSURE));
////         fftLowerCutoff = NumberUtils.displayStringToDouble(getPropertyValue(FFT_LOWER_CUTOFF));
////         fftLowerCutoff = clip(0.0, fftLowerCutoff, 100.0);
////         fftUpperCutoff = NumberUtils.displayStringToDouble(getPropertyValue(FFT_UPPER_CUTOFF));
////         fftUpperCutoff = clip(0.0, fftUpperCutoff, 100.0);
////         show = getPropertyValue(SHOW_IMAGES);
////         scoringMethod = getPropertyValue(SCORING_METHOD);
//
//      } catch (MMException | ParseException ex) {
//         studio_.logs().logError(ex);
//      }
//   }
//   @Override
//   public void saveSettings() {
//
//   }

}


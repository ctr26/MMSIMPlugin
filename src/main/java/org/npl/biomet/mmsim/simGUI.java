package org.npl.biomet.mmsim;

import com.google.common.eventbus.Subscribe;
import ij.ImagePlus;
import ij.ImageStack;
import ij.plugin.MontageMaker;
import ij.process.ImageProcessor;
import mmcorej.CMMCore;
import mmcorej.TaggedImage;
import org.micromanager.PropertyMap;
import org.micromanager.Studio;
import org.micromanager.UserProfile;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.*;
import org.micromanager.data.Image;
import org.micromanager.display.DisplayWindow;
import org.micromanager.events.AcquisitionEndedEvent;
import org.micromanager.events.AcquisitionStartedEvent;
import org.micromanager.internal.utils.imageanalysis.ImageUtils;
import org.micromanager.propertymap.MutablePropertyMapView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class simGUI extends javax.swing.JFrame implements Runnable {
	private final UserProfile user_profile;
	private final Studio studio_;
	int[] simProperties = new int[3];
	//DEFAULTS
	int SIMages = 9;
	int SIM_ROWS = 3;
	int SIM_COLS = 3;

//	private final Studio studio_;
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

	private JToggleButton liveModeButton;
	String liveModeButtonOnText = "Stop SIM Mode";
	String liveModeButtonOffText = "SIM Mode";
	private JComboBox modeComboBox;
	private JFormattedTextField SIMimagesTextField;
	private JFormattedTextField montageRowsTextField;
	private JFormattedTextField montageColsTextField;
	private JToggleButton buttonSimMode;
	private JPanel topPanel;
	private JComboBox optionMode;
	private JPanel masterPanel;
	private JPanel simagesPanel;
	private JPanel montageRowsPanel;
	private JPanel montageColsPanel;

	String comboItems[] = {"Montage mode", "MM Automatic"};


	public simGUI(Studio studio) {
		// Build GUI
		studio_ = studio;
		mmc = studio_.core();
		ij_converter = studio_.data().getImageJConverter();
		montager = new MontageMaker();
		user_profile = studio_.profile();
		main();


//		Runnable runnable = new simRunnable(studio_);
	}
	private void main(){
		getUserData(user_profile);
		initComponents();
	}

	private void getUserData(UserProfile user_profile) {
		MutablePropertyMapView propertyMap = user_profile.getSettings(user_profile.getClass());

		simProperties = propertyMap.getIntegerList("simProperties");

		int a = simProperties.length;
		if(simProperties.length == 0){
			System.out.println("Did not find defaults");
			simProperties = new int[]{SIMages,SIM_ROWS,SIM_COLS};
			updateUserData(user_profile,simProperties);
//			return;
		}
		else {
			System.out.println("Loading defaults");
			SIMages = simProperties[0];
			SIM_ROWS = simProperties[1];
			SIM_COLS = simProperties[2];
			System.out.println(simProperties.toString());
		}
	}

	void updateUserData(UserProfile user_profile,int[] simProperties){
		MutablePropertyMapView propertyMap = user_profile.getSettings(user_profile.getClass());

		propertyMap.putIntegerList("simProperties",simProperties);
		int[] updatedSIMProperties = propertyMap.getIntegerList("simProperties",simProperties);
		System.out.println(updatedSIMProperties.toString());
	}

	void initComponents(){
//		new JFrame();
		Dimension panelSize = masterPanel.getPreferredSize();

		this.setBounds(100, 100, (int) panelSize.getWidth(), (int) panelSize.getHeight());
		this.setTitle("SIM");
		this.setContentPane(masterPanel);

//		this.setBounds(masterPanel.getBounds());
//		this.setBounds(masterPanel.getBounds());
//		this.setDefaultCloseOperation();
		this.setVisible(true);
//		this.getContentPane().setLayout(null);

		ActionListener activeButtonListener = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				activeButton(e);
			}
		};

//		liveModeButton = new JToggleButton(liveModeButtonOffText);
//		liveModeButton.addActionListener(activeButtonListener);

		ActionListener settingsListener = new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				guiSettingsChange(e);
			}
		};

//		modeComboBox = new JComboBox(comboItems);
		//TODO Save mode

		NumberFormat format = NumberFormat.getNumberInstance();
//
//		SIMimagesTextField = new JFormattedTextField(format);
//			SIMimagesTextField.setValue(SIMages);
//			SIMimagesTextField.addActionListener(settingsListener);
//
//		montageRowsTextField = new javax.swing.JFormattedTextField(format);
//			montageRowsTextField.setValue(SIM_ROWS);
//			montageRowsTextField.addActionListener(settingsListener);
//
//		montageColsTextField = new javax.swing.JFormattedTextField(format);
//			montageColsTextField.setValue(SIM_COLS);
//			montageColsTextField.addActionListener(settingsListener);
//

//		masterPanel = new JPanel();
//		simagesPanel = new JPanel();
//		montageRowsPanel = new JPanel();
//		montageColsPanel = new JPanel();
//
//		masterPanel.add(simagesPanel);
//		masterPanel.add(montageRowsPanel);
//		masterPanel.add(montageColsPanel);
//		masterPanel.add(modeComboBox);
//		masterPanel.add(liveModeButton);
//
//		simagesPanel.add(SIMimagesTextField);
//		montageRowsPanel.add(montageRowsTextField);
//		montageColsPanel.add(montageColsTextField);

		this.setContentPane(masterPanel);
		this.setVisible(true);
	}

	private void cleanup() {
	}

	private void activeButton(java.awt.event.ActionEvent e) {
//		e.
		if(liveModeButton.isSelected()){
			liveModeButton.setText(liveModeButtonOnText);
			studio_.events().registerForEvents(this);
			studio_.acquisitions().attachRunnable(-1, -1, -1, -1, this);
		}
		if(!liveModeButton.isSelected()){
			liveModeButton.setText(liveModeButtonOffText);
			studio_.events().registerForEvents(this);
			studio_.acquisitions().attachRunnable(-1, -1, -1, -1, this);
		}
	}

	public void guiSettingsChange(java.awt.event.ActionEvent e){
//		SIMages = SIMimagesTextField.getText()

	}

	@Override
	public void run() {
		try {
			TaggedImage tImg;
			//         ImageUtils imageutils = new ImageUtils();
//         System.out.println("Runnable");
			//%TODO FIX WITHS AND HEIGHTS
			sim_stack = new ImageStack( 512,  512);
//      stack.addSlice(current_image_processor);
			studio_.core().startSequenceAcquisition(SIMages-1, 0, false);
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
			montage = montager.makeMontage2(sim_stack_plus, SIM_COLS, SIM_ROWS, 1.00, 1, SIMages, 1, 0, false);
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

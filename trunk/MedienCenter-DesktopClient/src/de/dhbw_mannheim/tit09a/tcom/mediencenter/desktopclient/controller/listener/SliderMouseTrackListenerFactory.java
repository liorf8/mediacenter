package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.plaf.basic.BasicSliderUI.TrackListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.VLCController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class SliderMouseTrackListenerFactory
{
    private static BasicSliderUI basicSliderUI;

    private static Map<JSlider, TrackListener> slidersTrackListener = new HashMap<JSlider, TrackListener>();

    // creates if not already there and returns a TrackListener for the specified JSlider
    public static TrackListener getTrackListenerInstance(JSlider jslider)
    {
	TrackListener trackListener = slidersTrackListener.get(jslider);

	if (trackListener == null)
	{
	    // System.out.println("SliderTrackListenerFactory: builded new TrackListener for " +jslider);
	    basicSliderUI = new BasicSliderUI(jslider);
	    trackListener = basicSliderUI.new TrackListener()
	    {
		public void mouseDragged(MouseEvent e)
		{
		    JSlider source = (JSlider) e.getSource();
		    int percent = source.getValue();
		    System.out.println("MouseDraggedTo:" + percent);
		    VLCController.getInstance(true).setPosition(
			    (float) percent / MainFrame.TIME_SLIDE_MAX);
		}

		public void mouseMoved(MouseEvent e)
		{
		   
		}

		public void mousePressed(MouseEvent e)
		{
		    
		}

		public void mouseClicked(MouseEvent e)
		{
		    
		}

		public void mouseEntered(MouseEvent e)
		{
		    
		}

		public void mouseExited(MouseEvent e)
		{
		   
		}

	    };

	    slidersTrackListener.put(jslider, trackListener);
	}

	return trackListener;
    }
}

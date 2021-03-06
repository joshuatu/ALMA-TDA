/*
 *     ALMA TDA - Contour tree based simplification and visualization for ALMA
 *     data cubes.
 *     Copyright (C) 2016 PAUL ROSEN
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     You may contact the Paul Rosen at <prosen@usf.edu>.
 */
package usf.saav.alma.util;

import java.util.Vector;

import usf.saav.common.Callback;
import usf.saav.common.monitor.MonitoredDouble;
import usf.saav.common.monitor.MonitoredInteger;
import usf.saav.common.mvc.ControllerComponent;

// TODO: Auto-generated Javadoc
/**
 * The Class CoordinateSystemController.
 */
public class CoordinateSystemController extends ControllerComponent.Default implements ControllerComponent, CoordinateSystem {

	private int prevMouseX = 0, prevMouseY = 0;
	private boolean dragOn = false;

	private int tx = 0, ty = 0;
	private MonitoredInteger x;
	private MonitoredInteger y;
	private MonitoredDouble zoom;

	/**
	 * Instantiates a new coordinate system controller.
	 *
	 * @param x0 the x 0
	 * @param y0 the y 0
	 * @param zoom the zoom
	 */
	public CoordinateSystemController( MonitoredInteger x0, MonitoredInteger y0, MonitoredDouble zoom ){
		super( false );

		this.x = x0;
		this.y = y0;
		this.zoom = zoom;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX( ){
		return x.get()+tx;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY( ){
		return y.get()+ty;
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.util.CoordinateSystem#getCoordinateSystemPosition(float, float)
	 */
	public float [] getCoordinateSystemPosition( float wx, float wy ){
		//System.out.println( Arrays.toString(this.getPosition()) );
		float csx = x.get() + (wx - (float)tx - (float)winX.start() - (float)winX.length()/2) / (float)zoom.get();
		float csy = y.get() + (wy - (float)ty - (float)winY.start() - (float)winY.length()/2) / (float)zoom.get();
		return new float[]{csx,csy};
	}

	/* (non-Javadoc)
	 * @see usf.saav.alma.util.CoordinateSystem#getWindowPosition(float, float)
	 */
	public float [] getWindowPosition( float csx, float csy ){
		//System.out.println( csx + " " + csy );
		//System.out.println( Arrays.toString(this.getPosition()) );
		float wx = winX.middle() + (float) ((csx-x.get())*zoom.get()) + tx;
		float wy = winY.middle() + (float) ((csy-y.get())*zoom.get()) + ty;
		return new float[]{wx,wy};
	}

	Vector<Callback> dragCallbacks = new Vector<Callback>( );
	Vector<Callback> zoomCallbacks = new Vector<Callback>( );
	Vector<Callback> releaseCallbacks = new Vector<Callback>( );


	public void addDragCallback( Object obj, String func_name ){
		try {
			dragCallbacks.add( new Callback(obj, func_name, int.class, int.class ) );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	

	public void addZoomCallback( Object obj, String func_name ){
		try {
			zoomCallbacks.add( new Callback(obj, func_name, float.class ) );
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public boolean mousePressed( int mouseX, int mouseY ) {
		if( !isEnabled() ) return false;
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) ) return false;

		dragOn = true;

		prevMouseX = mouseX;
		prevMouseY = mouseY;

		return true;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mouseDragged(int, int)
	 */
	@Override
	public boolean mouseDragged( int mouseX, int mouseY ) {
		if( !isEnabled() ) return false;
		if( !dragOn ) return false;

		int dX = mouseX-prevMouseX;
		int dY = mouseY-prevMouseY;

		prevMouseX = mouseX;
		prevMouseY = mouseY;

		this.tx += dX;
		this.ty += dY;

		for( Callback c : dragCallbacks ){
			c.call( tx, ty );
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mouseReleased()
	 */
	@Override
	public boolean mouseReleased() {
		if( !isEnabled() ) return false;
		if( !dragOn ) return false;

		dragOn = false;

		x.set( (int) (x.get()-tx/zoom.get()) );
		y.set( (int) (y.get()-ty/zoom.get()) );

		this.tx = 0;
		this.ty = 0;
		
		for( Callback c : releaseCallbacks ){
			c.call( tx, ty );
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see usf.saav.common.mvc.ControllerComponent.Default#mouseWheel(int, int, float)
	 */
	@Override
	public boolean mouseWheel(int mouseX, int mouseY, float count) {
		if( !isEnabled() ) return false;
		if( !winX.inRange(mouseX) || !winY.inRange(mouseY) ) return false;

		zoom.set( zoom.get()*Math.pow(0.9, count) );

		for( Callback c : zoomCallbacks ){
			c.call( (float)Math.pow(0.9, count) );
		}

		return true;
	}

}

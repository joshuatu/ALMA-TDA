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
package usf.saav.common.mvc;

import usf.saav.common.BasicObject;

public interface BasicComponent {
	
	public void setup( );
	public void update( );
		
	public void enable( );
	public void disable( );
	public void setEnabled( boolean enb );
	public boolean isEnabled( );

	public class Default extends BasicObject implements BasicComponent {

		private boolean enabled = true; 
		
		protected Default( ){ }
		protected Default(boolean verbose) { super(verbose); }
		
		@Override public void setup( ){ }
		@Override public void update( ){ }

		public void enable( ){ enabled = true; }
		public void disable( ){ enabled = false; }
		public void setEnabled( boolean enb ){ enabled = enb; }
		public boolean isEnabled( ){ return enabled; }

	}
}

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/* A class to implement sensors:
 * Range finders return distance
 * Adjacent Agent sensors return relative angles, distances
 * Pie slice sensors (radars) return activation levels
 * 
 */
public class CSensor{

	public static final boolean ON = true;
	public static final boolean OFF = false;
	
	// Range finder lines
	public Line L_feeler1 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	public Line L_feeler2 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	public Line L_feeler3 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	
	// Empty vector to use
	public Vector2f vector = new Vector2f();
	
	// Turn sensors on and off
	public boolean m_bl_rangeFinder = OFF;
	public boolean m_bl_agentSensor = OFF;
	public boolean m_bl_radar = OFF;
	
	// Range finder
	public int distance = 0;
	public double f_theta = 0.0;
	public double f_theta2 = 0.0;
	public float f_thetaDiff = 45.0f;
	public Vector2f v2f_intersect1 = new Vector2f(-10.0f,-10.0f);
	public Vector2f v2f_intersect2 = new Vector2f(-10.0f,-10.0f);
	private boolean dbintersectFlag = false;
	private int i_feeler1Count = 0;
	private int i_feeler2Count = 0;
	private int i_feeler3Count = 0;
	
	// Pie slice sensor
	public ArrayList<Integer> enemyAgtIDarrayFront = new ArrayList<Integer>();
	public ArrayList<Integer> enemyAgtIDarrayRight = new ArrayList<Integer>();
	public ArrayList<Integer> enemyAgtIDarrayRear = new ArrayList<Integer>();
	public ArrayList<Integer> enemyAgtIDarrayLeft = new ArrayList<Integer>();
	private float f_pieTempX = 0;
	private float f_pieTempY = 0;
	private Line L_upRightLine = new Line(0,0,0,0);
	private Line L_lowRightLine = new Line(0,0,0,0);
	private Line L_lowLeftLine = new Line(0,0,0,0);
	private Line L_upLeftLine = new Line(0,0,0,0);
	
	// Pie line length
	private int i_pieRearLineLength = 230;
	private int i_pieForwardLineLength = 250;
	private float f_pieThetaDiff = 0.7853981634f;
	
	// Polygon  
	private	Shape frontPie = new Polygon();
	private	Shape rightPie = new Polygon(); 
	private	Shape rearPie = new Polygon();
	private	Shape leftPie = new Polygon();
	
	// Activation Level
	private int frontActLevel = 0;
	private int rightActLevel = 0;
	private int rearActLevel = 0;
	private int leftActLevel = 0;
	//private int totalActLevel = 0;
	private String[] sa_ActLevelMessage = {"0", "Low", "Medium", "High", "High"};	// Activation Level Strings
	// Pointer to the entity
	public CEntity C_entity;
	
	// Access to drawing space
	Graphics g;
	
	// Others
	public int directionX;
	public int directionY;
	public int lineMultiplier = 60;
	private float f_tempX;
	private float f_tempY;
	private float f_tempX2;
	private float f_tempY2;
	private float f_standardFeelerLength = 120;
	private float f_centerFeelerLength = 120;
	private float f_leftFeelerLength = 120;
	private float f_rightFeelerLength = 120;
	
	private float f_centerFeelerDist = 0.0f;
	private float f_leftFeelDist = 0.0f;
	private float f_rightFeelDist = 0.0f;
	private float f_tempDist1 = -1.0f;
	private float f_tempDist2 = -1.0f;
	private float f_centerCloseDist = -1.0f;
	private float f_leftCloseDist = -1.0f;
	private float f_rightCloseDist = -1.0f;
	private boolean intersectFlag;
	
	
	
	/* Constructor for a sensor
	 * @param boolean bl_range  turns the range finder (wall sensor) on/off
	 * @param boolean bl_agent	in main
	 * @param boolean bl_radar	in main
	 * @param CEntity entity	An instance of the entity class
	 */
	public CSensor(boolean bl_range, boolean bl_agent, boolean bl_radar, CEntity entity) {
		
		this.rangeFinderOnOff(bl_range);
		this.agentSensorOnOff(bl_agent);
		this.pieSliceSensorOnOff(bl_radar);
		this.C_entity = entity;
		
	}
	
	// Run any active sensors
	public void sensorUpdate(float theta, Vector2f m_V2f_position, Vector2f m_V2f_velocity){
		// Run range finder method
		if(m_bl_rangeFinder == ON){
			
			// method to do range stuff
			rangeFinder(theta,  m_V2f_position, m_V2f_velocity);
		}
		
		// Run agent sensor method
		if(m_bl_agentSensor == ON){
			// Do agent sensing
			agentSensor();
		}
		
		// Run pie slice sensor method
		if(m_bl_radar == ON){
			// Do radar
			pieSliceSensor();
			//System.out.println("Pie Slice Sensor / Radar sensor -Active");
		}
	}
	
	// Draw sensor lines
	public void sensorRender(Graphics g){
		
		// Render range sensor
		if(m_bl_rangeFinder == ON){
			
			// Draw feeler colors
			g.setColor(new Color(0, 0, 0));  //g.setColor(new Color(27, 224, 27)); // Green feeler
			g.draw(L_feeler1);	
			
			//g.setColor(new Color(27, 27, 224)); // Blue feeler
			g.draw(L_feeler2);
			
			//g.setColor(new Color(224, 27, 27)); // Red feeler
			g.draw(L_feeler3);
			
			// Display feeler output
			g.setColor(new Color(0, 0, 0));
			
			
			if(f_centerFeelerDist < 0){
				 f_centerFeelerDist = 0;
			}
			if(f_leftFeelDist < 0){
				f_leftFeelDist = 0;
			}
			if(f_rightFeelDist < 0){
				f_rightFeelDist = 0;
			}
			g.drawString("Center Feeler distance: " + (f_centerFeelerDist), 10, 50);																				  
			g.drawString("Left   Feeler distance: " + (f_leftFeelDist), 10, 65);							  
			g.drawString("Right  Feeler distance: " + (f_rightFeelDist), 10, 80);
			

		}
		
		// Render agent sensor
		if(m_bl_agentSensor == ON){
	
		}
		
		// Render pie slice sensor
		if(m_bl_radar == ON){
		
			g.setColor(Color.red);
			g.draw(L_upRightLine);
			//System.out.println("Draw UPPER right line");
			
			g.setColor(Color.black);
			g.draw(L_lowRightLine);
			
			g.setColor(Color.green);
			g.draw(L_lowLeftLine);
			
			g.setColor(Color.blue);
			g.draw(L_upLeftLine);
			
			// Draw polygons for object detection -- FORWARD RADAR
			float[] frontPoints = {f_pieTempX, f_pieTempY, L_upLeftLine.getEnd().x, L_upLeftLine.getEnd().y, 
					L_upRightLine.getEnd().x, L_upRightLine.getEnd().y};
			
			frontPie = new Polygon(frontPoints);
			g.setColor(Color.red);
			g.draw(frontPie);
			
			
			// Draw polygons for object detection -- RIGHT RADAR
			float[] rightPoints = {f_pieTempX, f_pieTempY, L_upRightLine.getEnd().x, L_upRightLine.getEnd().y, 
					L_lowRightLine.getEnd().x, L_lowRightLine.getEnd().y};

			rightPie = new Polygon(rightPoints);
			g.setColor(Color.green);
			g.draw(rightPie);
			
			
			// Draw polygons for object detection -- REAR RADAR
			float[] rearPoints = {f_pieTempX, f_pieTempY, L_lowRightLine.getEnd().x, L_lowRightLine.getEnd().y, 
					L_lowLeftLine.getEnd().x, L_lowLeftLine.getEnd().y};

			rearPie = new Polygon(rearPoints);
			g.setColor(Color.blue);
			g.draw(rearPie);
			
			
			// Draw polygons for object detection -- LEFT RADAR
			float[] leftPoints = {f_pieTempX, f_pieTempY, L_lowLeftLine.getEnd().x, L_lowLeftLine.getEnd().y, 
					L_upLeftLine.getEnd().x, L_upLeftLine.getEnd().y};

			leftPie = new Polygon(leftPoints);
			g.setColor(Color.orange);
						g.draw(leftPie);
			
			
			
			// Render each pie's activation level
			
			g.setColor(Color.black);
			//totalActLevel = frontActLevel + rightActLevel + rearActLevel + leftActLevel;
			// Front
			if(frontActLevel <= 3){
				g.drawString("Front Pie Activation : " + sa_ActLevelMessage[frontActLevel], 10, 110);
			}
			if(frontActLevel > 3){
				g.drawString("Front Pie Activation : " + "High", 10, 110);
			}
			
			// Right
			if(rightActLevel <= 3){
				g.drawString("Right Pie Activation : " + sa_ActLevelMessage[rightActLevel], 10, 125);
			}
			if(rightActLevel > 3){
				g.drawString("Right Pie Activation : " + "High", 10, 125);
			}
			
			// Rear
			if(rearActLevel <= 3){
				g.drawString("Rear Pie Activation : " + sa_ActLevelMessage[rearActLevel], 10, 155);
			}
			if(rearActLevel > 3){
				g.drawString("Rear Pie Activation : " + "High", 10, 155);
			}
			
			// Left
			if(leftActLevel <= 3){
				g.drawString("Left Pie Activation : " + sa_ActLevelMessage[leftActLevel], 10, 140);
			}
			if(leftActLevel > 3){
				g.drawString("Left Pie Activation : " + "High", 10, 140);
			}
			
			
			
			
			
		}
		
		
	} // end render
	
	 
	// Range finder
	public void rangeFinder(float theta, Vector2f m_V2f_position, Vector2f m_V2f_velocity ){
		
		
		// Store entity's location and current rotation angle for reference		
		f_tempX =  (m_V2f_position.x + 20); //C_entity.getXposition(); 
		f_tempY =  (m_V2f_position.y + 50); //C_entity.getYposition(); 
		f_theta = (float)(Math.toRadians(C_entity.getImagePointer().getRotation()));
		
		// 45 degree adjustment to feelers
		f_thetaDiff = (float)Math.toRadians(Math.toDegrees(f_thetaDiff));
		
		//System.out.println("center of rotation y : " + C_entity.getImagePointer().getCenterOfRotationY());
		
		// Current x position = x + v * cos(theta) v is set to sensor length of 80
		L_feeler1.setLocation(f_tempX, f_tempY);	L_feeler2.setLocation(f_tempX, f_tempY);	L_feeler3.setLocation(f_tempX, f_tempY);
		
		//System.out.println("RANGE FINDER " + "  F_theta " + f_theta + "  Diff " + f_thetaDiff + "Cos " + (float)(f_tempX * f_centerFeelerLength * Math.cos(f_theta)));
		
		// Project feelers at start heading (theta = 0.0)
		if(f_theta == 0.0f){
			L_feeler1.set(f_tempX, f_tempY, f_tempX, f_tempY - f_centerFeelerLength);
			
			L_feeler2.set(f_tempX, f_tempY, f_tempX + ( (float)((f_rightFeelerLength * Math.sin(f_thetaDiff))) ),
											f_tempY - ( (float)(f_rightFeelerLength * Math.cos(f_thetaDiff))) ); // feelerLength * tan(theta) = y coord
			
			L_feeler3.set(f_tempX, f_tempY, f_tempX - ( (float)((f_leftFeelerLength * Math.sin(f_thetaDiff))) ),
											f_tempY - ( (float)(f_leftFeelerLength * Math.cos(f_thetaDiff))) );
			
			//System.out.println("Feelers");
		}
		// Project feelers at all other angles
		else{
			L_feeler1.set(f_tempX, f_tempY, f_tempX + ( (float)((f_centerFeelerLength * Math.sin(f_theta))) ),
											f_tempY - ( (float)(f_centerFeelerLength * Math.cos(f_theta))) );
			
			L_feeler2.set(f_tempX, f_tempY, (f_tempX + ( (float)((f_rightFeelerLength * Math.sin(f_theta + f_thetaDiff))))),
											(f_tempY - ( (float)(f_rightFeelerLength * Math.cos(f_theta + f_thetaDiff)))));
			
			L_feeler3.set(f_tempX, f_tempY, f_tempX + ( (float)((f_leftFeelerLength * Math.sin(f_theta - f_thetaDiff)))),
											f_tempY - ( (float)(f_leftFeelerLength * Math.cos(f_theta - f_thetaDiff))));
			
		}
		
		
			// FEELER 1
		
		
		// Copy current vehicle positioning to check for movement
		if(f_tempX2 == -1.0f | f_tempY2 == -1.0f | f_theta2 == -1.0){
			f_tempX2 = f_tempX;		f_tempY2 =  f_tempY; 	f_theta2 = f_theta;
		}

		// Range finding -- find closest intersection
		for(Line wall : Main.lineArray){

			// Feeler intersects with a wall
			if( L_feeler1.intersects(wall) ){

				intersectFlag = true;
				//System.out.println("INTERSECTION");
				// Look for any double intersections and take the closest one
				if(v2f_intersect1.x == -10.0f){

					// Get an intersection
					L_feeler1.intersect(wall, ON, v2f_intersect1);
					//System.out.println("v_intersect1");
				}
				else if(v2f_intersect2.x == -10.0){
					L_feeler1.intersect(wall, ON, v2f_intersect2);
					//System.out.println("v_intersect2");
				}

				// There are two walls intersected, use the POI with shortest distance
				if(v2f_intersect1.x != -10.0f && v2f_intersect2.x != -10.f){

					// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
					f_tempDist1 = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					f_tempDist2 = (float)Math.sqrt( ((v2f_intersect2.x - f_tempX) * (v2f_intersect2.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect2.y - f_tempY) * (v2f_intersect2.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					// Keep the shortest distance
					if(f_tempDist1 < f_tempDist2){
						f_centerCloseDist = f_tempDist1; 
					}
					else{
						f_centerCloseDist = f_tempDist2;
					}

					// Reset feeler length to reflect distance to point of intersection
					if( f_centerFeelerLength > f_centerCloseDist ){
						f_centerFeelerLength = f_centerCloseDist - 1;

						//System.out.println("Shorten feeler to intersection - double intersection");
					}

					// Send distance to display
					f_centerFeelerDist = f_centerCloseDist - 52;	// f_centerFeelerDist - 50 to account for image center

					v2f_intersect1.set(-10.0f, -10.0f);
					v2f_intersect2.set(-10.0f, -10.0f);
					intersectFlag = false;  // prevent any more distance calculations outside of this *if*.
					dbintersectFlag = true;
					//System.out.println("Double intersection");

				} // end find closest intersection	

			} // end big *if*

		} // end for loop

		// Only one wall intersected, compute distance
		if(v2f_intersect1.x != -10.0f && intersectFlag){

			// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
			f_centerCloseDist = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
					((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

			//System.out.println("Only 1 intersection");

		}



		// Reset feeler length to reflect distance to point of intersection
		if( intersectFlag && (f_centerFeelerLength > f_centerCloseDist) ){
			f_centerFeelerLength = f_centerCloseDist - 1;

			// Send distance to display
			f_centerFeelerDist = f_centerCloseDist - 51;	// f_centerFeelerDist - 50 to account for image center

		}
		// No intersection and vehicle has moved,  redraw feeler to normal length if it is less than normal
		//else{
		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false){

			if(f_centerFeelerLength < f_standardFeelerLength ){
				f_centerFeelerLength = f_standardFeelerLength;
				//System.out.println("Re-extend feeler");
			}
			f_tempX2 = -1.0f;	f_tempY2 = -1.0f; 	f_theta2 = -1.0;
		}

		//}

		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false && dbintersectFlag == false){

			i_feeler1Count += 1;
			if(i_feeler1Count > 10){
				// Zero out the display
				f_centerFeelerDist = 0.0f;
				i_feeler1Count = 0;
			}
		}


		// Reinitialize values
		f_tempDist1 = -1.0f;	f_tempDist2 = -1.0f;	f_centerCloseDist = -1.0f;
		v2f_intersect1.set(-10.0f, -10.0f);
		v2f_intersect2.set(-10.0f, -10.0f);
		intersectFlag = false;
		dbintersectFlag = false;

		
		
		
			// FEELER 2

		
		
		
		// Range finding -- find closest intersection
		for(Line wall : Main.lineArray){

			// Feeler intersects with a wall
			if( L_feeler2.intersects(wall) ){

				intersectFlag = true;
				//System.out.println("feeler2_INTERSECTION");
				// Look for any double intersections and take the closest one
				if(v2f_intersect1.x == -10.0f){

					// Get an intersection
					L_feeler2.intersect(wall, ON, v2f_intersect1);
					//System.out.println("feeler2_v_intersect1");
				}
				else if(v2f_intersect2.x == -10.0){
					L_feeler2.intersect(wall, ON, v2f_intersect2);
					//System.out.println("feeler2_v_intersect2");
				}

				// There are two walls intersected, use the POI with shortest distance
				if(v2f_intersect1.x != -10.0f && v2f_intersect2.x != -10.f){

					// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
					f_tempDist1 = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					f_tempDist2 = (float)Math.sqrt( ((v2f_intersect2.x - f_tempX) * (v2f_intersect2.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect2.y - f_tempY) * (v2f_intersect2.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					// Keep the shortest distance
					if(f_tempDist1 < f_tempDist2){
						f_rightCloseDist = f_tempDist1; 
					}
					else{
						f_rightCloseDist = f_tempDist2;
					}

					// Reset feeler length to reflect distance to point of intersection
					if( f_rightFeelerLength > f_rightCloseDist ){
						f_rightFeelerLength = f_rightCloseDist - 1;

						//System.out.println("feeler2_Shorten feeler to intersection - double intersection");
					}

					// Send distance to display
					// Distance to side from known angle (45 deg) and length variable f_rightCloseDist
					f_rightFeelDist = (float)((Math.sin(f_thetaDiff) * f_rightCloseDist) - 20);
					//f_rightFeelDist = f_rightCloseDist - 5;	// f_centerFeelerDist - 50 to account for image center

					v2f_intersect1.set(-10.0f, -10.0f);
					v2f_intersect2.set(-10.0f, -10.0f);
					intersectFlag = false;  // prevent any more distance calculations outside of this *if*.
					dbintersectFlag = true;
					//System.out.println("feeler2_Double intersection");

				} // end find closest intersection	

			} // end big *if*

		} // end for loop

		// Only one wall intersected, compute distance
		if(v2f_intersect1.x != -10.0f && intersectFlag){

			// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
			f_rightCloseDist = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
					((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

			//System.out.println("feeler2_Only 1 intersection");

		}


		// Reset feeler length to reflect distance to point of intersection
		if( intersectFlag && (f_rightFeelerLength > f_rightCloseDist) ){
			
			f_rightFeelerLength = f_rightCloseDist - 1;

			// Send distance to display
			// Distance to side from known angle (45 deg) and length variable f_rightCloseDist
			f_rightFeelDist = (float)((Math.sin(f_thetaDiff) * f_rightCloseDist) - 18);

			//System.out.println("feeler2_Shorten feeler to intersection");
		}
		// No intersection and vehicle has moved,  redraw feeler to normal length if it is less than normal
		//else{
		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false){

			if(f_rightFeelerLength < f_standardFeelerLength ){
				f_rightFeelerLength = f_standardFeelerLength;
				//System.out.println("feeler2_Re-extend feeler");
			}
			f_tempX2 = -1.0f;	f_tempY2 = -1.0f; 	f_theta2 = -1.0;
		}

		//}

		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false && dbintersectFlag == false){

			i_feeler2Count += 1;
			if(i_feeler2Count > 10){
				// Zero out the display
				f_rightFeelDist = 0.0f;
				i_feeler2Count = 0;
			}
		}


		// Reinitialize values
		f_tempDist1 = -1.0f;	f_tempDist2 = -1.0f;	f_rightCloseDist = -1.0f;
		v2f_intersect1.set(-10.0f, -10.0f);
		v2f_intersect2.set(-10.0f, -10.0f);
		intersectFlag = false;
		dbintersectFlag = false;
	
		
		
		
		
		// FEELER 3





		// Range finding -- find closest intersection
		for(Line wall : Main.lineArray){

			// Feeler intersects with a wall
			if( L_feeler3.intersects(wall) ){

				intersectFlag = true;
				//System.out.println("feeler3_INTERSECTION");
				// Look for any double intersections and take the closest one
				if(v2f_intersect1.x == -10.0f){

					// Get an intersection
					L_feeler3.intersect(wall, ON, v2f_intersect1);
					//System.out.println("feeler3_v_intersect1");
				}
				else if(v2f_intersect2.x == -10.0){
					L_feeler3.intersect(wall, ON, v2f_intersect2);
					//System.out.println("feeler3_v_intersect2");
				}

				// There are two walls intersected, use the POI with shortest distance
				if(v2f_intersect1.x != -10.0f && v2f_intersect2.x != -10.f){

					// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
					f_tempDist1 = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					f_tempDist2 = (float)Math.sqrt( ((v2f_intersect2.x - f_tempX) * (v2f_intersect2.x - f_tempX)) +  // (x2 - x1)^2 + 
							((v2f_intersect2.y - f_tempY) * (v2f_intersect2.y - f_tempY)) ); //	(y2 - y1)^2 = distance

					// Keep the shortest distance
					if(f_tempDist1 < f_tempDist2){
						f_leftCloseDist = f_tempDist1; 
					}
					else{
						f_leftCloseDist = f_tempDist2;
					}

					// Reset feeler length to reflect distance to point of intersection
					if( f_leftFeelerLength > f_leftCloseDist ){
						f_leftFeelerLength = f_leftCloseDist - 1;

						//System.out.println("feeler3_Shorten feeler to intersection - double intersection");
					}

					// Send distance to display
					// Distance to side from known angle (45 deg) and length variable f_rightCloseDist
					f_leftFeelDist = (float)((Math.sin(f_thetaDiff) * f_leftCloseDist) - 20);
	
					v2f_intersect1.set(-10.0f, -10.0f);
					v2f_intersect2.set(-10.0f, -10.0f);
					intersectFlag = false;  // prevent any more distance calculations outside of this *if*.
					dbintersectFlag = true;
					//System.out.println("feeler3_Double intersection");

				} // end find closest intersection	

			} // end big *if*

		} // end for loop

		// Only one wall intersected, compute distance
		if(v2f_intersect1.x != -10.0f && intersectFlag){

			// Compute distance from point of intersection to line origin (agent). POI is X2 and Y2
			f_leftCloseDist = (float)Math.sqrt( ((v2f_intersect1.x - f_tempX) * (v2f_intersect1.x - f_tempX)) +  // (x2 - x1)^2 + 
					((v2f_intersect1.y - f_tempY) * (v2f_intersect1.y - f_tempY)) ); //	(y2 - y1)^2 = distance

			//System.out.println("feeler3_Only 1 intersection");

		}


		// Reset feeler length to reflect distance to point of intersection
		if( intersectFlag && (f_leftFeelerLength > f_leftCloseDist) ){

			f_leftFeelerLength = f_leftCloseDist - 1;

			// Send distance to display
			// Distance to side from known angle (45 deg) and length variable f_rightCloseDist
			f_leftFeelDist = (float)((Math.sin(f_thetaDiff) * f_leftCloseDist) - 18);

			//System.out.println("feeler3_Shorten feeler to intersection");
		}
		// No intersection and vehicle has moved,  redraw feeler to normal length if it is less than normal
		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false){

			if(f_leftFeelerLength < f_standardFeelerLength ){
				f_leftFeelerLength = f_standardFeelerLength;
				//System.out.println("feeler3_Re-extend feeler");
			}
			f_tempX2 = -1.0f;	f_tempY2 = -1.0f; 	f_theta2 = -1.0;
		}

		//}

		if( (f_tempX2 != f_tempX | f_tempY2 != f_tempY | f_theta2 != f_theta) && intersectFlag == false && dbintersectFlag == false){

			i_feeler3Count += 1;
			if(i_feeler3Count > 10){
				// Zero out the display
				f_leftFeelDist = 0.0f;
				i_feeler3Count = 0;
			}
		}


		// Reinitialize values
		f_tempDist1 = -1.0f;	f_tempDist2 = -1.0f;	f_leftCloseDist = -1.0f;
		v2f_intersect1.set(-10.0f, -10.0f);
		v2f_intersect2.set(-10.0f, -10.0f);
		intersectFlag = false;
		dbintersectFlag = false;

	
	} // end range finder method
		

	
	// Pie slice sensor
	public void pieSliceSensor(){
		
		// Get entity's center of rotation and current rotation angle
		f_pieTempX = (C_entity.getXposition() + 20); //C_entity.getXposition();
		f_pieTempY = (C_entity.getYposition() + 50); //C_entity.getYposition();
		//f_pieThetaDiff = (float)(Math.toRadians(Math.toDegrees(f_pieThetaDiff)));
		 
		f_theta = (float)(Math.toRadians(C_entity.getImagePointer().getRotation()));
		//System.out.println("Pie theta diff  " + f_pieThetaDiff + "  Pie theta " + f_theta);
		
		// Draw lines from center of rotation out to magnitude = 120
		
		// Upper right line
		L_upRightLine.setLocation(f_pieTempX, f_pieTempY);
		L_upRightLine.set(f_pieTempX, f_pieTempY, ((float)(f_pieTempX + (i_pieForwardLineLength * Math.sin(f_theta + f_pieThetaDiff)))),
				((float)(f_pieTempY - (i_pieForwardLineLength * Math.cos(f_theta + f_pieThetaDiff))))  );
		
		// Lower right line
		L_lowRightLine.setLocation(f_pieTempX, f_pieTempY);
		L_lowRightLine.set(f_pieTempX, f_pieTempY, ((float)(f_pieTempX + (i_pieRearLineLength * Math.sin(f_theta + (3 * f_pieThetaDiff))))),
				((float)(f_pieTempY - (i_pieRearLineLength * Math.cos(f_theta + (3 * f_pieThetaDiff)))))  );
		
		// Lower left line
		L_lowLeftLine.setLocation(f_pieTempX, f_pieTempY);
		L_lowLeftLine.set(f_pieTempX, f_pieTempY, ((float)(f_pieTempX + (i_pieRearLineLength * Math.sin(f_theta - (3 * f_pieThetaDiff))))),
				((float)(f_pieTempY - (i_pieRearLineLength * Math.cos(f_theta - (3 * f_pieThetaDiff)))))  );
		
		// Upper left line
		L_upLeftLine.setLocation(f_pieTempX, f_pieTempY);
		L_upLeftLine.set(f_pieTempX, f_pieTempY, ((float)(f_pieTempX + (i_pieForwardLineLength * Math.sin(f_theta - f_pieThetaDiff)))),
				((float)(f_pieTempY - (i_pieForwardLineLength * Math.cos(f_theta - f_pieThetaDiff))))  );
		
		
							// ADD ENEMIES //
		
		// Count number of enemy agents within our radar 
		for(CEnemyAgt c : Main.agentArray){
			
			// Get its ID
			Integer id = c.getEntityID();
			//System.out.println("Enemy " + id);
			
			// Some enemy agent's bounding circle is within our forward radar -- FORWARD RADAR
			if(frontPie.contains(c.m_boundingCircle) | frontPie.intersects(c.m_boundingCircle)){
				
				//System.out.println("In Front Pie " + id);
				// This enemy is not on our list of enemies
				if(!enemyAgtIDarrayFront.contains(id)){
					
					// So add this enemy
					enemyAgtIDarrayFront.add(id);

					// Increase activation level
					frontActLevel += 1;
					//System.out.println("Add to array " + id + "Activation " + frontActLevel);
				}
				
			
			} // end *if* enemy in our forward radar
			
			
			// Some enemy agent's bounding circle is within our forward radar -- RIGHT RADAR
			if(rightPie.contains(c.m_boundingCircle) | rightPie.intersects(c.m_boundingCircle)){

				// This enemy is not on our list of enemies
				if(!enemyAgtIDarrayRight.contains(id)){

					// So add this enemy
					enemyAgtIDarrayRight.add(id);

					// Increase activation level
					rightActLevel += 1;
				}


			} // end *if* enemy in our forward radar
			
			// Some enemy agent's bounding circle is within our forward radar -- REAR RADAR
			if(rearPie.contains(c.m_boundingCircle) | rearPie.intersects(c.m_boundingCircle)){

				// This enemy is not on our list of enemies
				if(!enemyAgtIDarrayRear.contains(id)){

					// So add this enemy
					enemyAgtIDarrayRear.add(id);

					// Increase activation level
					rearActLevel += 1;
				}


			} // end *if* enemy in our forward radar
			
			// Some enemy agent's bounding circle is within our forward radar -- LEFT RADAR
			if(leftPie.contains(c.m_boundingCircle) | leftPie.intersects(c.m_boundingCircle)){

				// This enemy is not on our list of enemies
				if(!enemyAgtIDarrayLeft.contains(id)){

					// So add this enemy
					enemyAgtIDarrayLeft.add(id);

					// Increase activation level
					leftActLevel += 1;
				}


			} // end *if* enemy in our forward radar
			
		} // end for loop to add enemies for each pie slice
		
		
		
							// REMOVE ENEMIES //
		
		// Update enemy list for enemies no longer on our radar
		for(CEnemyAgt cn : Main.agentArray){
			
			Integer j = cn.getEntityID();
			/*
			// This enemy agent is not in any pie slice but is on the list of enemies
			if (( !frontPie.contains(cn.m_boundingCircle)) && ( !rightPie.contains(cn.m_boundingCircle) ) &&
					( !rearPie.contains(cn.m_boundingCircle) ) &&  ( !leftPie.contains(cn.m_boundingCircle) ) 
					&& (enemyAgentsIDarray.contains(j)) ){
				
				// Remove enemy from list
				enemyAgentsIDarray.remove(j);
				
				// Decrease total activation level
				totalActLevel -= 1;
			}
			*/
			
			// This enemy agent is NOT within our radar but it is on our list of enemies -- FORWARD RADAR
			if( ( !frontPie.contains(cn.m_boundingCircle) )  &&  ( !frontPie.intersects(cn.m_boundingCircle) ) &&
					(enemyAgtIDarrayFront.contains(j)) ){
				
				// Remove this enemy from our list
				enemyAgtIDarrayFront.remove(j);
				
				// Decrease activation level
				frontActLevel -= 1;
				
			}
			
			// This enemy agent is NOT within our radar but it is on our list of enemies -- RIGHT RADAR
			if( ( !rightPie.contains(cn.m_boundingCircle) )  &&  ( !rightPie.intersects(cn.m_boundingCircle) ) && 
					(enemyAgtIDarrayRight.contains(j)) ){

				// Remove this enemy from our list
				enemyAgtIDarrayRight.remove(j);

				// Decrease activation level
				rightActLevel -= 1;

			}
			
			// This enemy agent is NOT within our radar but it is on our list of enemies -- REAR RADAR
			if( ( !rearPie.contains(cn.m_boundingCircle) )  &&  ( !rearPie.intersects(cn.m_boundingCircle) ) &&
					(enemyAgtIDarrayRear.contains(j)) ){

				// Remove this enemy from our list
				enemyAgtIDarrayRear.remove(j);

				// Decrease activation level
				rearActLevel -= 1;

			}
			
			// This enemy agent is NOT within our radar but it is on our list of enemies -- LEFT RADAR
			if( ( !leftPie.contains(cn.m_boundingCircle) )  &&  ( !leftPie.intersects(cn.m_boundingCircle) ) &&
					(enemyAgtIDarrayLeft.contains(j)) ){

				// Remove this enemy from our list
				enemyAgtIDarrayLeft.remove(j);

				// Decrease activation level
				leftActLevel -= 1;

			}
			
			
		} // end for loop to remove enemies from each pie slice
		
		
	} // end pie slice sensor (radar)
	
	
	
	
	
	// Agent sensing
	public void agentSensor(){
		
		
		// Need function
		
		
	} // end agent sensor


	
	
	
	
	// Turn radar on / off
	public void pieSliceSensorOnOff(boolean flipSwitch) {
		this.m_bl_radar = flipSwitch;
	}
	
	
	// Get equipment status
	public boolean isOnOff(String key){
		
		if(key.compareTo("range") == 0){
			return this.m_bl_rangeFinder;
		}
		else if(key.compareTo("agent") == 0){
			return this.m_bl_agentSensor;
		}
		else if(key.compareTo("pie") == 0){
			return this.m_bl_radar;
		}
		else{
			return false;
		}
	}
	

	// Turn agent sensor on / off
	public void agentSensorOnOff(boolean flipSwitch) {
		this.m_bl_agentSensor = flipSwitch;
	}
	

	// Turn range finder on / off
	public void rangeFinderOnOff(boolean flipSwitch) {
		this.m_bl_rangeFinder = flipSwitch;
	}



}

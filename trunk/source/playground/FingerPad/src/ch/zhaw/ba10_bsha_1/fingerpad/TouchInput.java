package ch.zhaw.ba10_bsha_1.fingerpad;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import android.R.string;
import android.util.Log;


public class TouchInput {
	

	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_NONE = new MicroGestureDetectionStrategyNone();
	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_PREDICTION = new MicroGestureDetectionStrategyPreditction();
	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_CURVATURE = new MicroGestureDetectionStrategyCurvature();
	public static final IMicroGestureDetectionStrategy MG_DETECTION_STRATEGY_VARIANTB = new MicroGestureDetectionStrategyVariantB();
	
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_NONE = new CharacterDetectionStrategyNone();
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_GRAPH = new CharacterDetectionStrategyGraph();
	public static final ICharacterDetectionStrategy CHAR_DETECTION_STRATEGY_CUSTOM_GRAPH = new CharacterDetectionStrategyCustomGraph("");
	
	private ArrayList<TouchPoint> points;
	private boolean stretch = true;
	private boolean smooth = false;
	private float letterHeight;
	private float upperMax;
	private float lowerMax;
	private Collection<MicroGesture> microGestures;
	private Collection<Character> characters;
	private IMicroGestureDetectionStrategy mgDetectionStrategy;
	private ICharacterDetectionStrategy charDetectionStrategy;
	private IPathSmoothingStrategy smoothingStrategy;
	
	
	public TouchInput(float field_height) {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		mgDetectionStrategy = MG_DETECTION_STRATEGY_NONE;
		mgDetectionStrategy.setFieldHeight(field_height);
		charDetectionStrategy = CHAR_DETECTION_STRATEGY_NONE;
		smoothingStrategy = new PathSmoothingStrategySpline();
	}
	
	public TouchInput(IMicroGestureDetectionStrategy mg_strategy, float field_height, ICharacterDetectionStrategy char_strategy) {
		points = new ArrayList<TouchPoint>();
		microGestures = null;
		mgDetectionStrategy = mg_strategy;
		mgDetectionStrategy.setFieldHeight(field_height);
		charDetectionStrategy = char_strategy;
		smoothingStrategy = new PathSmoothingStrategySpline();
	}
	
	public boolean isToStretch() {
		return stretch;
	}
	
	public void enableStretching(boolean stretch) {
		this.stretch = stretch;
	}
	
	public void setToStretch(float letter_height) {
		stretch = true;
		letterHeight = letter_height;
	}
	
	public boolean isToSmooth() {
		return smooth;
	}
	
	public void enableSmoothing(boolean smooth) {
		this.smooth  = smooth;
	}
	
	public void add(TouchPoint point) {
		points.add(point);
		if ((point.y > upperMax) || (points.size() == 1)) {
			upperMax = point.y;
		}
		if (point.y < lowerMax) {
			lowerMax = point.y;
		}
	}
	
	public IMicroGestureDetectionStrategy getMicroGestureDetectionStrategy() {
		return mgDetectionStrategy;
	}

	public void setMicroGestureDetectionStrategy(IMicroGestureDetectionStrategy detectionStrategy) {
		this.mgDetectionStrategy = detectionStrategy;
	}
	
	public ICharacterDetectionStrategy getCharacterDetectionStrategy() {
		return charDetectionStrategy;
	}

	public void setCharacterDetectionStrategy(ICharacterDetectionStrategy detectionStrategy) {
		this.charDetectionStrategy = detectionStrategy;
	}
	
	public Collection<MicroGesture> detectMicroGestures(IMicroGestureDetectionStrategy strategy) {
		return (new ArrayList<MicroGesture>(strategy.detectMicroGestures(points)));
	}
	
	private void stretchToField(float field_height) {
		float real_height = lowerMax - upperMax;
		float corr = 0;
		if (upperMax != lowerMax) { 
			if (real_height > field_height) {
				corr = field_height / real_height;
			} else if ((real_height > (field_height * 2.0/3)) 
					&& (real_height < (field_height * 5.0/6))) {
				corr = (float) (field_height * 2.0/3) / real_height;
			}
		}
		if (corr != 0) {
			for (TouchPoint point : points) {
				point.y = corr * point.y;
			}
		}
	}
	
	public Collection<MicroGesture> getMicroGestures() {
		return microGestures;
	}
	
	public String getMicroGestureDetectionReport() {
		StringBuffer result = new StringBuffer();
		if (microGestures != null) {
			result.append("Strategy: ");
			result.append(mgDetectionStrategy);
			result.append(", MicroGestures: \n");
			for (MicroGesture mg : microGestures) {
				result.append(mg);
				result.append(", ");
			}
			result.delete(result.length() - 2, result.length());
		} else {
			result.append("No Detection done");
		}
		return result.toString();
	}
	
	public Collection<Character> detectCharacters(ICharacterDetectionStrategy strategy) {
		return (new ArrayList<Character>(strategy.detectCharacter(microGestures)));
	}
	
	public Collection<Character> getCharacters() {
		return characters;
	}
	
	public String getCharacterDetectionReport() {
		StringBuffer result = new StringBuffer();
		if (characters != null) {
			result.append("Strategy: ");
			result.append(charDetectionStrategy);
			result.append(", Characters: \n");
			for (Character chr : characters) {
				result.append(chr);
				result.append(", ");
			}
			result.delete(result.length() - 2, result.length());
		} else {
			result.append("No Detection done");
		}
		return result.toString();
	}
	
	public void startDetectionNew() {
		// Disclaimer: Grober Hack
		// Point normalizing

		///// Normalize Point distances /////
		TouchPoint[] temp = new TouchPoint[points.size()];
		temp = points.toArray(temp);
		ArrayList<TouchPoint> normalizedPoints = new ArrayList<TouchPoint>();
		TouchPoint prev = temp[0];
		normalizedPoints.add(temp[0]);
		for(int i = 1; i < temp.length; i++) {
			double dist = Math.sqrt(Math.pow(prev.x - temp[i].x, 2) + Math.pow(prev.y - temp[i].y, 2));	
			// Remove Points too close together
			if(dist > 15 && dist < 50) {
				normalizedPoints.add(temp[i]);
				prev = temp[i];
			}
			// Add Points if too far from each other
			else if(dist > 50) {
				TouchPoint newp = new TouchPoint((temp[i].x + prev.x)/2f, (temp[i].y + prev.y)/2f);
				normalizedPoints.add(newp);
				normalizedPoints.add(temp[i]);
				prev = temp[i];
				//prev = newp;
			}
		}
		points = new ArrayList<TouchPoint>(normalizedPoints);
		////// End of normalizing Points distances /////
		

		///// Edge detection /////
		Vector<Vector<TouchPoint>> lines = new Vector<Vector<TouchPoint>>();
		
		points = new ArrayList<TouchPoint>(smoothingStrategy.smoothePath(points));
		
		double tolerance = 0.2d;
		TouchPoint[] pts = new TouchPoint[points.size()];
		pts = points.toArray(pts);
		double lastCurve = 0;
		Vector<TouchPoint> temp2 = new Vector<TouchPoint>();
		temp2.add(pts[0]);
		for (int i = 1; i < pts.length-1; i++) {
			temp2.add(pts[i]);
			float x1, y1;
			float x2, y2;
			x1 = pts[i-1].x - pts[i].x;
			y1 = pts[i-1].y - pts[i].y;
			x2 = pts[i+1].x - pts[i].x;
			y2 = pts[i+1].y - pts[i].y;
			
			//Winkel
			double cos = (x1 * x2 + y1 * y2) / (Math.sqrt(x1*x1 + y1*y1) * Math.sqrt(x2*x2 + y2*y2));

			if(lastCurve == 0) {
				lastCurve = cos;
			}
			else if(cos > -0.1 && Math.abs(cos - lastCurve) > tolerance) {
				temp2.add(pts[i]);

				lines.add(temp2);
				
				if (i != pts.length-1) {
					temp2 = new Vector<TouchPoint>();
					temp2.add(pts[i]);
				}
				lastCurve = 0;
			}
			else {
				lastCurve = cos;
			}
		}
		temp2.add(pts[pts.length -1]);
		lines.add(temp2);
		
		/// ENDE EDGE DETECTION ///
		
		ArrayList<TouchPoint> tempPoints = new ArrayList<TouchPoint>();
		
		microGestures = new ArrayList<MicroGesture>();
		for(Vector<TouchPoint> v : lines) {
			///// SMOOTHING //////
			//points = new ArrayList<TouchPoint>(smoothingStrategy.smoothePath(v));
			points = new ArrayList<TouchPoint>(v);
			tempPoints.addAll(points);
			///// ENDE SMOOTHING /////
			
			///// MICRO GESTURE DETECTION /////
			
			microGestures.addAll(detectMicroGestures(mgDetectionStrategy));
			///// END MICROGESTURE DETECTION /////
		}
		points = tempPoints;
		
		
		///// COMBINE IDENTICAL MICRO GESTURES ////
		
		/*ArrayList<MicroGesture> result = new ArrayList<MicroGesture>(microGestures);
		microGestures = new ArrayList<MicroGesture>();
		
		if (result.size() > 0) {
			microGestures.add(result.get(0));
			MicroGesture previous = result.get(0);
			for (int i = 1; i < result.size(); i++) {
				MicroGesture current = result.get(i);
				if (current.getPoints().size() > 4) {
					if (current.getType() == previous.getType() && current.getDirection2() == previous.getDirection2()) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);
					}
					else if ((current.getType() == MicroGesture.TYPE_LONG_LINE || 
								current.getType() == MicroGesture.TYPE_SHORT_LINE) 
							&& (previous.getType() == MicroGesture.TYPE_LONG_LINE ||
									previous.getType() == MicroGesture.TYPE_SHORT_LINE)) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						new MicroGestureDetectionStrategyAwesome2().setMicroGesture(previous);
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);
					
					}
					else if ((current.getType() == MicroGesture.TYPE_HALFCIRCLE && previous.getType() == MicroGesture.TYPE_SHORT_LINE &&
								current.getDirection2() == 3 && previous.getDirection2() == 0) ) {
						ArrayList<TouchPoint> list = current.getPoints();
						for (TouchPoint p : list) {
							previous.addPoint(p);
						}
						previous.setType(MicroGesture.TYPE_HALFCIRCLE);
						new MicroGestureDetectionStrategyAwesome2().analyseMicroGestureDirection(previous);

					}
					else {
						microGestures.add(current);
						previous = current;
					}
				}
				else {
					ArrayList<TouchPoint> list = current.getPoints();
					for (TouchPoint p : list) {
						previous.addPoint(p);
					}
				}
			}
		}*/
		
		//// END OF COMBINE ////
		
		
		characters = detectCharacters(charDetectionStrategy);
	}

	public void startDetection() {
		try {
			if (smooth) {
				points = new ArrayList<TouchPoint>(smoothingStrategy.smoothePath(points));
			}
			if (stretch) {
				//stretchToField(letterHeight);
			}
			
			microGestures = detectMicroGestures(mgDetectionStrategy);
			characters = detectCharacters(charDetectionStrategy);
			//addCharactersToGraph();
		} catch (Exception ex) {
			Log.e("TouchInput.startDetection", Log.getStackTraceString(ex), ex);
		}
	}
	
	// TEST
	
	private static int id = 10;
	
	public void addCharactersToGraph() {
		
		//for (Character c : characters) {
			Node source = charDetectionStrategy.getRoot();
			
			Iterator<MicroGesture> itr = microGestures.iterator();//c.getMicroGestures().iterator();
			while (itr.hasNext()) {
				MicroGesture g = itr.next();
				Node target;
				if(itr.hasNext()) {
					target = new Node(id++, g.toString());
				}
				else {
					DetectionLogger l = DetectionLogger.getInstance();
					target = new Node(id++, g.toString(), l.getAttemptedChars()[0]);
				}
				
				Edge temp = new Edge(source, target, 1);
				source.addOutgoingEdge(temp);
				target.addIncomingEdge(temp);
				source = target;
			}
		//}
		
		File b = new File("/sdcard/out.xml");
		GraphMLExport exp = new GraphMLExport();

		try {
			exp.writeFile(charDetectionStrategy.getRoot(), b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
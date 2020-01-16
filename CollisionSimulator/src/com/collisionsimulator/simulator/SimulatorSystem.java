package com.collisionsimulator.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.List;

public final class SimulatorSystem {
	
	private static SimulatorSystem system;
	public static int areaX;
	public static int areaY;
	public static int areaW;
	public static int areaH;
	public static String fileCache;
	public static String fileSave;
	public static UniverseSimulator universe;

	private SimulatorSystem() {}
	
	public static void saveSimulationData(Geometria[] geos, int interactions, double[][][] data){
		File file = new File(SimulatorSystem.fileCache);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		if(file.exists()){
			try {
				ObjectOutputStream simuOut = new ObjectOutputStream(new FileOutputStream(file));
				simuOut.writeInt(interactions);
				simuOut.writeObject(geos);
				simuOut.writeObject(data);
				simuOut.flush();
				simuOut.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public static void openSimulationData(){}

	public static void saveFile(Geometria[] geos, ForceField[] forces){saveFileAs(geos, forces, fileSave);}
	
	public static void saveFileAs(Geometria[] geos, ForceField[] forces, String fileName){
		File file = new File(fileName);
//		if(file.)
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		if(file.exists() && universe != null){
			try {
				ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(file));
				fileOut.writeInt(areaX);
				fileOut.writeInt(areaY);
				fileOut.writeInt(areaW);
				fileOut.writeInt(areaH);
				fileOut.writeInt(universe.interactions);
				fileOut.writeObject(geos);
				fileOut.writeObject(forces);
				fileOut.flush();
				fileOut.close();
				System.out.println("Salvando "+fileName);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			fileSave = fileName;
		}
	}
	
	public static void openFile(String fileName){
		File file = new File(fileName);
		if(file.exists() && universe != null){
			try {
				ObjectInputStream fileInp = new ObjectInputStream(new FileInputStream(file));
				areaX = fileInp.readInt();
				areaY = fileInp.readInt();
				areaW = fileInp.readInt();
				areaH = fileInp.readInt();
				universe.interactions = fileInp.readInt();
				Object geosO = fileInp.readObject();
				Object forcesO = fileInp.readObject();
				
				int geosL = Array.getLength(geosO);
				int forcesL = Array.getLength(forcesO);
				Geometria[] geos = new Geometria[geosL];
				ForceField[] forces = new ForceField[forcesL];

				for(int i = 0; i < geosL; i++){
					geos[i] = (Geometria) Array.get(geosO, i);
				}
				for(int i = 0; i < forcesL; i++){
					forces[i] = (ForceField) Array.get(forcesO, i);
				}
				fileInp.close();
				System.out.println("Abrindo "+fileName);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				return;
			}
			fileSave = fileName;
		}
	}
	
	public static void renderCacheSimulation(
			Geometria[] geos, 
			int interactions, 
			List<double[][]> data, 
			int width, 
			int height, 
			double scale, 
			String fileName){
		
	}
	
	public static SimulatorSystem simulatorSystem(){
		if(system == null)
			system = new SimulatorSystem();
		return system;
	}

}

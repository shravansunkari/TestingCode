package com.vassarlabs.test;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.vassarlabs.common.dsp.err.DSPException;
import com.vassarlabs.common.init.err.AppInitializationException;
import com.vassarlabs.common.init.service.api.IApplicationInitService;
import com.vassarlabs.common.utils.err.ObjectNotFoundException;
import com.vassarlabs.config.spring.AppContext;
import com.vassarlabs.location.service.api.ILocationHierarchyService;
import com.vassarlabs.location.utils.LocationConstants;

@Component
public class ReservoirCheck {
	
	@Autowired
	ILocationHierarchyService locationHierarchyService;

	@Autowired
	@Qualifier("RESTInitServiceImpl")
	protected IApplicationInitService applicationInitService;
	
	public void start() throws DSPException, ObjectNotFoundException{
		try {
			applicationInitService.initialize();
		} catch (AppInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Map<String, List<String>> reservoirToMicroBasinMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.MICROBASIN, LocationConstants.RESERVOIR);
		Map<String, List<String>> reservoirToMandalMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.MANDAL, LocationConstants.RESERVOIR);
		Map<String, List<String>> stateToReservoirMap = locationHierarchyService
				.getAllLocForParentChildTypes(LocationConstants.STATE, LocationConstants.RESERVOIR);
		Set<String> microNot = new HashSet<>();
		Set<String> mandalNot = new HashSet<>();
		if(reservoirToMicroBasinMap==null){
			System.out.println("Null micro");
		}
		if(reservoirToMandalMap==null){
			System.out.println("Null mandal");
		}
		for(String state : stateToReservoirMap.keySet()){
			List<String> reservoirsUnderState = stateToReservoirMap.get(state);
			for(String reservoirUUID : reservoirsUnderState){
				boolean found = false;
				for(String microBasin : reservoirToMicroBasinMap.keySet()){
					List<String> reservoirsUnderMicroBasin = reservoirToMicroBasinMap.get(microBasin);
					if(reservoirsUnderMicroBasin.contains(reservoirUUID)){
						found= true;
						break;
					}
				}
				if(found==false){
					microNot.add(locationHierarchyService.getLocationNameFromUUID(reservoirUUID));
				}			
			}
		}
		for(String state : stateToReservoirMap.keySet()){
			List<String> reservoirsUnderState = stateToReservoirMap.get(state);
			for(String reservoirUUID : reservoirsUnderState){
				boolean found = false;
				for(String mandal : reservoirToMandalMap.keySet()){
					List<String> reservoirsUnderMandal = reservoirToMandalMap.get(mandal);
					if(reservoirsUnderMandal.contains(reservoirUUID)){
						found= true;
						break;
					}
				}
				if(found==false){
					mandalNot.add(locationHierarchyService.getLocationNameFromUUID(reservoirUUID));
				}			
			}
		}
		
		System.out.println("Reservoirs not mapped to any micro basin");
		for(String name :microNot){
			System.out.println(name);
		}
		System.out.println("\n\n");
		System.out.println("Reservoirs not mapped to any mandal");
		for(String name :mandalNot){
			System.out.println(name);
		}
		
	}
	
	public static void main(String[] args) throws DSPException, ObjectNotFoundException{
		ReservoirCheck task = AppContext.getApplicationContext().getBean(ReservoirCheck.class);
		task.start();
	}
}

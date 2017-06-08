package com.vassarlabs.bhuvan.scraper;

class BhuvanData {
	int gridId;
	double latitude;
	double longitude;
	double evapotranspiration;
	double runOff;
	double soilMoisture;
	int X;
	int Y;

	public int getX() {
		return X;
	}

	public void setX(int x) {
		X = x;
	}

	public int getY() {
		return Y;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getGridId() {
		return gridId;
	}

	public void setGridId(int gridId) {
		this.gridId = gridId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getEvapotranspiration() {
		return evapotranspiration;
	}

	public void setEvapotranspiration(double evapotranspiration) {
		this.evapotranspiration = evapotranspiration;
	}

	public double getRunOff() {
		return runOff;
	}

	public void setRunOff(double runOff) {
		this.runOff = runOff;
	}

	public double getSoilMoisture() {
		return soilMoisture;
	}

	public void setSoilMoisture(double soilMoisture) {
		this.soilMoisture = soilMoisture;
	}

	@Override
	public String toString() {
		return "BhuvanData [gridId=" + gridId + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", evapotranspiration=" + evapotranspiration + ", runOff=" + runOff + ", soilMoisture=" + soilMoisture
				+ ", X=" + X + ", Y=" + Y + "]";
	}

}
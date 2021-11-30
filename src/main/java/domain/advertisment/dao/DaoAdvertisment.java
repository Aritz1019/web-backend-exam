package domain.advertisment.dao;

import java.util.ArrayList;

import domain.advertisment.model.Advertisment;

public interface DaoAdvertisment {
    public void insertAdvertisment(Advertisment advertisment);
	public Advertisment loaAdvertisment(int advertismentId);
	public ArrayList<Advertisment> loadAllAdvertisments();
	public void updateAdvertisment(Advertisment advertisment);
	public boolean deleteAdvertisment(int advertismentId);
    
}

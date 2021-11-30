package domain.advertisment.dao;

import java.util.ArrayList;

import domain.advertisment.model.Advertisment;

public class AdvertisementFacade {
    DaoAdvertisment daoAdvertisment;

    public AdvertisementFacade() {
        daoAdvertisment = new DaoAdvertismentMySQL();
    }

    public void insertAdvertisment(Advertisment advertisment) {
        daoAdvertisment.insertAdvertisment(advertisment);
    }

    public Advertisment loadAdvertisement(int advertismentId) {
        return daoAdvertisment.loaAdvertisment(advertismentId);
    }

    public ArrayList<Advertisment> loadAllNewsItems() {
        return daoAdvertisment.loadAllAdvertisments();
    }

    public void saveAdvertisement(Advertisment advertisment) {
        if (advertisment.getAdvertismentId() == 0) {
            daoAdvertisment.insertAdvertisment(advertisment);
        } else {
            daoAdvertisment.updateAdvertisment(advertisment);
        }
    }

    public boolean deleteAdvertisement(int advertismentId) {
        return daoAdvertisment.deleteAdvertisment(advertismentId);
    }
    
}

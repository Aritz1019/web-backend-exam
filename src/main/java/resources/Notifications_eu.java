package resources;

import java.util.ListResourceBundle;

public class Notifications_eu extends ListResourceBundle{
    private static final Object[][] contents = {
        //LocaleController
        {"message.languageUpdated", "Hizkuntza eguneratu duzu."},
        //LoginController
        {"message.login", "Saioa hasi duzu!"},
        {"error.login","Erabiltzaile edo pasahitz okerrak."},
        {"message.logout", "Saioa itxi duzu."},
        //UserController
        {"message.deleteUser", "Erabiltzailea ezabatu da."},
        {"error.deleteUser", "Arazoak erabiltzailea ezabatzen."},
        {"message.editUser", "Erabiltzailea aldatu da."},
        {"error.editUser", "Arazoak erabiltzailea aldatzen."},
        {"message.createUser", "Erabiltzailea sortu da."},
        {"error.createUser", "Arazoak erabiltzailea sortzen."},
        //NewsItemController
        {"message.createNewsItem", "Berria sortu da."},
        {"error.createNewsItem", "Arazoak berria sortzean."},
        {"message.editNewsItem", "Berria aldatu da."},
        {"error.editNewsItem", "Arazoak berria aldatzean."},
        {"message.deleteNewsItem", "Berria ezabatu da."},
        {"error.deleteNewsItem", "Arazoak berria ezabatzean."},
        //AdvertisementController
        {"message.createAdvertisement", "Advertisement-a sortu da"},
        {"error.createAdvertisement", "Arazoak advertisement-a sortzen."},
        {"message.editAdvertisement", "Advertisement-a aldatu da."},
        {"error.editAdvertisement", "Arazoak advertisement-a aldatzen."},
        {"message.deleteAdvertisement", "Advertisement-a ezabatu da."},
        {"error.deleteAdvertisement", "Arazoak advertisement-a ezabatzean."},
        //Errors
        {"error.createUser", "Arazoak erabiltzailea sortzen."},
        {"error.400.unknown_action","Existitzen ez den akzio bat egiten saiatzen zabiltza."},
        {"error.403.jsp","Ez daukazu JSP fitxategiak zuzenean ikusteko baimenik."},
        {"error.403.not_own_user","Zure erabiltzailea bakarrik aldatu dezakezu."},
        {"error.403.not_session_user","Saioa hasi akzio hau egiteko."},
        {"error.403.user_not_author","Erabiltzailea baliabidearen autorea izan behar da hau aldatzeko."},
        {"error.404.not_found","Baliabidea ez da existitzen edo ezin da kargatu."}
    };
    
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
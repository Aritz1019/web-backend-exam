package resources;

import java.util.ListResourceBundle;

public class Labels extends ListResourceBundle{
    private static final Object[][] contents = {
        {"hello", "Hello"},
        {"userId", "User ID"},
        {"username", "Username"},
        {"password", "Password"},
        {"firstName", "First Name"},
        {"secondName", "Second Name"},
        {"email", "Email"},
        {"edit", "Edit"},
        {"delete", "Delete"},
        {"save", "Save"},
        {"home", "Home"},
        {"userList", "UserList"},
        {"createUser", "Create User"},
        {"editUser", "Edit User"},
        /**/
        {"title", "Title"},
        {"body", "Body"},
        {"newsList", "News"},
        {"newsItem", "News Item"},
        {"createNewsItem", "Create News Item"},
        {"editNewsItem", "Edit News Item"},
        {"language", "Language"},
        {"creationDate", "Creation Date"},
        /**/
        {"login", "Login"},
        {"logged", "You are logged!"},
        {"logout", "Logout"},
        {"author", "Author"},
        /**/
        {"createAdvertisement", "Create advertisement"},
        {"editAdvertisement", "Edit advertisement"},
        {"deleteAdvertisement", "Delete advertisement"},
        {"src", "Src"},
        {"url", "Url"},
        {"advertLang", "Language of the advertisement"},
        {"sessionLang", "Language of the session"},


        {"language.currentLocale", "Current Language"},
        {"language.en", "English"},
        {"language.es", "Spanish"},
        {"language.eu", "Basque"},
        {"error.400.title", "Error 400 - Bad Request"},
        {"error.400.message","The action does not exist."},
        {"error.403.title", "Error 403 - Forbidden"},
        {"error.403.message","You do not have access to the action or the resource."},
        {"error.404.title", "Error 404 - Not found."},
        {"error.404.message","Resource couldn't be loaded or does not exist."}
    };
    
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}
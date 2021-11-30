package helper;

import javax.servlet.http.HttpServletRequest;

/**
 * This class will split the URL and return the element ID and the action
 * String. It exists so it can be reused in multiple controllers.
 * 
 * @author aperez
 *
 */
public class ControllerHelper {
    private String[] pathElements;
    private String action = "list";
    private int id = -1;

    public ControllerHelper(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.contentEquals("/")) {
            System.out.println("Path info:" + pathInfo);
            pathElements = pathInfo.replaceFirst("/", "").split("/");
            getIdFromPathElements();
            getActionFromPathElements();
        } /*
           * else { // e.g. http://localhost:8080/friendly or
           * http://localhost:8080/friendly/ //For empty path, default values => list & -1
           * }
           */
    }

    private void getIdFromPathElements() {
        try {
            id = Integer.parseInt(pathElements[0]);
        } catch (NumberFormatException e) {
            // If pathElements[0] is not an integer, request does not have an ID => id = -1,
            // id's default value.
        }
    }

    private void getActionFromPathElements() {
        if (pathElements.length > 1) {
            // Both id & action have been sent (e.g.: /user/1/create)
            action = pathElements[1];
        } else {
            // Id or action has not been sent.
            if (id == -1) {
                // If id==-1, no id has been sent => first element must be an action.
                // e.g. http://localhost:8080/friendly/create
                action = pathElements[0];
            } else {
                // If the id is an integer and no action is sent, view the element.
                // e.g. http://localhost:8080/friendly/1
                action = "view";
            }
        }
    }

    public String getAction() {
        return action;
    }

    public int getId() {
        return id;
    }
}

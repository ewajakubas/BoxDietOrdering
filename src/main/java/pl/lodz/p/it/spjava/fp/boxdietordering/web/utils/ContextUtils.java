package pl.lodz.p.it.spjava.fp.boxdietordering.web.utils;

import java.io.IOException;
import java.security.Principal;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import pl.lodz.p.it.spjava.fp.boxdietordering.exception.AppBaseException;



@ApplicationScoped //większość metod jest statycznych, ale niektóre są wywoływane z poziomu EL w stronach XHTML i muszą być składowymi instancji, stąd potrzebujemy tego jednego obiektu
@Named
public class ContextUtils {

    public ContextUtils() {
    }

    /**
     * Zwraca obiekt FacesContext - kontekst serwletu FacesServlet
     */
    public static ExternalContext getContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    /*
    Ta metoda jest wykorzystywana przez stronę wymuszającą zalogowanie (/login/authenticate.xhtml).
    Zadaniem metody jest wymuszenie powrotu do strony głównej po zalogowaniu.
    Niestety strona docelowa musi być określona jako URL, nie przypadek nawigacji.
     */
    public void redirectToRoot() {
        try {
            getContext().redirect(getContext().getApplicationContextPath());
        } catch (IOException ex) {
            Logger.getLogger(ContextUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście aplikacji
     */
    public static Object getApplicationAttribute(String attributeName) {
        return getContext().getApplicationMap().get(attributeName);
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście sesji
     */
    public static Object getSessionAttribute(String attributeName) {
        return getContext().getSessionMap().get(attributeName);
    }

    /**
     * Wyszukuje atrybut o zadanej nazwie w kontekście żądania
     */
    public static Object getRequestAttribute(String attributeName) {
        return getContext().getRequestMap().get(attributeName);
    }

    /**
     * Wyszukuje parametr inicjalizacyjny o zadanej nazwie
     */
    public static String getContextParameter(String paramName) {
        return getContext().getInitParameter(paramName);
    }

    /**
     * Dokonuje zamknięcia bieżącej sesji
     * @return 
     */
    public String invalidateSession() {
        ((HttpSession) getContext().getSession(true)).invalidate();

        /* Poprawne zakończenie sesji wymaga wymuszenia nowego żądania na przeglądarce, stąd metoda ta
         * prowadzi do przypadku nawigacji z elementem <redirect />.
         * UWAGA: integracja logowania typu BASIC z przeglądarką powoduje, że czasem mimo to "wylogowanie" jest nieskuteczne - 
         * powstaje nowa sesja już zalogowanego użytkownika. Dlatego bezpieczniej jest stosować uwierzytelnianie przez formularz (FORM).
         */
        return "main";

    }

    /**
     * Zwraca identyfikator bieżącej sesji
     */
    public static String getSessionID() {
        HttpSession session = (HttpSession) getContext().getSession(true);
        return session.getId();
    }

    /**
     * Zwraca nazwę zalogowanego użytkownika
     */
    public String getUserName() {
        Principal p = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        return (null == p ? "" : p.getName());
    }

    /**
     * Zwraca zasób (ResourceBundle) o ścieżce wskazywanej przez parametr
     * resourceBundle.path
     */
    public static ResourceBundle getDefaultBundle() {
        String bundlePath = getContextParameter("resourceBundle.path");
        if (null == bundlePath) {
            return null;
        } else {
            return ResourceBundle.getBundle(bundlePath, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        }
    }
   public static void emitI18NdMessageOfException(AppBaseException ex) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage();
        String bundle = context.getExternalContext().getInitParameter("resourceBundle.path");
        if (bundle != null && ex.getMessage() != null) {
            String localizedMessage = ResourceBundle.getBundle(bundle, context.getViewRoot().getLocale()).getString(ex.getMessage());
            message.setSummary(localizedMessage);
        }
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(null, message);
    }

//    public static void emitSuccessMessage(String id) {
//        emitI18NMessage(id, "general.success.message");
//    }
    public static boolean isI18NKeyExist(final String key) {
        try {
            return ContextUtils.getDefaultBundle().getString(key) != null && !"".equals(ContextUtils.getDefaultBundle().getString(key));
        } catch (MissingResourceException e) {
            return false;
        }
    }
    public static String getI18NMessage(String key) {
        if (isI18NKeyExist(key)) {
            return ContextUtils.getDefaultBundle().getString(key);
        } else {
            return key;
        }
    }
    public static void emitI18NMessage(String id, final String key) {
        FacesMessage msg = new FacesMessage(getI18NMessage(key));
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(id, msg);
    }
    
    public static void emitI18NMessage1(String id, final String key, String s) {
        FacesMessage msg = new FacesMessage(getI18NMessage(key));
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(id, msg);

    }
}




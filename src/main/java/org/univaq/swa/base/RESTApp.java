package org.univaq.swa.base;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.univaq.swa.framework.jackson.ObjectMapperContextResolver;
import org.univaq.swa.exceptions.AppExceptionMapper;
import org.univaq.swa.framework.security.CORSFilter;
import org.univaq.swa.resources.SimpleRes;
import org.univaq.swa.framework.security.AuthLoggedFilter;
import org.univaq.swa.framework.security.AuthenticationRes;
import org.univaq.swa.exceptions.JacksonExceptionMapper;
import org.univaq.swa.resources.Home;

/**
 *
 * @author didattica
 */
@ApplicationPath("rest")
public class RESTApp extends Application {

    private final Set<Class<?>> classes;

    public RESTApp() {
        HashSet<Class<?>> c = new HashSet<Class<?>>();
        //aggiungiamo tutte le *root resurces* (cioè quelle
        //con l'annotazione Path) che vogliamo pubblicare
        c.add(SimpleRes.class);
        c.add(Home.class);
        c.add(AuthenticationRes.class);
        //c.add(AuthenticationRes.class);

        //aggiungiamo il provider Jackson per poter
        //usare i suoi servizi di serializzazione e 
        //deserializzazione JSON
        c.add(JacksonJsonProvider.class);

        //necessario se vogliamo una (de)serializzazione custom di qualche classe    
        c.add(ObjectMapperContextResolver.class);

        //esempio di autenticazione
        c.add(AuthLoggedFilter.class);
        
        //aggiungiamo il filtro che gestisce gli header CORS
        c.add(CORSFilter.class);

        //esempi di exception mapper, che mappano in Response eccezioni non già derivanti da WebApplicationException
        c.add(AppExceptionMapper.class);
        c.add(JacksonExceptionMapper.class);

        classes = Collections.unmodifiableSet(c);
    }

    //l'override di questo metodo deve restituire il set
    //di classi che Jersey utilizzerà per pubblicare il
    //servizio. Tutte le altre, anche se annotate, verranno
    //IGNORATE
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}

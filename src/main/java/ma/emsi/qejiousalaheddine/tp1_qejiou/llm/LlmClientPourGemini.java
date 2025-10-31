package ma.emsi.qejiousalaheddine.tp1_qejiou.llm;

import jakarta.enterprise.context.Dependent;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;

/**
 * Gère l'interface avec l'API de Gemini.
 * Son rôle est essentiellement de lancer une requête à chaque nouvelle
 * question qu'on veut envoyer à l'API.
 */
@Dependent
public class LlmClientPourGemini implements Serializable {
    // Clé pour l'API du LLM
    private final String key;
    // Client REST. Facilite les échanges avec une API REST.
    private Client clientRest; // Pour pouvoir le fermer
    // Représente un endpoint de serveur REST
    private final WebTarget target;

    public LlmClientPourGemini() {
        // 1. Récupère la clé
        this.key = System.getenv("GEMINI_KEY");

        // 2. Vérifie la clé
        if (this.key == null || this.key.isBlank()) {
            throw new RuntimeException("La variable d'environnement GEMINI_KEY n'est pas définie !");
        }

        // 3. Crée le client REST et l'assigne AU CHAMP DE LA CLASSE (this.clientRest)
        this.clientRest = ClientBuilder.newClient();

        // 4. Crée l'URL cible (avec le modèle 'gemini-2.5-flash' qui marche pour vous)
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + this.key;

        // 5. Prépare la cible en utilisant le client de la classe (this.clientRest)
        this.target = this.clientRest.target(apiUrl);

        // 6. Ligne de débogage pour voir l'URL dans la console
        System.out.println("URL CIBLE DE L'API (Corrigée): " + this.target.getUri().toString());
    }

    /**
     * Envoie une requête à l'API de Gemini.
     * @param requestEntity le corps de la requête (en JSON).
     * @return réponse REST de l'API (corps en JSON).
     */
    public Response envoyerRequete(Entity requestEntity) {
        Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
        // Envoie la requête POST au LLM
        return request.post(requestEntity);
    }

    /**
     * Ferme le client REST.
     */
    public void closeClient() {
        // Ajout d'une vérification pour éviter une NullPointerException
        if (this.clientRest != null) {
            this.clientRest.close();
        }
    }
}
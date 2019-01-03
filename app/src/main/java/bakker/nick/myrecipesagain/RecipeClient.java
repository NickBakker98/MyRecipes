package bakker.nick.myrecipesagain;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeClient {

    String API_KEY = "a8170cc8d6e0808d293428894f55cbe6";
    String INGREDIENT = "&q=taco";

    @GET("/api/search?key=" + API_KEY + INGREDIENT)
    Call<RecipeList> recipeList();

    @GET("/api/get?key=" + API_KEY)
    Call<RecipeDetails> getRecipeDetails(@Query(value="rId") String recipeId);

}


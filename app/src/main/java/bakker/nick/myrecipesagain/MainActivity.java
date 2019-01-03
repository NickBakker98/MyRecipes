package bakker.nick.myrecipesagain;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


// display 3 recipes from a Recipe API
// don't forget to add some depencies
// implementation 'com.squareup.retrofit2:retrofit:2.4.0'
//    implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
//    implementation 'com.github.bumptech.glide:glide:4.7.1'
//    annotationProcessor 'com.github.bumptech.glide:compiler:4.7.1'
//
// and don't forget to allow internet accesa in you manifest file
//     <uses-permission android:name="android.permission.INTERNET" />

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final List<Recipe> recepten = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Now we want to get our data from the API
        // and store it in an Arraylist

        String BASE_URL = "https://www.food2fork.com/";

        // Specify the Retrofit builder that will take care of the API
        // you have to define the (base) url
        // and a converter. In this case GSON will convert the JSON output to java

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        // create the actual Retrofit object
        Retrofit retrofit = builder.build();

        //Call the API
        RecipeClient client = retrofit.create(RecipeClient.class);
        Call<RecipeList> call = client.recipeList();

        // Consume the API asynchronously
        // and put the first 3 results in the ArrayList
        call.enqueue(new Callback<RecipeList>() {
            @Override
            public void onResponse(Call<RecipeList> call, Response<RecipeList> response) {
                RecipeList recipes = response.body();
                if (recipes == null) {
                    Toast.makeText(MainActivity.this, "No recipes found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // We only want to see the first 3 recipes
                int count = recipes.getCount();
                if (count > 3) count = 3;
                for (int i=0; i<count; i++) {
                    recepten.add(recipes.getRecipes().get(i));
                }

            }
            @Override
            public void onFailure(Call<RecipeList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });

        // We have to wait for our data
        // otherwise we have nothing to display.
        // So we wait 3 seconds


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                // Create the adapter that will return a fragment for each of the three
                // primary sections of the activity.
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        }, 3000);   //3 seconds
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Now let's add the contents of our ArrayList in the fragments
            // Use Glide to get the image from the imageUrl
            ImageView imageView = rootView.findViewById(R.id.recipe_imageView);
            Glide.with(rootView).load(recepten.get(getArguments().getInt(ARG_SECTION_NUMBER)).getImageUrl()).into(imageView);

            // Now add the title of the recipe
            TextView textView = (TextView) rootView.findViewById(R.id.recipe_textView);
            textView.setText(recepten.get(getArguments().getInt(ARG_SECTION_NUMBER)).getTitle());

            // some listView for the ingredients must be added
            // List<String> ingredients = new ArrayList<>();
            // ingredients = recepten.get(getArguments().getInt(ARG_SECTION_NUMBER)).getIngredients();
            // and now add this list to some listView
            // not sure if you need an adapter for that

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return recepten.size();
        }
    }
}

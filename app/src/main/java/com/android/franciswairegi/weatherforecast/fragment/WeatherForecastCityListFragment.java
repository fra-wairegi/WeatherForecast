package com.android.franciswairegi.weatherforecast.fragment;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.viewmodel.WeatherForecastCityViewModel;
import com.android.franciswairegi.weatherforecast.model.WeatherForecastCityItem;
import com.android.franciswairegi.weatherforecast.utils.QueryCityPreferences;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastCityListFragment extends Fragment {

    /**
     * TAG used during debugging
     * private static final String TAG = WeatherForecastCityListFragment.class.getSimpleName();
    **/
    public static final String EXTRA_CITY_ID = "city_id";
    private static final int RESULT_CODE = Activity.RESULT_OK;

    private RecyclerView mCityListRecyclerView;
    private WeatherForecastCityViewModel mCityViewModel;
    private  List<WeatherForecastCityItem> mCityItems =
            new ArrayList<>();

    private WeatherForecastCityAdapter mCityAdapter;

    public static WeatherForecastCityListFragment newInstance(){

        return new WeatherForecastCityListFragment();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mCityViewModel = ViewModelProviders.of(getActivity())
                .get(WeatherForecastCityViewModel.class);
        mCityViewModel.getAllCities().observe(
                getActivity(),
                new Observer<List<WeatherForecastCityItem>>() {
                    @Override
                    public void onChanged(@Nullable List<WeatherForecastCityItem> weatherForecastCityItems) {

                        mCityItems = weatherForecastCityItems;
                        mCityAdapter.setWeatherForecastItems(weatherForecastCityItems);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weather_forecast_city_list, menu);

        MenuItem searchCity = menu.findItem(R.id.menu_city_filter);
        final SearchView searchView = (SearchView) searchCity.getActionView();

        searchCity.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        // Obtain the saved preference for the city name
        String cityName = QueryCityPreferences.getStoredQuery(getActivity());
        if(!cityName.isEmpty()){
            searchView.setQuery(cityName, false);
        }

        // Obtain the icon (Magnifying glass) of the SearchView

        ImageView searchViewIcon = searchView
                .findViewById(androidx.appcompat.R.id.search_mag_icon);

        // Remove the Magnifying glass
        //searchViewIcon.setVisibility(View.GONE); // This does not remove it
        searchViewIcon.setImageDrawable(null); // This does remove it


        searchView.setQueryHint(getString(R.string.search_city_hint));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mCityAdapter != null) mCityAdapter.getFilter().filter(newText);
                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_forecast_recyclerview_fragment,
                container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCityListRecyclerView = view.findViewById(R.id.fragment_recyclerview);
        mCityListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCityAdapter = new WeatherForecastCityAdapter(mCityItems);

        mCityListRecyclerView.setAdapter(mCityAdapter);
    }

    public class WeatherForecastCityAdapter extends RecyclerView.Adapter<WeatherForecastCityHolder>
    implements Filterable{

        private List<WeatherForecastCityItem> mWeatherForecastCityItems;
        private List<WeatherForecastCityItem> mFilteredList;

        public WeatherForecastCityAdapter(List<WeatherForecastCityItem> weatherForecastCityItems) {
            mWeatherForecastCityItems = weatherForecastCityItems;
            mFilteredList = weatherForecastCityItems;
        }

        @Override
        public WeatherForecastCityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WeatherForecastCityHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_forecast_city_list_item, parent, false));
        }

        void setWeatherForecastItems(List<WeatherForecastCityItem> weatherForecastCityItems){
            mWeatherForecastCityItems = weatherForecastCityItems;
            mFilteredList = weatherForecastCityItems;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mFilteredList.size();
        }

        @Override
        public void onBindViewHolder(WeatherForecastCityHolder holder, int position) {
            final WeatherForecastCityItem weatherForecastCityItem =
                    mFilteredList.get(position);
            holder.bindWeatherForecastCity(weatherForecastCityItem);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cityID = weatherForecastCityItem.getCityID().toString();
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_CITY_ID, cityID);
                    QueryCityPreferences.setStoredQuery(getActivity(),
                            weatherForecastCityItem.getCityName());
                    QueryCityPreferences.setStoredCityId(getActivity(),cityID);
                    getActivity().setResult(RESULT_CODE, intent);
                    getActivity().finish();
                }
            });

        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {

                    String charString = charSequence.toString().toLowerCase();

                    if (charString.isEmpty()) {

                        mFilteredList = mWeatherForecastCityItems;
                    } else {

                        ArrayList<WeatherForecastCityItem> filteredList = new ArrayList<>();

                        for (WeatherForecastCityItem cityItem : mWeatherForecastCityItems) {

                          if (cityItem.getCityName().toLowerCase().contains(charString)
                              || cityItem.getCityCountryName().toLowerCase().contains(charString)){

                            filteredList.add(cityItem);
                            }
                        }

                        mFilteredList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mFilteredList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<WeatherForecastCityItem>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


        private class WeatherForecastCityHolder extends RecyclerView.ViewHolder{

            private TextView mTextViewCityName, mTextViewCityCountryName;


            // Constructor to obtain the view from the WeatherForecastCityAdapter
            public WeatherForecastCityHolder(View itemView){
                super(itemView);

                mTextViewCityName = itemView.findViewById(R.id.city_name);
                mTextViewCityCountryName = itemView.findViewById(R.id.city_country_name);

            }

            public void bindWeatherForecastCity(WeatherForecastCityItem weatherForecastCityItem){

                mTextViewCityName.setText(weatherForecastCityItem.getCityStateName());
                mTextViewCityCountryName.setText(weatherForecastCityItem.getCityCountryName());
            }
        } // end of WeatherForecastCityHolder
}


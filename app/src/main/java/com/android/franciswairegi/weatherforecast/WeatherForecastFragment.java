package com.android.franciswairegi.weatherforecast;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecastFragment extends Fragment {

    public static final String TAG = WeatherForecastFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1;
    private static final String EXTRA_CITY_ID = "city_id";
    private static final String EXTRA_CITY_LIST = "city_list";
    private static final String FAHRENHEIT = "f";
    private static final String CELCIUS = "c";

    private RecyclerView mWeatherForecastRecyclerView;
   // private String mCityId = "5358705"; // Testing Huntington Beach
                                        // Should be set to the current location
    private String mCityId = Utility.DEFAULT_CITY_ID;
    private String mTemperatureUnit;
    //private static String sCityId;
    private WeatherForecastViewModel mWeatherForecastViewModel;
    private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItemsByCity =
            new ArrayList<>();
    //private WeatherForecastData mWeatherForecastData = new WeatherForecastData();
    private String mSubtitle = "";
    private ToggleButton mToggleButton;


    private WeatherForecastAdapter mWeatherForecastAdapter;

    //public static WeatherForecastFragment newInstance(String cityId){
    public static WeatherForecastFragment newInstance(){
/*        sCityId = cityId;
        Log.i(TAG, "cityId received in WeatherForecastFragment.newInstance "+ cityId);*/
        return new WeatherForecastFragment();
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        //mCityId = sCityId.toString();
      /*  mCityId = sCityId;
        QueryCityPreferences.setStoredQuery(getActivity(),null);
        QueryCityPreferences.setStoredCityId(getActivity(), mCityId); // used to set cityId to be
                                       // by RoomDatabase to build the OpenWeather Url*/

        Log.i(TAG, "TESTING1 in Fragment onCreate");

        mToggleButton = new ToggleButton(getActivity());

       queryWeatherForecastItems(mCityId);



/*        String [] timezones = TimeZone.getAvailableIDs();

        for ( String timezone : timezones){
            Log.i("TIMEZONEIDs", " Timezone " + timezone);
        }

        //*****************
        // Testing timezones

        int dateForecasted = 1533567600;
        Date mDate = new Date(dateForecasted*1000L);

        //TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
        TimeZone timezone = TimeZone.getTimeZone("Africa/Nairobi");
        timezone.getDisplayName();

        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeZone(timezone);
        //calendar.setTime(mDate);

        Calendar nairobiTime = new GregorianCalendar(TimeZone.getTimeZone("Africa/Nairobi"));
        nairobiTime.setTimeInMillis(calendar.getTimeInMillis());

        //Log.i("TIMEZONE", "timezone " + timezone.getRawOffset());
        Log.i("GGGGG", "calendar.getTime() " + calendar.getTime());
        Log.i("GGGGG", "nairobiTime.getTime() " + nairobiTime.getTime());
        Log.i("GGGGG", "mDate " + mDate);
        Log.i("GGGGG", "dateForecasted " + dateForecasted);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        //System.currentTimeMillis()


        //mDateForecast = new SimpleDateFormat(context.getString(R.string.date_format)).format(mDate);

        //mTimeForecast = new SimpleDateFormat(context.getString(R.string.time_format)).format(mDate);

        //******************/


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weather_forecast_fragment, menu);

        MenuItem temperatureUnitToggle = menu.findItem(R.id.app_bar_toggle);

        mToggleButton = (ToggleButton) temperatureUnitToggle.getActionView();

        //mToggleButton.setText(R.string.degrees_fahrenheit);
/*
// Commented on Fri Nov 2 as it is causing some runtime error !!!!!
       mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    updateTemperatureUnit(FAHRENHEIT);
                    //compoundButton.setText(R.string.degrees_fahrenheit);
                } else {
                    updateTemperatureUnit(CELCIUS);
                    //compoundButton.setText(R.string.degrees_celcius);
                }

            }
        });*/

       // MenuItem searchCity = menu.findItem(R.id.menu_city_search);
/*        final SearchView searchView = (SearchView) searchCity.getActionView();

        searchView.setQueryHint(getString(R.string.search_city_hint));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "Query Submit " + query);
                QueryCityPreferences.setStoredQuery(getActivity(),query);
                searchView.onActionViewCollapsed();
                updateWeatherForecastItems();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "Query Text Change " + newText);
                if (newText.isEmpty()){
                    Log.i(TAG, "newText is null " + newText);
                    //QueryCityPreferences.setStoredQuery(getActivity(),newText);
                }
                return false;
            }
        });


        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityQuery = QueryCityPreferences.getStoredQuery(getActivity());
                searchView.setQuery(cityQuery, false);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

                //startActivity(new Intent(getActivity(),WeatherForecastCityListActivity.class));
                Intent intent = new Intent(getActivity(), WeatherForecastCityListActivity.class);
                //intent.putParcelableArrayListExtra(EXTRA_CITY_LIST,mCityItems);
                startActivityForResult(intent, REQUEST_CODE);
                return true;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "TESTING1 on Fragment onActivityResult ");

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String cityId = data.getStringExtra(WeatherForecastCityListFragment.EXTRA_CITY_ID);
               // String cityId = data.getStringExtra(EXTRA_CITY_ID);
                Log.i(TAG,"TESTING1 onActivityResult String cityId " + cityId);
                //newInstance(cityId);
               // if (mWeatherForecastViewModel.getCount(cityId) == 0) {
                    mWeatherForecastViewModel.insert(cityId);
                //}
                queryWeatherForecastItems(cityId);
                //startActivity(data);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG,"TESTING1 onActivityResult RESULT_CANCELED ");
               // queryWeatherForecastItems(mCityId);
            }
        }
    }



    private void queryWeatherForecastItems(String cityId){
        //String cityQuery = QueryCityPreferences.getStoredQuery(getActivity());
        Log.i(TAG,"TESTING1 cityId " + cityId);
        Log.i(TAG,"Inside1 queryWeatherForecastItems");


        Log.i(TAG,"TESTING1 Just before calling ViewModelProviders.of");



        mWeatherForecastViewModel = ViewModelProviders.of(this)
                .get(WeatherForecastViewModel.class);

        mWeatherForecastViewModel.getWeatherForecastItemsByCity(cityId).observe(
        //mWeatherForecastViewModel.getAllWeatherForecast().observe(
            getActivity(),
            new Observer<List<WeatherForecastDao.WeatherForecastItemCity>>() {
                @Override
                public void onChanged(@Nullable List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems) {


                    if(!weatherForecastItems.isEmpty()) {

                    mWeatherForecastItemsByCity =  weatherForecastItems;

                    Log.i(TAG,"weatherForecastItems.get(0).getCityName()"
                            + weatherForecastItems.get(0).weatherForecastItem.getCityName());
                    Log.i(TAG,"weatherForecastItems.get(0).weatherForecastCityItem.getCityState()"
                            + weatherForecastItems.get(0).getCityState());

                    updateTemperatureUnit(FAHRENHEIT);

/*                   WeatherForecastData weatherForecastData =
                        weatherForecastDataFormatter.formatRecyclerViewItems(
                        mWeatherForecastItemsByCity);*/

// new method start

/*                   WeatherForecastDataFormatter weatherForecastDataFormatter =
                        new WeatherForecastDataFormatter(getActivity());

                   WeatherForecastData weatherForecastData =
                        weatherForecastDataFormatter.formatRecyclerViewItems(
                                mWeatherForecastItemsByCity);


                       // mWeatherForecastData = weatherForecastData;

                        mWeatherForecastAdapter = new WeatherForecastAdapter(
                                weatherForecastData.getSectionOrRowItems(),
                                weatherForecastData.getWeatherForecastItems());

                        mWeatherForecastRecyclerView.
                                setAdapter(mWeatherForecastAdapter);

                        mWeatherForecastAdapter.setWeatherForecastData(
                                weatherForecastData.getSectionOrRowItems(),
                                weatherForecastData.getWeatherForecastItems());

                        //mCityName = weatherForecastData.getCity();
                       // mCountryName = weatherForecastData.getCountry();
                        mSubtitle = weatherForecastData.getSubtitle();

                        setSubtitle();*/
// new method end


 /*                       String subtitle = getString(R.string.city_country_subtitle
                                , weatherForecastData.getCity()
                                , weatherForecastData.getCountry());

                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        activity.getSupportActionBar().setSubtitle(subtitle);*/
                    }
                }
            });

        Log.i(TAG,"TESTING1 mWeatherForecastItemsByCity.size() " + mWeatherForecastItemsByCity.size() );

        //new FetchWeatherItemsTask(cityId).execute();
    }

    private void updateTemperatureUnit(String temperatureUnit){

        WeatherForecastDataFormatter weatherForecastDataFormatter =
                new WeatherForecastDataFormatter(getActivity());

        WeatherForecastData weatherForecastData =
                weatherForecastDataFormatter.formatRecyclerViewItems(
                        mWeatherForecastItemsByCity);


        // mWeatherForecastData = weatherForecastData;

        mWeatherForecastAdapter = new WeatherForecastAdapter(
                weatherForecastData.getSectionOrRowItems(),
                weatherForecastData.getWeatherForecastItems());

        mWeatherForecastRecyclerView.
                setAdapter(mWeatherForecastAdapter);

        mWeatherForecastAdapter.setWeatherForecastData(
                weatherForecastData.getSectionOrRowItems(),
                weatherForecastData.getWeatherForecastItems(),
                temperatureUnit);

        if(temperatureUnit.equals(FAHRENHEIT)){
            mToggleButton.setText(R.string.fahrenheit);
        } else {
            mToggleButton.setText(R.string.celcius);
        }

        //mCityName = weatherForecastData.getCity();
        // mCountryName = weatherForecastData.getCountry();
        mSubtitle = weatherForecastData.getSubtitle();

        setSubtitle();
    }

    private void setSubtitle(){
        String subtitle = getString(R.string.subtitle,mSubtitle);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"Inside1 onCreateView");
        return inflater.inflate(R.layout.weather_forecast_recyclerview_fragment,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //queryWeatherForecastItems(mCityId);  * Commented on Oct 16
                                            // * to retain scroll position on configuration
                                            // * changes

        setSubtitle();

        mWeatherForecastRecyclerView = view.findViewById(R.id.fragment_recyclerview);
        mWeatherForecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWeatherForecastRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(getActivity()));

        //mWeatherForecastAdapter = new WeatherForecastAdapter();
        Log.i(TAG,"Inside1 onViewCreated");
       // mWeatherForecastAdapter = new WeatherForecastAdapter(mWeatherForecastData);
        mWeatherForecastRecyclerView.setAdapter(mWeatherForecastAdapter);


    /*    mWeatherForecastRecyclerView.setAdapter(
                new WeatherForecastAdapter(
                        mWeatherForecastData.getSectionOrRowItems(),
                        mWeatherForecastData.getWeatherForecastItems()));*/
    }

    private class WeatherForecastHolderHeader extends RecyclerView.ViewHolder{

        private TextView mDateForecasted;

        //private WeatherForecastItem mWeatherForecastItem;
        //private WeatherForecastItemHeader mWeatherForecastItemHeader;

        // Constructor to obtain the view from the WeatherForecastAdapter
        public WeatherForecastHolderHeader(View itemView){
            super(itemView);

            mDateForecasted = itemView.findViewById(R.id.date_forecasted);

        }

        public void bindWeatherForecastHeader(SectionOrRow sectionOrRow){

            //mWeatherForecastItemHeader = sectionOrRow;

            //mDateForecasted.setText(sectionOrRow.getSection().getDateForecastStr());
            //WeatherForecastItem sectionForecastItem = sectionOrRow.getSection();
            WeatherForecastDao.WeatherForecastItemCity sectionForecastItem =
                    sectionOrRow.getSection();
            Long dateForecasted = sectionForecastItem.weatherForecastItem.getDateForecasted();
            String timezone = sectionForecastItem.getCityTimezone();
            String dateForecastedStr = Utility.
                    getDateForecastStr(getActivity(), dateForecasted, timezone);
            Log.i(TAG, "DateFormat dateForecastedStr " + dateForecastedStr);
            mDateForecasted.setText(dateForecastedStr);
        }
    }

    private class WeatherForecastHolderItem extends RecyclerView.ViewHolder {
           // implements View.OnClickListener{

        private ImageView mWeatherIcon;
        //private TextView mDateForecasted,
        private TextView mTimeForecasted, mWeatherGroup, mTemperature, mTextViewTemperatureUnit;

        // Constructor to obtain the view from the WeatherForecastAdapter
        public WeatherForecastHolderItem(View itemView){
            super(itemView);
            //itemView.setOnClickListener(this);

            mWeatherIcon = itemView.findViewById(R.id.weather_icon);
            mTimeForecasted = itemView.findViewById(R.id.time_forecasted);
            mWeatherGroup = itemView.findViewById(R.id.weather_group);
            mTemperature = itemView.findViewById(R.id.temperature);
            mTextViewTemperatureUnit = itemView.findViewById(R.id.temperature_unit);

        }

        //public void bindWeatherForecastItem(WeatherForecastItem weatherForecastItem){
        public void bindWeatherForecastItem(SectionOrRow sectionOrRow){

            //mWeatherForecastItem = weatherForecastItem;
            WeatherForecastDao.WeatherForecastItemCity rowForecastItem = sectionOrRow.getRow();
            Log.i(TAG,"rowForecastItem icon " +
                    rowForecastItem.weatherForecastItem.getWeatherIcon());
            Log.i(TAG,"rowForecastItem getWeatherDescription " +
                    rowForecastItem.weatherForecastItem.getWeatherDescription());
            Log.i(TAG,"rowForecastItem temperat high" +
                    rowForecastItem.weatherForecastItem.getTemperatureHigh());

            Picasso.get()
                    .load(Utility.getWeatherIconUrl(
                            getActivity(), rowForecastItem.weatherForecastItem.getWeatherIcon()))
                    .into(mWeatherIcon);

           // Log.i(TAG, "Weather Icon URL: " + weatherForecastItem.getWeatherIconUrl());
            Long dateForecasted = rowForecastItem.weatherForecastItem.getDateForecasted();
            Integer temperatureFahrenheit = rowForecastItem.weatherForecastItem.getTemperature().intValue();
            Integer temperatureCelcius = rowForecastItem.getTemperatureCelcius().intValue();
            //Integer temperatureCeclius = Utility.farenheitToCelcius(temperatureFahrenheit);
            String timezone = rowForecastItem.getCityTimezone();
            String timeForecastedStr = Utility.
                    getTimeForecastStr(getActivity(),dateForecasted,timezone);
            mTimeForecasted.setText(timeForecastedStr);
            mWeatherGroup.setText(rowForecastItem.weatherForecastItem.getWeatherMain());
            //mTemperature.setText(temperatureCelcius.toString());

            if(mTemperatureUnit.equals(FAHRENHEIT)){
                mTemperature.setText(temperatureFahrenheit.toString());
                mTextViewTemperatureUnit.setText(R.string.fahrenheit);
                //mTemperature.setText(getString(R.string.degrees_fahrenheit,temperatureFahrenheit.toString()));
            } else if (mTemperatureUnit.equals(CELCIUS)){
                mTemperature.setText(temperatureCelcius.toString());
                mTextViewTemperatureUnit.setText(R.string.celcius);
                //mTemperature.setText(getString(R.string.degrees_celcius,temperatureCelcius.toString()));
            }

     /*       if(mToggleButton.isChecked()) {
                mTemperature.setText(temperatureFahrenheit.toString());
            } else {
                mTemperature.setText(temperatureCeclius.toString());
            }*/

        }

     /*   @Override
        public void onClick(View view) {
            Intent intent = WeatherForecastDetailPagerActivity.newIntent(getActivity(),
                    mWeatherForecastItem);
            //startActivity(intent);
        }*/
    }

    private class WeatherForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        private List<SectionOrRow> mWeatherSectionOrRowItems = new ArrayList<>();
        private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItems = new ArrayList<>();

        //private ArrayList<WeatherForecastItemHeader> mWeatherForecastItemHeaders;

        //private WeatherForecastData mWeatherForecastData;

        // Constructor to receive the Weather Forecast items
        public WeatherForecastAdapter(
                List<SectionOrRow> weatherForecastSectionOrRowItems,
                List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems){
            mWeatherSectionOrRowItems = weatherForecastSectionOrRowItems;
            mWeatherForecastItems = weatherForecastItems;
        }
/*        public WeatherForecastAdapter(WeatherForecastData weatherForecastData){
            Log.i(TAG,"Inside1 WeatherForecastData.Constructor");
            Log.i(TAG,"Inside1 WeatherForecastData.Constructor " +
                    mWeatherForecastData.getSectionOrRowItems().size());
            mWeatherForecastData = weatherForecastData;
        }*/


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == TYPE_HEADER) {
                return new WeatherForecastHolderHeader(LayoutInflater.from(getActivity()).inflate(
                        R.layout.recyclerview_forecast_list_item_header, parent, false
                ));
            } else { //if (viewType == TYPE_ITEM){
                return new WeatherForecastHolderItem(LayoutInflater.from(getActivity()).inflate(
                        R.layout.recyclerview_forecast_list_item, parent, false
                ));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder weatherForecastHolder, int position) {

            final SectionOrRow item = mWeatherSectionOrRowItems.get(position);
            //final SectionOrRow item = mWeatherForecastData.getSectionOrRowItems().get(position);
            if(!item.isRow()) {
                WeatherForecastHolderHeader weatherForecastHolderHeader =
                        (WeatherForecastHolderHeader) weatherForecastHolder;
                weatherForecastHolderHeader.bindWeatherForecastHeader(item);
            } else {
                WeatherForecastHolderItem weatherForecastHolderItem =
                        (WeatherForecastHolderItem) weatherForecastHolder;
                weatherForecastHolderItem.bindWeatherForecastItem(item);

                weatherForecastHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String forecastId = item.getRow().weatherForecastItem.getForecastId();
                        String cityId = item.getRow().weatherForecastItem.getCityId();
                        Log.i(TAG, "item.getRow().getForecastId()" + forecastId);


                        Intent intent = WeatherForecastDetailPagerActivity.newIntent(getActivity(),
                                forecastId, cityId);
                        startActivity(intent);

                    }
                });
            }

        }

        @Override
        public int getItemViewType(int position) {
            super.getItemViewType(position);
            //SectionOrRow item = mWeatherForecastData.getSectionOrRowItems().get(position);
            SectionOrRow item = mWeatherSectionOrRowItems.get(position);

            if(!item.isRow()) {
                return TYPE_HEADER;
            } else {
                return TYPE_ITEM;
            }
        }


        void setWeatherForecastData(List<SectionOrRow> weatherForecastSectionOrRowItems,
                                    List<WeatherForecastDao.WeatherForecastItemCity> weatherForecastItems,
                                    String temperatureUnit){
            mWeatherSectionOrRowItems = weatherForecastSectionOrRowItems;
            mWeatherForecastItems = weatherForecastItems;
            mTemperatureUnit = temperatureUnit;
            Log.i(TAG, "Inside setWeatherForecastData weatherForecastSectionOrRowItems.size() " +
                    "" + weatherForecastSectionOrRowItems.size());
            Log.i(TAG, "Inside setWeatherForecastData weatherForecastItems.size()"
                    + weatherForecastItems.size());
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            Log.i(TAG, "ttttt getItemCount "+ mWeatherSectionOrRowItems.size());
            return mWeatherSectionOrRowItems.size();

        }
    }

 /*   private class FetchWeatherItemsTask
            //extends AsyncTask<Void, Void, ArrayList<SectionOrRow>>{
            extends AsyncTask<Void, Void, WeatherForecastData>{

        private String mCityQuery;

        public FetchWeatherItemsTask(String queryCity){
            mCityQuery = queryCity;
        }

        @Override
        //protected ArrayList<SectionOrRow> doInBackground(Void... voids) {
        protected WeatherForecastData doInBackground(Void... voids) {
            FetchWeatherForecast fetchWeatherForecast = new FetchWeatherForecast(getActivity());

   *//*         if (mCityQuery == 0) {
                mCityQuery = 5358705; // Huntington Beach, US;
            }
*//*
             return fetchWeatherForecast.fetchWeatherForecastData(mCityQuery);

        }
        @Override
        //protected void onPostExecute(ArrayList<SectionOrRow> weatherForecastItems) {
        protected void onPostExecute(WeatherForecastData weatherForecastData) {

            mWeatherForecastRecyclerView.
                    setAdapter(new WeatherForecastAdapter(
                            weatherForecastData.getSectionOrRowItems()
                            , weatherForecastData.getWeatherForecastItems()));

                String subtitle = getString(R.string.city_country_subtitle
                        , weatherForecastData.getCity()
                        , weatherForecastData.getCountry());

                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(subtitle);


        }

    }*/
}

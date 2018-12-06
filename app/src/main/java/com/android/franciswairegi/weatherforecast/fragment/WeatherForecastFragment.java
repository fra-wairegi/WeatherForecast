package com.android.franciswairegi.weatherforecast.fragment;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.adapter.WeatherForecastAdapter;
import com.android.franciswairegi.weatherforecast.viewmodel.WeatherForecastViewModel;
import com.android.franciswairegi.weatherforecast.activity.WeatherForecastCityListActivity;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.utils.WeatherForecastData;
import com.android.franciswairegi.weatherforecast.utils.RecyclerViewItemDecorator;
import com.android.franciswairegi.weatherforecast.utils.WeatherForecastDataFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.android.franciswairegi.weatherforecast.utils.Utility.CELCIUS;
import static com.android.franciswairegi.weatherforecast.utils.Utility.FAHRENHEIT;

public class WeatherForecastFragment extends Fragment {

    public static final String TAG = WeatherForecastFragment.class.getSimpleName();
    private static final int REQUEST_CODE = 1;

    private RecyclerView mWeatherForecastRecyclerView;
    private WeatherForecastViewModel mWeatherForecastViewModel;
    private List<WeatherForecastDao.WeatherForecastItemCity> mWeatherForecastItemsByCity =
            new ArrayList<>();
    private String mSubtitle = "";
    private ToggleButton mToggleButton;


    private WeatherForecastAdapter mWeatherForecastAdapter;

    public static WeatherForecastFragment newInstance() {
        return new WeatherForecastFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mToggleButton = new ToggleButton(getActivity());

        // TODO: defaultCityId to use the devices current location
        String defaultCityId = getString(R.string.city_id_default_value);
        queryWeatherForecastItems(defaultCityId);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weather_forecast_fragment, menu);

        MenuItem temperatureUnitToggle = menu.findItem(R.id.app_bar_toggle);

        mToggleButton = (ToggleButton) temperatureUnitToggle.getActionView();

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    updateTemperatureUnit(FAHRENHEIT);
                } else {
                    updateTemperatureUnit(CELCIUS);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getActivity(), WeatherForecastCityListActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
        return true;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String cityId = data.getStringExtra(WeatherForecastCityListFragment.EXTRA_CITY_ID);
                mWeatherForecastViewModel.insert(cityId);
                queryWeatherForecastItems(cityId);
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }


    private void queryWeatherForecastItems(String cityId) {

        mWeatherForecastViewModel = ViewModelProviders.of(this)
                .get(WeatherForecastViewModel.class);

        mWeatherForecastViewModel.getWeatherForecastItemsByCity(cityId).observe(
                getActivity(),
                new Observer<List<WeatherForecastDao.WeatherForecastItemCity>>() {
                    @Override
                    public void onChanged(@Nullable List<WeatherForecastDao.WeatherForecastItemCity>
                                                  weatherForecastItems) {


                        if (!weatherForecastItems.isEmpty()) {

                            mWeatherForecastItemsByCity = weatherForecastItems;

                            updateTemperatureUnit(FAHRENHEIT);

                        }
                    }
                });
    }

    private void updateTemperatureUnit(String temperatureUnit) {

        WeatherForecastDataFormatter weatherForecastDataFormatter =
                new WeatherForecastDataFormatter(getActivity());

        WeatherForecastData weatherForecastData =
                weatherForecastDataFormatter.formatRecyclerViewItems(
                        mWeatherForecastItemsByCity);

        mWeatherForecastAdapter = new WeatherForecastAdapter(
                getActivity(),
                weatherForecastData.getSectionOrRowItems(),
                temperatureUnit);

        mWeatherForecastRecyclerView.
                setAdapter(mWeatherForecastAdapter);

        if (temperatureUnit.equals(FAHRENHEIT)) {
            mToggleButton.setText(R.string.fahrenheit);
        } else {
            mToggleButton.setText(R.string.celcius);
        }

        mSubtitle = weatherForecastData.getSubtitle();

        setSubtitle();
    }

    private void setSubtitle() {
        String subtitle = getString(R.string.subtitle, mSubtitle);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weather_forecast_recyclerview_fragment,
                container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSubtitle();

        mWeatherForecastRecyclerView = view.findViewById(R.id.fragment_recyclerview);
        mWeatherForecastRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWeatherForecastRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(getActivity()));

        mWeatherForecastRecyclerView.setAdapter(mWeatherForecastAdapter);
    }
}

package com.android.franciswairegi.weatherforecast.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.franciswairegi.weatherforecast.R;
import com.android.franciswairegi.weatherforecast.activity.WeatherForecastDetailPagerActivity;
import com.android.franciswairegi.weatherforecast.dao.WeatherForecastDao;
import com.android.franciswairegi.weatherforecast.utils.SectionOrRow;
import com.android.franciswairegi.weatherforecast.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import static com.android.franciswairegi.weatherforecast.utils.Utility.CELCIUS;
import static com.android.franciswairegi.weatherforecast.utils.Utility.FAHRENHEIT;

public class WeatherForecastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = WeatherForecastAdapter.class.getSimpleName();
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;
    private List<SectionOrRow> mWeatherSectionOrRowItems;
    private String mTemperatureUnit;

    // Constructor to receive the Weather Forecast items
    public WeatherForecastAdapter(
                                    Context context,
                                    List<SectionOrRow> weatherForecastSectionOrRowItems,
                                    String temperatureUnit) {
        mContext = context;
        mWeatherSectionOrRowItems = weatherForecastSectionOrRowItems;
        mTemperatureUnit = temperatureUnit;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new WeatherForecastHolderHeader(LayoutInflater.from(mContext).inflate(
                    R.layout.recyclerview_forecast_list_item_header, parent, false
            ));
        } else { //if (viewType == TYPE_ITEM){
            return new WeatherForecastHolderItem(LayoutInflater.from(mContext).inflate(
                    R.layout.recyclerview_forecast_list_item, parent, false
            ));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder weatherForecastHolder, int position) {

        final SectionOrRow item = mWeatherSectionOrRowItems.get(position);
        //final SectionOrRow item = mWeatherForecastData.getSectionOrRowItems().get(position);
        if (!item.isRow()) {
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


                    Intent intent = WeatherForecastDetailPagerActivity.newIntent(mContext,
                            forecastId, cityId);
                    mContext.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        //SectionOrRow item = mWeatherForecastData.getSectionOrRowItems().get(position);
        SectionOrRow item = mWeatherSectionOrRowItems.get(position);

        if (!item.isRow()) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mWeatherSectionOrRowItems.size();

    }

    /**
     * Displays the Day Header section
     */
    private class WeatherForecastHolderHeader extends RecyclerView.ViewHolder {

        private TextView mDateForecasted;

        public WeatherForecastHolderHeader(View itemView) {
            super(itemView);

            mDateForecasted = itemView.findViewById(R.id.date_forecasted);

        }

        public void bindWeatherForecastHeader(SectionOrRow sectionOrRow) {

            WeatherForecastDao.WeatherForecastItemCity sectionForecastItem =
                    sectionOrRow.getSection();
            Long dateForecasted = sectionForecastItem.weatherForecastItem.getDateForecasted();
            String timezone = sectionForecastItem.getCityTimezone();
            String dateForecastedStr = Utility.
                    getDateForecastStr(mContext, dateForecasted, timezone);
            mDateForecasted.setText(dateForecastedStr);
        }
    }

    /**
     * Displays the Hourly Item section
     */
    private class WeatherForecastHolderItem extends RecyclerView.ViewHolder {

        private ImageView mWeatherIcon;
        private TextView mTimeForecasted, mWeatherGroup, mTemperature, mTextViewTemperatureUnit;

        // Constructor to obtain the view from the WeatherForecastAdapter
        public WeatherForecastHolderItem(View itemView) {
            super(itemView);

            mWeatherIcon = itemView.findViewById(R.id.weather_icon);
            mTimeForecasted = itemView.findViewById(R.id.time_forecasted);
            mWeatherGroup = itemView.findViewById(R.id.weather_group);
            mTemperature = itemView.findViewById(R.id.temperature);
            mTextViewTemperatureUnit = itemView.findViewById(R.id.temperature_unit);

        }

        public void bindWeatherForecastItem(SectionOrRow sectionOrRow) {

            WeatherForecastDao.WeatherForecastItemCity rowForecastItem = sectionOrRow.getRow();

            Picasso.get()
                    .load(Utility.getWeatherIconUrl(
                            mContext, rowForecastItem.weatherForecastItem.getWeatherIcon()))
                    .into(mWeatherIcon);

            Long dateForecasted = rowForecastItem.weatherForecastItem.getDateForecasted();
            Integer temperatureFahrenheit =
                    rowForecastItem.weatherForecastItem.getTemperature().intValue();
            Integer temperatureCelcius = rowForecastItem.getTemperatureCelcius().intValue();
            String timezone = rowForecastItem.getCityTimezone();
            String timeForecastedStr = Utility.
                    getTimeForecastStr(mContext, dateForecasted, timezone);
            mTimeForecasted.setText(timeForecastedStr);
            mWeatherGroup.setText(rowForecastItem.weatherForecastItem.getWeatherMain());

            if (mTemperatureUnit.equals(FAHRENHEIT)) {
                mTemperature.setText(temperatureFahrenheit.toString());
                mTextViewTemperatureUnit.setText(R.string.fahrenheit);
            } else if (mTemperatureUnit.equals(CELCIUS)) {
                mTemperature.setText(temperatureCelcius.toString());
                mTextViewTemperatureUnit.setText(R.string.celcius);
            }
        }
    }
}


package com.example.lr10_mapsquiz;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    // Статические флаги, чтобы сохранять статус прохождения (Шаг 12)
    private static boolean isMoscowCorrect = false;
    private static boolean isParisCorrect = false;
    private static boolean isRioCorrect = false;
    private static boolean isSydneyCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Простой и надежный способ загрузки макета
        setContentView(R.layout.activity_maps);

        // Проверяем, вернулись ли мы из викторины с правильным ответом
        String answeredCity = getIntent().getStringExtra("answered_city");
        if (answeredCity != null) {
            if (answeredCity.equals("Moscow")) isMoscowCorrect = true;
            if (answeredCity.equals("Paris")) isParisCorrect = true;
            if (answeredCity.equals("Rio")) isRioCorrect = true;
            if (answeredCity.equals("Sydney")) isSydneyCorrect = true;
        }

        // Находим фрагмент карты и загружаем её
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Координаты городов
        LatLng sydney = new LatLng(-34, 151);
        LatLng moscow = new LatLng(55.753688, 37.622037);
        LatLng paris = new LatLng(48.856651, 2.351691);
        LatLng rio = new LatLng(-22.801122, -43.336894);

        // Настраиваем маркер Сиднея
        MarkerOptions sydneyMarker = new MarkerOptions().position(sydney).title("Sydney");
        if (isSydneyCorrect) {
            sydneyMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        mMap.addMarker(sydneyMarker);

        // Настраиваем маркер Москвы
        MarkerOptions moscowMarker = new MarkerOptions().position(moscow).title("Moscow");
        if (isMoscowCorrect) {
            moscowMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        mMap.addMarker(moscowMarker);

        // Настраиваем маркер Парижа
        MarkerOptions parisMarker = new MarkerOptions().position(paris).title("Paris");
        if (isParisCorrect) {
            parisMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        mMap.addMarker(parisMarker);

        // Настраиваем маркер Рио
        MarkerOptions rioMarker = new MarkerOptions().position(rio).title("Rio");
        if (isRioCorrect) {
            rioMarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        mMap.addMarker(rioMarker);

        // Фокусируемся на центре Земли с зумом 1.0f (чтобы видеть все маркеры)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 1.0f));

        // Настраиваем клик на маркеры (Шаг 8)
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String cityName = marker.getTitle();
                if (cityName == null) return false;

                // Проверяем, пройдена ли уже викторина для этого города
                if (cityName.equals("Moscow") && isMoscowCorrect) return false;
                if (cityName.equals("Paris") && isParisCorrect) return false;
                if (cityName.equals("Rio") && isRioCorrect) return false;
                if (cityName.equals("Sydney") && isSydneyCorrect) return false;

                // Переход к экрану вопросов (Шаг 8)
                Intent intent = new Intent(MapsActivity.this, QuizActivity.class);
                intent.putExtra("city_name", cityName); // Передаем имя города
                startActivity(intent);
                return true;
            }
        });
    }
}
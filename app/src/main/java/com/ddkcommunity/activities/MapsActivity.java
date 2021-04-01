package com.ddkcommunity.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.Constant;
import com.ddkcommunity.R;
import com.ddkcommunity.model.Country;
import com.ddkcommunity.model.user.UserResponse;
import com.ddkcommunity.utilies.AppConfig;
import com.ddkcommunity.utilies.DirectionsJSONParser;
import com.ddkcommunity.utilies.RunTimePermission;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.ddkcommunity.Constant.filter_current_address;

public class MapsActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    ArrayList<String> descriptionList = null;
    String searchString = "";

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    ProgressDialog fetch_dialog=null;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;
    View myLocationButton;
    private LatLng mOrigin_one;
    private LatLng mDestination_one;

    boolean current_button_click = false;
    boolean search_pickup = false;
    boolean drawroute = false;

    ProgressDialog progressDialog;

    AlertDialog gps_dialog;

    public static final int REQUEST_CODE = 99;

    double drop_latitude = 0.0;
    double drop_longitude = 0.0;

    LocationManager locationManager;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //the client
    private FusedLocationProviderClient mFusedLocationClient;


    String area = "";
    //search location
    double latitude = 0.0;
    double longitude = 0.0;
    long mLastClickTime = 0;
    AlertDialog custom_loader;
    // EditText search_drop_location_edt;
    RelativeLayout pic_cross_layout, cross_layout;
    ImageView pic_cros_img, cros_img;
    String json = "";
    ArrayList<HashMap<String, String>> all_address_list;
    ArrayList<HashMap<String, String>> searchResults_list;
    SearchOtherLocationAdapter searchOtherLocationActivity;
    boolean location_loded = false;
    EditText search_pick_up_from, search_drop_location_edt;
    RelativeLayout back_laout, search_back_laout, search_drop_location_layout, maplayout;
    ImageView backImg, search_backImg;
    RecyclerView search_recyclerview;
    TextView drop_location_edt, pick_up_from;
    private Polyline mPolyline;
    String countrycode="";
    boolean addressList = false;
    AlertDialog.Builder alertDialog2 = null;
    ProgressBar find_progressbar;
    private UserResponse userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        userData = AppConfig.getUserData(MapsActivity.this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        find_progressbar = findViewById(R.id.find_progressbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build();
        StrictMode.setThreadPolicy(policy);
        findids();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        EnableGPSAutoMatically();
        getGpsLocation();
        if (haveLocationPermission(MapsActivity.this))
        {
            checkgpsEnabled();
        } else {
            requestLocation(MapsActivity.this);
        }
        Handler d = new Handler();
        d.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (latitude == 0.0)
                {
                    checkgpsEnabled();
                }
            }
        }, 700);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        countrycode=userData.getUser().getCountryId();
        for (Country country : SplashActivity.countryList) {
            if (country.getId().equals(countrycode))
            {
                countrycode=country.getCountry();
            }
        }
        String substin="";
        if(countrycode!=null && !countrycode.equalsIgnoreCase(""))
        {
            substin=countrycode.substring(0,2);
        }
        countrycode=substin.toLowerCase();
        cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drop_location_edt.setText("");
                search_drop_location_edt.setText("");
                search_drop_location_edt.setHint("Enter Drop location");
                cros_img.setVisibility(View.GONE);
                cros_img.setImageResource(R.drawable.cancel);
                searchString = "";
            }
        });


        pic_cross_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pick_up_from.setText("");
                search_pick_up_from.setText("");
                search_pick_up_from.setHint("Enter Drop location");
                pick_up_from.setHint("Home");
                pic_cros_img.setVisibility(View.GONE);
                pic_cros_img.setImageResource(R.drawable.cancel);
                searchString = "";
            }
        });


        search_pick_up_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                search_pickup = true;
                try {
                    searchString = s.toString();
                    final int textLength = searchString.length();
                    if (searchString.equals("")) {
                        find_progressbar.setVisibility(View.GONE);


                        // cros_img.setImageResource(R.drawable.ca);
                        pic_cros_img.setVisibility(View.GONE);

                        // Constant.search_String="0";
                        searchResults_list = new ArrayList<>();

                        search_recyclerview.setVisibility(View.GONE);
                        searchOtherLocationActivity = new SearchOtherLocationAdapter(MapsActivity.this, searchResults_list);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.VERTICAL, false);
                        search_recyclerview.setLayoutManager(horizontalLayoutManager);
                        search_recyclerview.setAdapter(searchOtherLocationActivity);

                        // Constant.hideKeyBoard(SearchOtherLocationActivity.this);
                    } else {

                        new getAllAddress().execute();
                        pic_cros_img.setVisibility(View.VISIBLE);
                        //  autocomplete(s.toString());

                    }
                } catch (Exception aa) {
                    aa.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        search_drop_location_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search_pickup = false;
                try {
                    searchString = s.toString();
                    final int textLength = searchString.length();
                    if (searchString.equals("")) {
                        find_progressbar.setVisibility(View.GONE);
                        // cros_img.setImageResource(R.drawable.ca);
                        cros_img.setVisibility(View.GONE);

                        // Constant.search_String="0";
                        searchResults_list = new ArrayList<>();

                        search_recyclerview.setVisibility(View.GONE);
                        searchOtherLocationActivity = new SearchOtherLocationAdapter(MapsActivity.this, searchResults_list);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.VERTICAL, false);
                        search_recyclerview.setLayoutManager(horizontalLayoutManager);
                        search_recyclerview.setAdapter(searchOtherLocationActivity);
                        // Constant.hideKeyBoard(SearchOtherLocationActivity.this);
                    } else {

                        new getAllAddress().execute();
                        cros_img.setVisibility(View.VISIBLE);
                        //  autocomplete(s.toString());

                    }
                } catch (Exception aa) {
                    aa.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getGpsLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    public ArrayList<String> autocomplete(String input) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                find_progressbar.setVisibility(View.VISIBLE);
            }
        });
        String LOG_TAG = "Google Places Autocomplete";
        String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        String TYPE_AUTOCOMPLETE = "/autocomplete";
        String OUT_JSON = "/json";
        String API_KEY = "AIzaSyAm6cgddaokQMNWHGwcitYolkUivMPULE0";
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:"+countrycode);
            sb.append("&type=address");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e)
        {
            // Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            //   Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            Log.d("yo", jsonResults.toString());
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            descriptionList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).toString());
                descriptionList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
            // saveArray(resultList.toArray(new String[resultList.size()]), "predictionsArray", getContext());
            System.out.println("" + resultList);
            System.out.println("" + descriptionList);
        } catch (JSONException e) {
            //  Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              if (descriptionList.size() > 0) {
                                  if (search_pickup == true) {
                                      pic_cros_img.setImageResource(R.drawable.cancel);
                                      pic_cros_img.setVisibility(View.VISIBLE);
                                  } else {
                                      cros_img.setImageResource(R.drawable.cancel);
                                      cros_img.setVisibility(View.VISIBLE);

                                  }
                                  //8962108118
                                  // Constant.search_String="1";
                                  search_recyclerview.setVisibility(View.VISIBLE);
                                  find_progressbar.setVisibility(View.GONE);
                                  searchResults_list = new ArrayList<>();

                                  for (int j = 0; j < descriptionList.size(); j++) {
                                      String addressName = descriptionList.get(j);
                                      HashMap hashMap = new HashMap();
                                      hashMap.put("address", addressName);
                                      searchResults_list.add(hashMap);
                       /* if(textLength<=foodname.length()){
                            if(foodname.toLowerCase().contains(searchString.toLowerCase()))
                            {
                                searchResults_list.add(all_address_list.get(j));
                            }
                        }*/
                                  }

                                  searchOtherLocationActivity = new SearchOtherLocationAdapter(MapsActivity.this, searchResults_list);
                                  LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.VERTICAL, false);
                                  search_recyclerview.setLayoutManager(horizontalLayoutManager);
                                  search_recyclerview.setAdapter(searchOtherLocationActivity);
                                  //Constant.hideKeyBoard(SearchOtherLocationActivity.this);

                                  if (searchResults_list.size() == 0) {
                                      search_recyclerview.setVisibility(View.GONE);
                                  } else {
                                      search_recyclerview.setVisibility(View.VISIBLE);
                                  }


                              } else {

                                  searchResults_list = new ArrayList<>();
                                  searchOtherLocationActivity = new SearchOtherLocationAdapter(MapsActivity.this, searchResults_list);
                                  LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.VERTICAL, false);
                                  search_recyclerview.setLayoutManager(horizontalLayoutManager);
                                  search_recyclerview.setAdapter(searchOtherLocationActivity);
                                  //Constant.hideKeyBoard(SearchOtherLocationActivity.this);

                                  if (searchResults_list.size() == 0) {
                                      search_recyclerview.setVisibility(View.GONE);
                                  } else {
                                      search_recyclerview.setVisibility(View.VISIBLE);
                                  }


                              }


                          }
                      }

        );


        //progressDialog.dismiss();
        return descriptionList;
    }


    private void findids() {
        pick_up_from = findViewById(R.id.pick_up_from);
        drop_location_edt = findViewById(R.id.drop_location_edt);
        drop_location_edt = findViewById(R.id.drop_location_edt);
        search_drop_location_edt = findViewById(R.id.search_drop_location_edt);
        search_back_laout = findViewById(R.id.search_back_laout);
        search_pick_up_from = findViewById(R.id.search_pick_up_from);
        cross_layout = findViewById(R.id.cross_layout);
        pic_cross_layout = findViewById(R.id.pic_cross_layout);
        cros_img = findViewById(R.id.cros_img);
        pic_cros_img = findViewById(R.id.pic_cros_img);
        back_laout = findViewById(R.id.back_laout);
        backImg = findViewById(R.id.backImg);
        search_drop_location_layout = findViewById(R.id.search_drop_location_layout);
        maplayout = findViewById(R.id.maplayout);
        search_backImg = findViewById(R.id.search_backImg);
        search_recyclerview = findViewById(R.id.address_recyclerview);
        back_laout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        drop_location_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_drop_location_layout.setVisibility(View.VISIBLE);
                maplayout.setVisibility(View.GONE);
                search_drop_location_edt.requestFocus();
                Constant.showKeyBoard(MapsActivity.this);
                search_drop_location_edt.requestFocus();
                search_drop_location_edt.requestFocus();
            }
        });
        pick_up_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_drop_location_layout.setVisibility(View.VISIBLE);
                maplayout.setVisibility(View.GONE);
                search_pick_up_from.requestFocus();
                Constant.showKeyBoard(MapsActivity.this);
                search_drop_location_edt.requestFocus();
                search_pick_up_from.requestFocus();
                find_progressbar.setVisibility(View.GONE);
            }
        });
        search_back_laout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_drop_location_layout.setVisibility(View.GONE);
                maplayout.setVisibility(View.VISIBLE);
                Constant.hideKeyBoard(MapsActivity.this);
            }
        });
        search_backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_drop_location_layout.setVisibility(View.GONE);
                maplayout.setVisibility(View.VISIBLE);
                Constant.hideKeyBoard(MapsActivity.this);


            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap = googleMap;
       /* if (mMap == null) {
            return;
        }*/
        UiSettings settings = mMap.getUiSettings();
        settings.setAllGesturesEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getMyLocation();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
        googleMap.animateCamera(cameraUpdate, 17, null);

        try {
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String current_location = addresses.get(0).getAddressLine(0);
            /*area = String.valueOf(addresses.get(0).getSubLocality());
            if(area.equalsIgnoreCase(""))
            {
                area = String.valueOf(addresses.get(0).getLocality());
            }*/
       /*     pi.setText(current_location);
            search_drop_location_edt.setText(current_location);
*/
        } catch (Exception c) {

            c.printStackTrace();
        }


    }

    public void currentLocationButton() {
        myLocationButton = mapFragment.getView().findViewById(0x2);

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_button_click=true;
                fetch_dialog = new ProgressDialog(MapsActivity.this);
                fetch_dialog.setTitle("Please wait your location is fetching");
                fetch_dialog.setCanceledOnTouchOutside(false);
                fetch_dialog.show();
                getLocation();
            }
        });

    }




    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (current_button_click==true)
        {
            new getGpsOnbuttonClick(location).execute();

        }


    }


    private void changedLocation(Location location) {
        try {



            if (drawroute==false)
            {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String       current_location =addresses.get(0).getAddressLine(0);
                String geoAddressString =addresses.get(0).getAddressLine(0);
/*            area =addresses.get(0).getSubLocality();
            if(area.equalsIgnoreCase(""))
            {
                area = String.valueOf(addresses.get(0).getLocality());

            }*/
                pick_up_from.setText(geoAddressString);
                search_pick_up_from.setText(geoAddressString);
         /*   mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this);
       */     currentLocationButton();

                mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);

                /*if (addressList==false)
                {
                    all_address_list=new ArrayList<>();
                    StringBuilder sbValue = new StringBuilder(sbMethod());
                    PlacesTask placesTask = new PlacesTask();
                    placesTask.execute(sbValue.toString());

                }
*/
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public StringBuilder sbMethod() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setTitle("Please wait fetching address");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        double mLatitude = 20.5937;
        double mLongitude = 78.9629;


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

        //StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+inputstr+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyAm6cgddaokQMNWHGwcitYolkUivMPULE0");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=5000");
        sb.append("&types=" + "address");
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyAm6cgddaokQMNWHGwcitYolkUivMPULE0");//new api
        Log.d("Map", "api: " + sb.toString());
        return sb;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showCustomLoader();
        }
        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;
        List<HashMap<String, String>> places = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();
            try {
                jObject = new JSONObject(jsonData[0]);
                places = placeJson.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            progressDialog.dismiss();
            if (places.size()>0){
                addressList=true;
            }
            else
            {
                //     Toast.makeText(MapsActivity.this, "Address not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public class Place_JSON {
        public List<HashMap<String, String>> parse(JSONObject jObject) {
            JSONArray jPlaces = null;
            try {
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return getPlaces(jPlaces);
        }
        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            final String[][] placename_array = {{"address"}};
            final List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            List<HashMap<String, String>> placesList1 = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> list = new ArrayList<String>();
                        for(int j=0;j<placesList.size();j++)
                        {
                            String school_name = placesList.get(j).get("place_name");
                            list.add(school_name);
                        }
                        /*String[] tempArray = new String[list.size()];
                        placename_array[0] = list.toArray(tempArray);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, placename_array[0]);
                        regaddress_edittext1.setThreshold(1);//will start working from first character
                        regaddress_edittext1.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
                        regaddress_edittext1.setTextColor(Color.BLACK);*/
                    }
                });

            }
            catch (Exception c)
            {

            }

            try
            {

                //   custom_loader.dismiss();


                for(int i=0;i<=placesList.size();i++)
                {
                    HashMap<String,String>map=new HashMap<>();
                    map.put("address",placesList.get(i).get("place_name"));
                    map.put("vicinity",placesList.get(i).get("vicinity"));
                    map.put("lat",placesList.get(i).get("lat"));
                    map.put("lng",placesList.get(i).get("lng"));
                    all_address_list.add(map);
                }
            }
            catch (Exception xx)
            {
                xx.printStackTrace();
            }
            custom_loader.dismiss();
            System.out.println("placpicker_list"+all_address_list.toString());
            return placesList;
        }
        private HashMap<String, String> getPlace(JSONObject jPlace) {
            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {

                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }
                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");
                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return place;
        }
    }

    //adapter
    public class SearchOtherLocationAdapter extends  RecyclerView.Adapter<SearchOtherLocationAdapter.MyViewHolder> {
        Context ctx;
        int count;
        long mLastClickTime = 0;

        ArrayList<HashMap<String,String>>addresslist;
        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView loc_name,address;
            public MyViewHolder(View view) {
                super(view);
                loc_name = view.findViewById(R.id.loc_name);
                address = view.findViewById(R.id.address);
            }
        }
        public SearchOtherLocationAdapter(Context ctx, ArrayList<HashMap<String,String>>addresslist) {
            this.addresslist = addresslist;
            this.ctx = ctx;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_other_location_adapter, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.loc_name.setText(addresslist.get(position).get("address"));
            //  holder.address.setText(addresslist.get(position).get("vicinity"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (latitude==0.0)
                    {
                        Toast.makeText(MapsActivity.this, "Pickup latitude and longitude not found,Please try again", Toast.LENGTH_SHORT).show();
                        getLocation();
                        return;
                    }

                    filter_current_address=addresslist.get(position).get("address");

                    if (search_pickup==false)
                    {
                        search_drop_location_edt.setText(filter_current_address);
                        drop_location_edt.setText(filter_current_address);

                    }
                    else
                    {
                        search_pick_up_from.setText(filter_current_address);
                        pick_up_from.setText(filter_current_address);
                    }

                    new getLatlongAsynctask().execute();
                    if (search_pick_up_from.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        search_pick_up_from.setError("Please enter pickup location");
                        search_pick_up_from.requestFocus();
                        return;
                    }

                    if (search_drop_location_edt.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        search_drop_location_edt.setError("Please enter drop location");
                        search_drop_location_edt.requestFocus();
                        return;
                    }











                    maplayout.setVisibility(View.VISIBLE);
                    search_drop_location_layout.setVisibility(View.GONE);


                    //  drawRouteone();

                    Constant.hideKeyBoard(MapsActivity.this);


                }
            });
        }
        @Override
        public int getItemCount() {
            try {
                count = addresslist.size();
                if (count == 0) {
                    count = 0;
                }
                else
                {
                    count = addresslist.size();
                }
            }
            catch (NullPointerException zxv) {
                zxv.printStackTrace();
            }
            return count;
        }
    }
    private void drawRouteone(){
        // Getting URL to the Google Directions API
        String url = getDirectionsUrlone(mOrigin_one, mDestination_one);

        DownloadTaskone downloadTask = new DownloadTaskone();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrlone(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        //String key = "key=" + "AIzaSyALvRsw2s8suFNnCyKJNGmVFhEVxXJop44";
        String key = "key=" + "AIzaSyAm6cgddaokQMNWHGwcitYolkUivMPULE0";
        String parameters = str_origin+"&"+str_dest+"&"+key;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }


    private class DownloadTaskone extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrlone(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTaskone parserTask = new ParserTaskone();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private String downloadUrlone(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    class ParserTaskone extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes

            mMap.clear();

            try {
                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for(int j=0;j<path.size();j++){
                        HashMap<String,String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(8);
                    lineOptions.color(Color.RED);



                }



            }
            catch (Exception h)
            {
                h.printStackTrace();
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);
                drawroute = false;
                addMarkerMethod();



            }else
            {

                System.out.println("");
                drawRouteone();
            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },5000);



            // Toast.makeText(getApplicationContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }

    private void addMarkerMethod() {

       /* Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);
*/
// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

// modify canvas
     /*   canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.green_round), 0,0, color);
        canvas1.drawText("User Name!", 30, 40, color);
*/
        Drawable drawable = ContextCompat.getDrawable(MapsActivity.this,R.drawable.green_round);
        Bitmap icon = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);


        Drawable drawable_drop= ContextCompat.getDrawable(MapsActivity.this,R.drawable.red_round);
        Bitmap icon_drop = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);
        Canvas canvas_drop = new Canvas(icon_drop);
        drawable_drop.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable_drop.draw(canvas_drop);

// add marker to Map

        //mMap.clear();

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(drop_latitude, drop_longitude))
                .icon(BitmapDescriptorFactory.fromBitmap(icon_drop))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            }
            catch (Exception v )
            {

            }
        }
    }


    public void getLocationfused() {


        // Toast.makeText(getApplicationContext(), "getLocation", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //request the last location and add a listener to get the response. then update the UI.
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location.
                    //  TextView locationText = findViewById(R.id.locationText);
                    if (location != null) {
                        // locationText.setText("location: " + location.toString());
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        changedLocation(location);



                        //   Toast.makeText(MapsActivity.this, ""+String.valueOf(latitude), Toast.LENGTH_SHORT).show();


                    } else {
                        //getLocationfused();
                        //    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        //locationTlext.setText("location: IS NULL");
                    }
                }
            });
        } else {
            //  Toast.makeText(getApplicationContext(), "getLocation ERROR", Toast.LENGTH_LONG).show();
            // TextView locationText = findViewById(R.id.locationText);
            // locationText.setText("location: ERROR");
            //  getLocationfused();

        }
    }

    ///autocomplete adapter
    public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private static final String LOG_TAG = "Google Places Autocomplete";
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";
        private static final String API_KEY = "AIzaSyAm6cgddaokQMNWHGwcitYolkUivMPULE0";
        private ArrayList<String> resultList;
        private Context context = null;
        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.context = context;
        }


        @Override
        public int getCount() {
            if(resultList != null)
                return resultList.size();
            else
                return 0;
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }


        public ArrayList<String> autocomplete(String input) {
            ArrayList<String> resultList = null;
            ArrayList<String> descriptionList = null;
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("&components=country:in");
                sb.append("&input=" + URLEncoder.encode(input, "utf8"));

                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                //Log.e(LOG_TAG, "Error processing Places API URL", e);
                return resultList;
            } catch (IOException e) {
                // Log.e(LOG_TAG, "Error connecting to Places API", e);
                return resultList;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                // Create a JSON object hierarchy from the results
                Log.d("yo",jsonResults.toString());
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                descriptionList = new ArrayList(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).toString());
                    descriptionList.add(predsJsonArray.getJSONObject(i).getString("description"));
                }

                System.out.println(""+resultList);
                System.out.println(""+descriptionList);

                //String data = resultList.toArray(new String[resultList.size()]), "predictionsArray", getContext();

                //  saveArray(resultList.toArray(new String[resultList.size()]), "predictionsArray", getContext());
            } catch (JSONException e) {
                //   Log.e(LOG_TAG, "Cannot process JSON results", e);
            }

            return descriptionList;
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        //   setImageVisibility();




                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        drawroute = true;
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address locations = address.get(0);
            if (search_pickup==true)
            {
                latitude = locations.getLatitude();
                longitude = locations.getLongitude();
            }
            else
            {

                drop_latitude = locations.getLatitude();
                drop_longitude = locations.getLongitude();

            }

            //  area = String.valueOf(location.getSubLocality());

            mOrigin_one = new LatLng(latitude,longitude);
            mDestination_one = new LatLng(drop_latitude,drop_longitude);

            drawRouteone();

           /* if(area.equalsIgnoreCase(""))
            {
                area = String.valueOf(location.getAdminArea());

            }

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
*/
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }


    public  void requestLocation(Activity caller)
    {
        List<String> permissionList = new ArrayList<String>();

        if  (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if  (ContextCompat.checkSelfPermission(caller,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionList.size()>0)
        {
            String [] permissionArray = new String[permissionList.size()];

            for (int i=0;i<permissionList.size();i++)
            {
                permissionArray[i] = permissionList.get(i);
            }

            ActivityCompat.requestPermissions(caller, permissionArray, REQUEST_CODE);
        }

        checkgpsEnabled();
    }






    public static boolean haveLocationPermission(Activity caller)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED)
        {
            permissionCheck = ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck== PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
        }
        else
        {
            Toast.makeText(caller, "Please enable permission for location", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private class getLatlongAsynctask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setTitle("Please wait route is drawing");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                getLocationFromAddress(MapsActivity.this,filter_current_address);

            } catch (Exception xc) {

            }

            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

        }
    }


    public void checkgpsEnabled()
    {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
        {
            EnableGPSAutoMatically();
        }
        else
        {
            new FusedLocationAsyncZTask().execute();
        }
    }

    private class getAllAddress extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setTitle("Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            //  progressDialog.show();

        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            autocomplete(searchString);
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //   autocomplete()

            progressDialog.dismiss();

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (latitude==0.0)
        {
            checkgpsEnabled();

        }
    }

    public void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(MapsActivity.this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            //   toast("Success");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // toast("GPS is not on");
                            try {
                                status.startResolutionForResult(MapsActivity.this, 1000);

                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //  toast("Setting change not allowed");
                            break;
                    }
                }
            });
        }
    }

    private class FusedLocationAsyncZTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setTitle("Please wait your location is fetching");
            progressDialog.setCanceledOnTouchOutside(false);
            // progressDialog.show();

        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            String data="";
            getLocationfused();
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

        }
    }

    private class getGpsOnbuttonClick extends AsyncTask<String, Integer, String> {
        String data = null;
        Location location = null;
        String geoAddressString="";

        public getGpsOnbuttonClick(Location location) {
            this.location= location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            ;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();
                        if (lat != 0.0 && lng != 0.0) {
                            System.out.println("WE GOT THE LOCATION");
                            System.out.println(lat);
                            System.out.println(lng);

                            if (current_button_click==true)
                            {


                                try {
                                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    String current_location = addresses.get(0).getAddressLine(0);
                                    geoAddressString = addresses.get(0).getAddressLine(0);
                                    pick_up_from.setText(geoAddressString);
                                    search_pick_up_from.setText(geoAddressString);
                                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(MapsActivity.this);

                                    if (search_drop_location_edt.getText().toString().trim().equalsIgnoreCase(""))
                                    {

                                    }
                                    else
                                    {
                                        mOrigin_one = new LatLng(latitude,longitude);
                                        mDestination_one = new LatLng(drop_latitude,drop_longitude);
                                        drawRouteone();
                                    }

                                    //   Toast.makeText(MapsActivity.this, geoAddressString, Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception v)
                                {

                                }

                            }



                        }

                        //     progressDialog.dismiss();
                    }
                    else
                    {
                        //  progressDialog.dismiss();
                    }
                    double lat = location.getLatitude();
                    double longintu = location.getLongitude();

                    //  Toast.makeText(MapsActivity.this, ""+String.valueOf(longintu)+"\n"+String.valueOf(lat), Toast.LENGTH_SHORT).show();

                }
            });
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            fetch_dialog.dismiss();
            current_button_click=false;

            if (search_pick_up_from.getText().toString().trim().equalsIgnoreCase(""))
            {


            }else
            if (search_drop_location_edt.getText().toString().trim().equalsIgnoreCase(""))
            {


            }
            else
            {
                mOrigin_one = new LatLng(latitude,longitude);
                mDestination_one = new LatLng(drop_latitude,drop_longitude);
                drawRouteone();
            }
            //  Toast.makeText(MapsActivity.this, "Location="+geoAddressString, Toast.LENGTH_SHORT).show();

        }
    }
}

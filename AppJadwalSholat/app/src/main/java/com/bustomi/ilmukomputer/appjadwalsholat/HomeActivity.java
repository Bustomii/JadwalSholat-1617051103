package com.bustomi.ilmukomputer.appjadwalsholat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bustomi.ilmukomputer.appjadwalsholat.Model.ItemsItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class HomeActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    String zuhur, ashar, magrib, isya, subuh, tanggal;
    List<ItemsItem> jadwal;

    Location location;
    String lokasi;
    ProgressDialog pd;
    @BindView(R.id.tv_tanngal)
    TextView tvTanngal;
    @BindView(R.id.tv_nama_daerah)
    TextView tvNamaDaerah;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.txtSubuh)
    TextView txtSubuh;
    @BindView(R.id.img_subuh)
    ImageView imgSubuh;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.txtDzuhur)
    TextView txtDzuhur;
    @BindView(R.id.img_zuhur)
    ImageView imgZuhur;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.txtAshar)
    TextView txtAshar;
    @BindView(R.id.img_ashar)
    ImageView imgAshar;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.txtMaghrib)
    TextView txtMaghrib;
    @BindView(R.id.img_magrhib)
    ImageView imgMagrhib;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.txtIsya)
    TextView txtIsya;
    @BindView(R.id.img_isya)
    ImageView imgIsya;
    @BindView(R.id.swipe_id)
    SwipeRefreshLayout swipeId;
    NestedScrollView idHomeActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_home);
        ButterKnife.bind(this);
        idHomeActivity = (NestedScrollView) findViewById(R.id.id_home_activity);

        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pd = new ProgressDialog(HomeActivity.this);
        pd.setTitle("Loading . . . ");
        pd.setMessage("Waiting . . .");
        pd.setCancelable(false);
        pd.show();

        actionLoad();


    }
    private void actionLoad() {

        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            EasyPermissions.requestPermissions(HomeActivity.this, "Butuh Permission Location", 10, perms);
        } else {
            FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("My Current location", "Lat : " + location.getLatitude() + " Long : " + location.getLongitude());
                        Geocoder gcd3 = new Geocoder(getBaseContext(), Locale.getDefault());
                        List<Address> addresses;

                        try {
                            addresses = gcd3.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses.size() > 0)

                            {
                                Log.d("Cek lokasi", "1 :" + addresses.get(0).getLocality().toString());
                                lokasi = addresses.get(0).getLocality().toString();

                                if (lokasi != null) {
                                    Log.d("location", "locatin :" + lokasi);

                                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                    Call<Items> call = apiInterface.getJadwalSholat(lokasi);
                                    call.enqueue(new Callback<Items>() {
                                        @Override
                                        public void onResponse(Call<Items> call, Response<Items> response) {
                                            Log.d("Data ", " respon" + response.body().getItems());
                                            jadwal = response.body().getItems();
                                            Log.d("respon data ", "" + new Gson().toJson(jadwal));

                                            if (jadwal != null) {
                                                zuhur = jadwal.get(0).getDhuhr();
                                                ashar = jadwal.get(0).getAsr();
                                                magrib = jadwal.get(0).getMaghrib();
                                                isya = jadwal.get(0).getIsha();
                                                subuh = jadwal.get(0).getFajr();
                                                tanggal = jadwal.get(0).getDateFor();
                                                Log.d("respon :", "" + zuhur);
                                                txtDzuhur.setText(zuhur);
                                                txtAshar.setText(ashar);
                                                txtMaghrib.setText(magrib);
                                                txtIsya.setText(isya);
                                                txtSubuh.setText(subuh);

                                                Log.d("", "lokasi" + lokasi);
                                                tvNamaDaerah.setText(lokasi);
                                                tvTanngal.setText(tanggal);
                                                swipeId.setRefreshing(false);
                                                pd.dismiss();
                                            } else {
                                                Toast.makeText(HomeActivity.this, "Error Network", Toast.LENGTH_SHORT).show();
                                                swipeId.setRefreshing(false);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Items> call, Throwable t) {
                                            Log.d("Data ", "" + t.getMessage());
                                            swipeId.setRefreshing(false);
                                            pd.dismiss();
                                        }
                                    });

                                }

                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == 10) {
            actionLoad();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

}

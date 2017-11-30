package com.example.scryptan.popoika.Views;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.scryptan.popoika.R;
import com.example.scryptan.popoika.Server.ApiUtils;
import com.example.scryptan.popoika.Server.Interfaces.CreateNewIvent;
import com.example.scryptan.popoika.Server.Interfaces.UploadImage;
import com.example.scryptan.popoika.Server.Objects.Ivent;
import com.example.scryptan.popoika.Server.Objects.toServer.toCreateNewIvent;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {

    String TAG = "AddActivity", latitude, longitude;
    List<Address> adresses;
    private Geocoder geocoder;
    EditText nameET, decriptionET, adressET;
    ProgressBar loadingPB;
    ImageView iv;
    Button pullBTN;
    private UploadImage uploadImage;
    private CreateNewIvent createNewIvent;
    private toCreateNewIvent toCreateNewIvent;
    private final int GALLERY_REQUEST = 62555;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        nameET =(EditText) findViewById(R.id.nameET);
        decriptionET =(EditText) findViewById(R.id.descriptionET);
        adressET =(EditText) findViewById(R.id.adressET);
        iv = (ImageView) findViewById(R.id.photoIV);
        loadingPB = (ProgressBar) findViewById(R.id.loadingPB);
        pullBTN = (Button) findViewById(R.id.readyBTN);
        loadingPB.setVisibility(View.GONE);
        createNewIvent = ApiUtils.createNewIvent();
        uploadImage = ApiUtils.uploadImage();
        adresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.readyBTN:
                if(adressET.getText()!=null&&nameET.getText()!=null&&decriptionET.getText()!=null)
                newIvent();
                break;
            case R.id.photoIV:
                addImage();
                return;
        }
    }

    public void newIvent(){
            pullBTN.setVisibility(View.GONE);
            loadingPB.setVisibility(View.VISIBLE);
        try {
            adresses = geocoder.getFromLocationName(adressET.getText().toString(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(adresses !=null) {
            latitude = adresses.get(0).getLatitude() + "";
            longitude = adresses.get(0).getLongitude() + "";
        }
        createNewIvent.createNewIvent(new toCreateNewIvent("",
                nameET.getText().toString(),
                decriptionET.getText().toString(),
                latitude,
                longitude)).enqueue(new Callback<Ivent>() {
            @Override
            public void onResponse(Call<Ivent> call, Response<Ivent> response) {
                if(response.isSuccessful()){
                    uploadFile(selectedImage);
                }
            }

            @Override
            public void onFailure(Call<Ivent> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Fail with network or server",Toast.LENGTH_SHORT).show();
                pullBTN.setVisibility(View.VISIBLE);
                loadingPB.setVisibility(View.GONE);
            }
        });

    }
    public void addImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    private void uploadFile(Uri fileUri) {
        File file =new File(getRealPathFromURI(getApplicationContext(), fileUri));

        // Создаем RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part используется, чтобы передать имя файла
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // Выполняем запрос
        Call<ResponseBody> call = uploadImage.upload(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Your ivent has created", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();
                    try {
                        Log.e(TAG, "onActivityResult: "+selectedImage.toString() );
                        iv.setImageURI(selectedImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}

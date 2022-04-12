package com.example.wearit.admin.addProduct;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wearit.R;
import com.example.wearit.api.ApiClient;
import com.example.wearit.api.response.Category;
import com.example.wearit.api.response.RegisterResponse;
import com.example.wearit.utils.DataHolder;
import com.example.wearit.utils.PermissionUtils;
import com.example.wearit.utils.SharedPrefUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {

    List<Category> cats = new ArrayList<>();
    private static final int TAKE_PICTURE = 2;
    private static final int PICK_PICTURE = 1;
    String currentPhotoPath;
    List<String> photoPath = new ArrayList<>();
    List<Uri> photoUris = new ArrayList<>();
    RecyclerView imageRv;
    RecyclerView catRv;
    RAdapter iAdapter;
    RAdapter cAdapter;
    Button uploadBtn;
    EditText productNameET, descriptionET, priceET, quantityET, discountPriceET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setContentView(R.layout.activity_add_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Product");
        imageRv = findViewById(R.id.imageRv);
        catRv = findViewById(R.id.catRv);
        uploadBtn = findViewById(R.id.uploadBtn);
        productNameET = findViewById(R.id.productNameET);
        descriptionET = findViewById(R.id.descriptionET);
        priceET = findViewById(R.id.priceET);
        quantityET = findViewById(R.id.quantityET);
        discountPriceET = findViewById(R.id.discountPriceET);
        setImgRV();
        setCatRv();
    }

    private void setCatRv() {
        catRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cAdapter = new RAdapter(false, null, cats, this, new RAdapter.OnItemCLick() {
            @Override
            public void onCLick(int position) {
                cats.remove(position);
                cAdapter.notifyItemRemoved(position);
            }
        });
        catRv.setAdapter(cAdapter);
    }

    private void setImgRV() {
        imageRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        iAdapter = new RAdapter(true, photoUris, null, this, new RAdapter.OnItemCLick() {
            @Override
            public void onCLick(int position) {
                photoUris.remove(position);
                photoPath.remove(position);
                iAdapter.notifyItemRemoved(position);
            }
        });
        imageRv.setAdapter(iAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addCategory(View view) {
        Intent addCategory = new Intent(this, SelectCategoryActivity.class);
        startActivity(addCategory);
    }

    public void pDateClick(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void updateImagesRV(Uri uri) {
        photoUris.add(uri);
        System.out.println("Image Urs " + photoUris.size());
        iAdapter.notifyItemInserted(photoUris.size() - 1);
    }

    public void updateCatRV(Category category) {
        cats.add(category);
        cAdapter.notifyItemInserted(cats.size() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                updateImagesRV(Uri.fromFile(f));
                photoPath.add(currentPhotoPath);
            }
        } else if (requestCode == PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                updateImagesRV(data.getData());
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(data.getData(), filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                photoPath.add(picturePath);

            }
        }
    }

    public void openCam(View view) {
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (PermissionUtils.isCameraPermissionGranted(this, "", 1)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, TAKE_PICTURE);
            }

        }

    }

    public void openGallery(View view) {
        if (PermissionUtils.isStoragePermissionGranted(this, "", PICK_PICTURE)) {
            Intent chooseFile = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(chooseFile, PICK_PICTURE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void uploadProduct(View view) {
        ProgressDialog progressDialog = ProgressDialog.show(this, "",
                "Uploading. Please wait...", false);
        String name = productNameET.getText().toString();
        String price = priceET.getText().toString();
        String discountPrice = discountPriceET.getText().toString();
        String quantity = quantityET.getText().toString();
        String desc = descriptionET.getText().toString();
        String pDate = "2020-11-12";
        String eDate = "2024-10-12";
        String key = SharedPrefUtils.getString(this, getString(R.string.api_key));
        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody rPrice = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody rDisP = RequestBody.create(MediaType.parse("text/plain"), discountPrice);
        RequestBody rQuantity = RequestBody.create(MediaType.parse("text/plain"), quantity);
        RequestBody rPD = RequestBody.create(MediaType.parse("text/plain"), pDate);
        RequestBody rED = RequestBody.create(MediaType.parse("text/plain"), eDate);
        RequestBody rDesc = RequestBody.create(MediaType.parse("text/plain"), desc);
        MultipartBody.Part[] files = new MultipartBody.Part[photoPath.size()];
        for (int i = 0; i < photoPath.size(); i++) {
            File file = new File(photoPath.get(i));
            files[i] = MultipartBody.Part.createFormData("files[]", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        String result = cats.stream()
                .map(n -> String.valueOf(n.getId()))
                .collect(Collectors.joining(",", "[", "]"));
        System.out.println(result);
        RequestBody categories = RequestBody.create(MediaType.parse("text/plain"), result);

        Call<RegisterResponse> responseCall = ApiClient.getClient().uploadProduct(
                key,
                files,
                rName,
                rPrice,
                rDesc,
                rQuantity,
                rDisP,
                categories

        );
        responseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(!response.body().getError()){
                        Toast.makeText(AddProductActivity.this, "Product Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddProductActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AddProductActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataHolder.category != null) {
            updateCatRV(DataHolder.category);
            DataHolder.category = null;
        }
    }
}
package com.musafi.ai_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.musafi.ai_app.ml.SsdMobilenetV11Metadata13;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 0;
    private ImageView main_img_image;
    private TextView main_txv_result;
    private ArrayList<String> arrLabels;
    Bitmap selectImage;
    Canvas canvas;
    private ArrayList<Detect> arrResults;
    private My_adapter my_adapter;
    private RecyclerView main_rc_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_rc_res = findViewById(R.id.main_rc_res);



        arrLabels = new ArrayList<>();

        String filename = "labels.txt";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filename)));
            String line;
            while ((line = reader.readLine()) != null){
                arrLabels.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void func_select(View view) {

        Intent photoSelect = new Intent(Intent.ACTION_PICK);
        photoSelect.setType("image/*");
        startActivityForResult(photoSelect, RESULT_LOAD_IMG);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        main_img_image = findViewById(R.id.main_img_image);

        if(resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            InputStream imageS = null;
            try {
                imageS = getContentResolver().openInputStream(imageUri);
                selectImage = BitmapFactory.decodeStream(imageS);

                Bitmap resize = Bitmap.createScaledBitmap(selectImage, 300,300,true);
                main_img_image.setImageBitmap(resize);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }else{
            Toast.makeText(this, "you havent picked image", Toast.LENGTH_LONG).show();
        }
    }

    public void func_detect(View view) {

        main_txv_result = findViewById(R.id.main_txv_result);

        Bitmap bm = ((BitmapDrawable) main_img_image.getDrawable()).getBitmap();

        Bitmap resize = Bitmap.createScaledBitmap(bm, 300,300,true);

        try {
            SsdMobilenetV11Metadata13 model = SsdMobilenetV11Metadata13.newInstance(this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 300, 300, 3}, DataType.UINT8);

            TensorImage selectImage = TensorImage.fromBitmap(resize);

            ByteBuffer byteBuffer = selectImage.getBuffer();

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            SsdMobilenetV11Metadata13.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getScoresAsTensorBuffer();
            TensorBuffer outputFeature1 = outputs.getClassesAsTensorBuffer();
            TensorBuffer outputFeature2 = outputs.getLocationsAsTensorBuffer();

            arrResults = new ArrayList<>();
            for(int i = 0; i < outputFeature1.getFloatArray().length; i ++){
                arrResults.add(new Detect().setLabel(arrLabels.get((int)outputFeature1.getFloatArray()[i]))
                        .setScore(outputFeature0.getFloatArray()[i]));
            }
            my_adapter = new My_adapter(this, arrResults);
            initList(this, my_adapter, main_rc_res, 1);

//            Log.d("pttt","sss: "+outputFeature2.getIntArray()[0]);
//            Log.d("pttt","sss: "+outputFeature2.getIntArray().length);
//
//            for(float a : outputFeature2.getFloatArray()){
//                Log.d("pttt", "rr: "+a);
//            }
//
//            int  locationsArr [][] = new int[outputFeature2.getFloatArray().length/4][4];
//            for(int i = 0; i < outputFeature2.getFloatArray().length/4; i++){
//                    for (int j = 0; j < 4; j ++){
//                        locationsArr[i][j] = (int)outputFeature2.getFloatArray()[i*j + j] * 100;
//                    }
//            }


            int max = getMax(outputFeature0.getFloatArray());
//            float [] arr = outputFeature0.getFloatArray();
//            Float [] arrf = new Float[1000];
//            for(int i = 0; i < arrf.length; i ++){
//                arrf[i] = arr[i];
//            }
//
//            Arrays.sort(arr);
//            Arrays.sort(arrf, Collections.reverseOrder());
//
//            arrResults = new ArrayList<>();
//
//            for(int i = 0; i < 100; i ++){
//                arrResults.add((arrf[i].floatValue()*100)/255);
//            }
//
//            state_adapter = new State_Adapter(this, states_list);
//            initList(this,state_adapter,main_rc_res,1);
//
//            Log.d("pttt", "ddd: "+ arrf[0]);



            canvas = new Canvas(resize);

            Paint paint = new Paint();
            //paint.setStyle(Paint.Style.FILL);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);

//            Log.d("pttt", "yyy: "+ locationsArr[max][0]);
//            Log.d("pttt", "yyy: "+ locationsArr[max][1]);
//            Log.d("pttt", "yyy: "+ locationsArr[max][2]);
//            Log.d("pttt", "yyy: "+ locationsArr[max][3]);
            Log.d("pttt", "yyy: "+ (int)(outputFeature2.getFloatArray()[max*4 + 1] * 1000));
            Log.d("pttt", "yyy: "+ (int)(outputFeature2.getFloatArray()[max*4 + 0] * 1000));
            Log.d("pttt", "yyy: "+ (int)(outputFeature2.getFloatArray()[max*4 + 3] * 1000));
            Log.d("pttt", "yyy: "+ (int)(outputFeature2.getFloatArray()[max*4 + 2] * 1000));

            my_adapter.SetOnItemClickListener(new My_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, Detect state) {
                    Rect rectangle = new Rect(
                            (int) (outputFeature2.getFloatArray()[position * 4 + 1] * 300), // Left
                            (int) (outputFeature2.getFloatArray()[position * 4 + 0] * 300), // Top
                            (int) (outputFeature2.getFloatArray()[position * 4 + 3] * 300), // Right
                            (int) (outputFeature2.getFloatArray()[position * 4 + 2] * 300)// Bottom
                    );
                    canvas.drawRect(rectangle, paint);
                    main_img_image.setImageBitmap(resize);
                }
            });

//            for(int i = 0; i < 10; i ++) {
//                Rect rectangle = new Rect(
//                        (int) (outputFeature2.getFloatArray()[i * 4 + 1] * 300), // Left
//                        (int) (outputFeature2.getFloatArray()[i * 4 + 0] * 300), // Top
//                        (int) (outputFeature2.getFloatArray()[i * 4 + 3] * 300), // Right
//                        (int) (outputFeature2.getFloatArray()[i * 4 + 2] * 300)// Bottom
//                );
//                if(outputFeature0.getFloatArray()[i] > 0.65)
//                    canvas.drawRect(rectangle, paint);
//            }

            main_txv_result.setText(""+arrLabels.get((int)outputFeature1.getFloatArray()[max]));


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {

        }


    }



    private int getMax(float [] arr){
        Log.d("pttt", "aa: "+ arr.length);
        Log.d("pttt", "aa: "+ arr[0]);
        int index = 0;
        float max = 0f;
        for(int i = 0; i < arr.length; i ++){
            if(arr[i] > max){
                max = arr[i];
                index = i;
            }
        }
        return index;
    }


    private  void initList(Context context, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, RecyclerView recyclerView, int orientation) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,orientation,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


}
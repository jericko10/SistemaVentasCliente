package com.grezzoss.sistemaventascliente;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import java.net.*;
import java.io.*;
import org.json.simple.JSONValue;
import android.os.*;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private static String dniUsuario = "";
    private static String cantidad = "";
    public static boolean flag_login=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;
        dniUsuario = recibirdni();
        cantidad=recibircantidad();

        if(currentApiVersion >=  Build.VERSION_CODES.M)
        {
            if(checkPermission())
            {
                Toast.makeText(getApplicationContext(), "Permiso concedido", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }

    }




    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if(scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permiso concedido, puedes acceder a la cámara", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permiso denegado, no tienes acceso a la cámara", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("Debes conceder permiso para utilizar la aplicación",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("CANCELAR", null)
                .create()
                .show();
    }
    public void openDialog(){
        cantidad_dialog cantidad = new cantidad_dialog();
        cantidad.show(getSupportFragmentManager(),"cantidad");
    }


    @Override
    public void handleResult(Result result) {
        final String myResult = result.getText();
        Log.d("SistemaVentasCliente", result.getText());
        Log.d("SistemaVentasCliente", result.getBarcodeFormat().toString());
//if(myResult.toString().length() != 0 ){
        openDialog();
       // enviarDatos(dniUsuario,myResult);
      //  Toast.makeText(getApplicationContext(), "Usuario:"+ dniUsuario+"-"+myResult+"-", Toast.LENGTH_LONG).show();
   // scannerView.resumeCameraPreview(ScannerActivity.this);

//}
//else    {

  //  scannerView.resumeCameraPreview(ScannerActivity.this);
//}



        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("RESULTADO");
        //builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            //@Override
            //public void onClick(DialogInterface dialog, int which) {



              //  enviarDatos(dniUsuario,myResult);
               // scannerView.resumeCameraPreview(ScannerActivity.this);

            //}
        //});
        //builder.setNeutralButton("CANCELAR", new DialogInterface.OnClickListener() {
          //  @Override
           // public void onClick(DialogInterface dialog, int which) {

            //    scannerView.resumeCameraPreview(ScannerActivity.this);
            //}
        //});
        //builder.setMessage(result.getText());
        //AlertDialog alert1 = builder.create();
       // alert1.show();
    }

    public String recibirdni(){
        Bundle datoDNI=getIntent().getExtras();
        String dniRegistro=datoDNI.getString("dni");
        return dniRegistro;
        //Toast.makeText(getApplicationContext(), "Usuario:"+ dniRegistro, Toast.LENGTH_LONG).show();

    }
    public String recibircantidad(){
        Bundle cantidad_Producto=getIntent().getExtras();
        String cantidad=cantidad_Producto.getString("cantidad");
        return cantidad;

    }



    public void enviarDatos(String dniUsuario, String codigoBarraProducto){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //Creo el Objeto JSON
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("dniUsuario", dniUsuario);
            jsonParam.put("codigoBarra", codigoBarraProducto);
          // jsonParam.put("cantidad",cantidad);

            //Creamos una lista para almacenar el JSON
            List l = new LinkedList();
            l.addAll(Arrays.asList(jsonParam));

            //Generamos el String JSON
            String jsonString = JSONValue.toJSONString(l);

            //Codificar el json a URL
            jsonString = URLEncoder.encode(jsonString, "UTF-8");


            //Generar la URL
            String url = "http://192.168.0.7/ventas/p.php";
            //Creamos un nuevo objeto URL con la url donde queremos enviar el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            //Creamos los parametros para enviar
            String urlParameters = "json=" + jsonString;
            // Enviamos los datos por POST
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Capturamos la respuesta del servidor
            int respuesta = con.getResponseCode();

            if(respuesta != 200){
                //mostrar mensaje de error ya sea por conexion o algo
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}


package com.grezzoss.sistemaventascliente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class cantidad_dialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.cantidad_producto,null);

        final EditText cantidad= view.findViewById(R.id.cantidad_Producto);

        builder.setView(view)
                .setTitle("Producto")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface,int i){
                        if(cantidad.getText().toString().length() != 0   )
                        {       Toast.makeText(getActivity(),"Producto Agregado",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(getActivity(),"Ingrese cantidad",Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }




                });



        return builder.create();

    }
   /* public void obtenerProducto(String producto){
        BuscarProducto producto = new BuscarProducto();
    return producto;
    }*/
}

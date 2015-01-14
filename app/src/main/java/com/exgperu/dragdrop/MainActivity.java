package com.exgperu.dragdrop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ImageView myImage;
    private static final String IMAGEVIEW_TAG = "El logo de Android";

    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        myImage = (ImageView)findViewById(R.id.image);
        // Asignamos la etiqueta
        myImage.setTag(IMAGEVIEW_TAG);

        // Asignamos el Listener para la data a ser movida
        myImage.setOnLongClickListener(new MyClickListener());

        findViewById(R.id.toplinear).setOnDragListener(new MyDragListener());
        findViewById(R.id.bottomlinear).setOnDragListener(new MyDragListener());

    }

    private final class MyClickListener implements OnLongClickListener {

        // Llamada cuando el item es long-clicked
        @Override
        public boolean onLongClick(View view) {
            // TODO Auto-generated method stub

            // Creado de la etiqueta del objeto
            ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

            String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag( data, //data a ser arrastrada
                    shadowBuilder, //sombra de arrastre
                    view, //Datos locales del Drag and Drop
                    0   //sin flags
            );


            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    class MyDragListener implements OnDragListener {
        Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);
        Drawable targetShape = getResources().getDrawable(R.drawable.target_shape);

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Manejadores de eventos
            switch (event.getAction()) {

                // inicio de Drag and Drop
                case DragEvent.ACTION_DRAG_STARTED:
                    // NADA
                    break;

                // se inicia el arrastre sobre el View
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(targetShape);	// cambiamos la forma del View
                    break;

                // El usuario movio el item fuera del limite del View
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);	// regresamos el view a su forma normal
                    break;

                // Liberacion de la sombra de arrastre
                case DragEvent.ACTION_DROP:
                    // Si el View es el inferior se prodece con el item arrastrado
                    if(v == findViewById(R.id.bottomlinear)) {
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        viewgroup.removeView(view);

                        // Cambio de Texto
                        TextView text = (TextView) v.findViewById(R.id.text);
                        text.setText("El item ha sido arrastrado");

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context, "No puedes Soltar la imagen aqui",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                // fin de la operacion de Drag and Drop
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);	// regresamos a la forma normal

                default:
                    break;
            }
            return true;
        }
    }
}
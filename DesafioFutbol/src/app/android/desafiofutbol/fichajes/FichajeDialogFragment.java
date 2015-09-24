package app.android.desafiofutbol.fichajes;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.android.desafiofutbol.R;
import app.android.desafiofutbol.RetreiveFeedTask;
import app.android.desafiofutbol.clases.Jugador;

//Define a DialogFragment that displays the error dialog
public class FichajeDialogFragment extends DialogFragment {

	// Global field to contain the error dialog
	private Dialog mDialog;
	private Jugador jugador = null;

	private TextView equipo;
	private TextView posicion;
	private TextView puntos;
	private TextView valor;
	private TextView propietario;
	private EditText cantidad;
	private FragmentFichajes fragment;
	private ImageView imageJugador;

	// Default constructor. Sets the dialog field to null
	public FichajeDialogFragment(Jugador jugador, FragmentFichajes fragment) {
		super();
		this.jugador = jugador;
		this.fragment = fragment;
		mDialog = null;
	}

	// Set the dialog to display
	public void setDialog(Dialog dialog) {
		mDialog = dialog;
	}

	// Return a Dialog to the DialogFragment.
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return mDialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public AlertDialog createDialogLugar(final Context contexto, final View v) {

		equipo = (TextView) v.findViewById(R.id.FichajeEquipo);
		posicion = (TextView) v.findViewById(R.id.FichajePosicion);
		puntos = (TextView) v.findViewById(R.id.FichajePuntos);
		valor = (TextView) v.findViewById(R.id.FichajeValor);
		cantidad = (EditText) v.findViewById(R.id.editTextFichajeCantidad);
		imageJugador = (ImageView) v.findViewById(R.id.imageViewFichajeJugador);
		propietario = (TextView) v.findViewById(R.id.FichajePropietario);

		equipo.setText(jugador.getEquipo());
		posicion.setText(jugador.getPosicion());
		puntos.setText(String.valueOf(jugador.getPuntos()));

		DecimalFormat formatterValor = new DecimalFormat("###,###,###,###,###�");

		valor.setText(String.valueOf(formatterValor.format(jugador.getValor())));
		propietario.setText(jugador.getPropietarioNombre().equals("null") ? ""
				: jugador.getPropietarioNombre());

		if (jugador.getMiOferta() != -1) {
			cantidad.setText(String.valueOf(jugador.getMiOferta()));
		} else {
			cantidad.setText(String.valueOf(jugador.getValor()));
		}

		RetreiveFeedTask aus = new RetreiveFeedTask(this,
				jugador.getUrlImagen(), "actualizaImagen");
		aus.execute();

		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		String buttonOferta = "Enviar Oferta";

		if (jugador.getMiOferta() != -1) {
			buttonOferta = "Cambiar Oferta";
		}

		builder.setTitle(jugador.getApodo())
				.setPositiveButton(buttonOferta,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (!cantidad.getText().toString().equals("")) {
									int cantidadOferta = Integer
											.parseInt(cantidad.getText()
													.toString());
									if (cantidadOferta < jugador.getValor()) {
										Toast.makeText(
												fragment.getActivity(),
												"El valor m�nimo para fichar a "
														+ jugador.getApodo()
														+ " es "
														+ jugador.getValor(),
												Toast.LENGTH_SHORT);
									} else {
										JSONObject json = new JSONObject();
										try {
											json.put(
													"oferta_"
															+ jugador
																	.getIdMercado(),
													cantidadOferta);
											json.put("id", jugador.getId());
											json.put("oferta", cantidadOferta);
										} catch (JSONException e) {
											e.printStackTrace();
										}
										// PostDesafioFutbol post = new
										// PostDesafioFutbol("mercado/create_ofertas",
										// fragment, json, "gestionaWS");
										// post.execute();

										dialog.cancel();
									}
								}
							}
						})
				.setNegativeButton(
						"Pagar Cl�usula ("
								+ formatterValor.format(2 * jugador.getValor())
								+ ")", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								JSONObject json = new JSONObject();
								try {
									json.put("id", jugador.getId());
									json.put("oferta", 0);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								// PostDesafioFutbol post = new
								// PostDesafioFutbol("mercado/"+FichajeDialogFragment.this.jugador.getIdMercado()+"/clausula_pagar",
								// fragment, json, "gestionaWS");
								// post.execute();
								dialog.cancel();
							}
						})
				.setNeutralButton("Eliminar Oferta",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								JSONObject json = new JSONObject();
								try {
									json.put(
											"delflag" + jugador.getIdMercado(),
											1);
									// json.put("oferta_"+jugador.getIdMercado(),cantidadOferta);
									json.put("id", jugador.getId());
									json.put("oferta", -1);
								} catch (JSONException e) {
									e.printStackTrace();
								}
								// PostDesafioFutbol post = new
								// PostDesafioFutbol("mercado/create_ofertas",
								// fragment, json, "gestionaWS");
								// post.execute();
								dialog.cancel();
							}
						}).setView(v);
		return builder.create();
	}

	public void actualizaImagen(final Bitmap bm) {
		fragment.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				imageJugador.setImageBitmap(bm);
			}
		});
	}
}
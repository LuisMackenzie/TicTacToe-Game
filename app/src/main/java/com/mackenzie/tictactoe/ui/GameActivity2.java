package com.mackenzie.tictactoe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.mackenzie.tictactoe.R;
import com.mackenzie.tictactoe.app.Constantes;
import com.mackenzie.tictactoe.databinding.ActivityGame2Binding;
import com.mackenzie.tictactoe.databinding.ContentGameBinding;
import com.mackenzie.tictactoe.model.Jugada;
import com.mackenzie.tictactoe.model.User;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class GameActivity2 extends AppCompatActivity {

    private ActivityGame2Binding binding;
    private ContentGameBinding binding2;
    private List<ImageView> casillas;
    private List<LottieAnimationView> animCasillas;
    // TextView tvPlayer1, tvPlayer2;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String uid, jugadaId = "", playerOneName = "", playerTwoName = "", ganadorId = "";
    private Jugada jugada;
    private ListenerRegistration listenerJugada = null;
    private FirebaseUser firebaseUser;
    private String nombreJugador;
    private User userPlayer1, userPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGame2Binding.inflate(getLayoutInflater());
        binding2 = ContentGameBinding.inflate(getLayoutInflater());
        setContentView(binding2.getRoot());
        // setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // binding2.textViewPlayer1.setTextColor(getResources().getColor(R.color.purple_500));

        initViews();
        initGame();
        // updatePlayersUI();

    }

    private void initGame() {
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        uid = firebaseUser.getUid();

        Bundle extras = getIntent().getExtras();
        jugadaId = extras.getString(Constantes.EXTRA_JUGADA_ID);
    }

    private void initViews() {

        casillas = new ArrayList<>();
        animCasillas = new ArrayList<>();

        animCasillas.add(binding2.ivLottie00);
        animCasillas.add(binding2.ivLottie01);
        animCasillas.add(binding2.ivLottie02);
        animCasillas.add(binding2.ivLottie03);
        animCasillas.add(binding2.ivLottie04);
        animCasillas.add(binding2.ivLottie05);
        animCasillas.add(binding2.ivLottie06);
        animCasillas.add(binding2.ivLottie07);
        animCasillas.add(binding2.ivLottie08);


        casillas.add(binding2.imageView0);
        casillas.add(binding2.imageView1);
        casillas.add(binding2.imageView2);
        casillas.add(binding2.imageView3);
        casillas.add(binding2.imageView4);
        casillas.add(binding2.imageView5);
        casillas.add(binding2.imageView6);
        casillas.add(binding2.imageView7);
        casillas.add(binding2.imageView8);
    }

    private void jugadaListener() {
        listenerJugada = db.collection("jugadas")
                .document(jugadaId)
                .addSnapshotListener(GameActivity2.this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            Toast.makeText(GameActivity2.this, "Error al obtener los datos de la jugadas", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String source = snapshot != null
                                && snapshot.getMetadata().hasPendingWrites() ? "Local" : "Server";

                        if(snapshot.exists() && source.equals("Server")) {
                            // Parseando DocumentSnapshot > Jugada
                            jugada = snapshot.toObject(Jugada.class);
                            if(playerOneName.isEmpty() || playerTwoName.isEmpty()) {
                                // obtener los nombres de usuario de la jugada
                                getPlayerNames();
                            }

                            updateUI();
                        }

                        updatePlayersUI();
                    }
                });
    }

    private void updatePlayersUI() {
        if(jugada.isTurnoP1()) {
            binding2.textViewPlayer1.setTextColor(getResources().getColor(R.color.purple_500));
            binding2.textViewPlayer2.setTextColor(getResources().getColor(R.color.teal_700));
        } else {
            binding2.textViewPlayer1.setTextColor(getResources().getColor(R.color.teal_700));
            binding2.textViewPlayer2.setTextColor(getResources().getColor(R.color.teal_200));
        }

        if(!jugada.getGanadorId().isEmpty()) {
            ganadorId = jugada.getGanadorId();
            mostrarDialogoGameOver();
        }

    }

    private void updateUI() {
        for(int i=0; i<9; i++) {
            int casilla = jugada.getCeldas().get(i);
            ImageView ivCasillaActual = casillas.get(i);
            LottieAnimationView lavCasillaActual = animCasillas.get(i);

            if(casilla == 0) {
                ivCasillaActual.setImageResource(R.drawable.ic_empty_square);
            } else if(casilla == 1) {
                // lavCasillaActual.setRepeatCount(0);
                lavCasillaActual.setAnimation("green_check.json");
                //lavCasillaActual.playAnimation();
                // ivCasillaActual.setImageResource(R.drawable.ic_player_one);
            } else {
                // lavCasillaActual.setRepeatCount(0);
                lavCasillaActual.setAnimation("red_cross.json");
                // lavCasillaActual.playAnimation();
                // ivCasillaActual.setImageResource(R.drawable.ic_player_two);
            }
        }
    }

    private void getPlayerNames() {
        // Obtener el nombre del player 1
        db.collection("users")
                .document(jugada.getJugador1())
                .get()
                .addOnSuccessListener(GameActivity2.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userPlayer1 = documentSnapshot.toObject(User.class);
                        playerOneName = documentSnapshot.get("name").toString();
                        binding2.textViewPlayer1.setText(playerOneName);

                        if(jugada.getJugador1().equals(uid)) {
                            nombreJugador = playerOneName;
                        }
                        Log.e("TAG NAME PLAYER 1", "name 1 " + playerOneName);
                    }
                });

        // Obtener el nombre del player 2
        db.collection("users")
                .document(jugada.getJugador2())
                .get()
                .addOnSuccessListener(GameActivity2.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userPlayer2 = documentSnapshot.toObject(User.class);
                        playerTwoName = documentSnapshot.get("name").toString();
                        binding2.textViewPlayer2.setText(playerTwoName);
                        // Toast.makeText(GameActivity2.this, "Player 2 name" + playerTwoName, Toast.LENGTH_SHORT).show();
                        // Log.e("TAG NAME PLAYER 2", "name 2 " + playerTwoName);
                        if(jugada.getJugador2().equals(uid)) {
                            nombreJugador = playerTwoName;
                        }
                    }
                });
    }

    public void casillaSeleccionada(View view) {
        if(!jugada.getGanadorId().isEmpty()) {
            Toast.makeText(this, "La partida ha terminado", Toast.LENGTH_SHORT).show();
        } else {
            if(jugada.isTurnoP1() && jugada.getJugador1().equals(uid)) {
                // Está jugando el jugador 1
                actualizarJugada(view.getTag().toString());
            } else if(!jugada.isTurnoP1() && jugada.getJugador2().equals(uid)) {
                // Está jugando el jugador 2
                actualizarJugada(view.getTag().toString());
            } else {
                Toast.makeText(this, "No es tu turno aún", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarJugada(String numeroCasilla) {
        int posicionCasilla = Integer.parseInt(numeroCasilla);

        if(jugada.getCeldas().get(posicionCasilla) != 0) {
            Toast.makeText(this, "Seleccione una casilla libre", Toast.LENGTH_SHORT).show();
        } else {
            if (jugada.isTurnoP1()) {
                animCasillas.get(posicionCasilla).setRepeatCount(0);
                animCasillas.get(posicionCasilla).setAnimation("green_check.json");
                animCasillas.get(posicionCasilla).playAnimation();
                // casillas.get(posicionCasilla).setImageResource(R.drawable.ic_player_one);
                jugada.getCeldas().set(posicionCasilla, 1);
            } else {
                animCasillas.get(posicionCasilla).setRepeatCount(0);
                animCasillas.get(posicionCasilla).setAnimation("red_cross.json");
                animCasillas.get(posicionCasilla).playAnimation();
                // casillas.get(posicionCasilla).setImageResource(R.drawable.ic_player_two);
                jugada.getCeldas().set(posicionCasilla, 2);
            }

            if(existeSolucion()) {
                jugada.setGanadorId(uid);
                Toast.makeText(this, "Hay solución", Toast.LENGTH_SHORT).show();
            } else if(existeEmpate()) {
                jugada.setGanadorId("EMPATE");
                Toast.makeText(this, "Hay empate", Toast.LENGTH_SHORT).show();
            } else {
                cambioTurno();
            }
            // Actualizar en Firestore los datos de la jugada
            db.collection("jugadas")
                    .document(jugadaId)
                    .set(jugada)
                    .addOnSuccessListener(GameActivity2.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Aqui faltan cosas

                        }
                    }).addOnFailureListener(GameActivity2.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("ERROR", "Error al guardar la jugada");
                }
            });
        }

    }

    private void cambioTurno() {
        // Cambio de turno
        jugada.setTurnoP1(!jugada.isTurnoP1());
    }

    private boolean existeEmpate() {
        boolean existe = false;

        // Empate
        boolean hayCasillaLibre = false;
        for(int i=0; i<9; i++) {
            if(jugada.getCeldas().get(i) == 0) {
                hayCasillaLibre = true;
                break;
            }
        }

        if(!hayCasillaLibre)
            existe = true;

        return existe;
    }

    private boolean existeSolucion() {
        boolean existe = false;

        List<Integer> selectedCells = jugada.getCeldas();
        if(selectedCells.get(0) == selectedCells.get(1)
                && selectedCells.get(1) == selectedCells.get(2)
                && selectedCells.get(2) != 0) { // 0 - 1 - 2
            existe = true;
        } else if(selectedCells.get(3) == selectedCells.get(4)
                && selectedCells.get(4) == selectedCells.get(5)
                && selectedCells.get(5) != 0) { // 3 - 4 - 5
            existe = true;
        } else if(selectedCells.get(6) == selectedCells.get(7)
                && selectedCells.get(7) == selectedCells.get(8)
                && selectedCells.get(8) != 0) { // 6 - 7 - 8
            existe = true;
        } else if(selectedCells.get(0) == selectedCells.get(3)
                && selectedCells.get(3) == selectedCells.get(6)
                && selectedCells.get(6) != 0) { // 0 - 3 - 6
            existe = true;
        } else if(selectedCells.get(1) == selectedCells.get(4)
                && selectedCells.get(4) == selectedCells.get(7)
                && selectedCells.get(7) != 0) { // 1 - 4 - 7
            existe = true;
        } else if(selectedCells.get(2) == selectedCells.get(5)
                && selectedCells.get(5) == selectedCells.get(8)
                && selectedCells.get(8) != 0) { // 2 - 5 - 8
            existe = true;
        } else if(selectedCells.get(0) == selectedCells.get(4)
                && selectedCells.get(4) == selectedCells.get(8)
                && selectedCells.get(8) != 0) { // 0 - 4 - 8
            existe = true;
        } else if(selectedCells.get(2) == selectedCells.get(4)
                && selectedCells.get(4) == selectedCells.get(6)
                && selectedCells.get(6) != 0) { // 2 - 4 - 6
            existe = true;
        }

        return existe;

    }

    public void mostrarDialogoGameOver() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialogo_game_over, null);
        // Obtenemos las referencias a los View components de nuestro layout
        TextView tvPuntos = v.findViewById(R.id.textViewPuntos);
        TextView tvInformacion = v.findViewById(R.id.textViewInformacion);
        LottieAnimationView gameOverAnimation = v.findViewById(R.id.animation_view);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Game Over");
        builder.setCancelable(false);
        builder.setView(v);

        if(ganadorId.equals("EMPATE")) {
            actualizarPuntuacion(1);
            tvInformacion.setText("¡" + nombreJugador + " has empatado!");
            tvPuntos.setText("+1 punto");
        } else if(ganadorId.equals(uid)) {
            actualizarPuntuacion(3);
            tvInformacion.setText("¡" + nombreJugador + " has ganado!");
            tvPuntos.setText("+3 puntos");
        } else {
            actualizarPuntuacion(0);
            tvInformacion.setText("¡" + nombreJugador + " has perdido!");
            tvPuntos.setText("0 puntos");
            gameOverAnimation.setAnimation("thumbs_down_animation.json");
        }

        gameOverAnimation.playAnimation();

        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void actualizarPuntuacion(int puntosConseguidos) {
        User jugadorActualizar = null;
        if(nombreJugador.equals(userPlayer1.getName())) {
            userPlayer1.setPoints(userPlayer1.getPoints() + puntosConseguidos);
            userPlayer1.setPartidas(userPlayer1.getPartidas() + 1);
            jugadorActualizar = userPlayer1;
        } else {
            userPlayer2.setPoints((userPlayer2.getPoints() + puntosConseguidos));
            userPlayer2.setPartidas(userPlayer2.getPartidas() + 1);
            jugadorActualizar = userPlayer2;
        }

        db.collection("users")
                .document(uid)
                .set(jugadorActualizar)
                .addOnSuccessListener(GameActivity2.this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(GameActivity2.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        jugadaListener();
    }

    @Override
    protected void onStop() {
        if(listenerJugada != null) {
            listenerJugada.remove();
        }
        super.onStop();
    }

}
package com.mackenzie.tictactoe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.mackenzie.tictactoe.app.Constantes;
import com.mackenzie.tictactoe.databinding.ActivityFindPlayerBinding;
import com.mackenzie.tictactoe.model.Jugada;

import javax.annotation.Nullable;

public class FindPlayerActivity extends AppCompatActivity {

    private ActivityFindPlayerBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String uid, jugadaId = "";
    private ListenerRegistration listenerRegistration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initProgressBar();
        firebaseInit();
        events();

    }

    private void buscarJugadaLibre() {
        binding.textViewLoading.setText("Buscando una jugada libre...");
        binding.animationView.playAnimation();

        db.collection("jugadas")
                .whereEqualTo("jugadorDosId", "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() == 0) {
                            // No existen partidas libres, crear una nueva
                            crearNuevaJugada();
                        } else {
                            boolean encontrado = false;

                            for(DocumentSnapshot docJugada : task.getResult().getDocuments()) {
                                if(!docJugada.get("jugadorUnoId").equals(uid)) {
                                    encontrado = true;

                                    jugadaId = docJugada.getId();
                                    Jugada jugada = docJugada.toObject(Jugada.class);
                                    jugada.setJugador2(uid);

                                    db.collection("jugadas")
                                            .document(jugadaId)
                                            .set(jugada)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    binding.textViewLoading.setText("¡Jugada libre encontrada! Comienza la partida");
                                                    binding.animationView.setRepeatCount(0);
                                                    binding.animationView.setAnimation("checked_animation.json");
                                                    binding.animationView.playAnimation();

                                                    final Handler handler = new Handler();
                                                    final Runnable r = new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startGame();
                                                        }
                                                    };

                                                    handler.postDelayed(r, 1500);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            changeVisibility(true);
                                            Toast.makeText(FindPlayerActivity.this, "Hubo algún error al entrar en la jugada", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;
                                }

                                if(!encontrado) crearNuevaJugada();

                            }


                        }
                    }
                });

    }

    private void crearNuevaJugada() {
        binding.textViewLoading.setText("Creando una jugada nueva...");
        Jugada nuevaJugada = new Jugada(uid);

        db.collection("jugadas")
                .add(nuevaJugada)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        jugadaId = documentReference.getId();
                        // Tenemos creada la jugada, debemos esperar a otro jugador
                        esperarJugador();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                changeVisibility(true);
                Toast.makeText(FindPlayerActivity.this, "Error al crear la nueva jugada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void esperarJugador() {
        binding.textViewLoading.setText("Esperando a otro jugador...");

        listenerRegistration = db.collection("jugadas")
                .document(jugadaId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if(!documentSnapshot.get("jugadorDosId").equals("")) {
                            binding.textViewLoading.setText("¡Ya ha llegado un jugador! Comienza la partida");
                            binding.animationView.setRepeatCount(0);
                            binding.animationView.setAnimation("checked_animation.json");
                            binding.animationView.playAnimation();

                            final Handler handler = new Handler();
                            final Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    startGame();
                                }
                            };

                            handler.postDelayed(r, 1500);

                        }
                    }
                });
    }

    private void startGame() {
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }
        Intent i = new Intent(FindPlayerActivity.this, GameActivity2.class);
        i.putExtra(Constantes.EXTRA_JUGADA_ID, jugadaId);
        startActivity(i);
        jugadaId = "";
    }

    private void firebaseInit() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        uid = mAuth.getUid();
    }

    private void events() {
        binding.buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeVisibility(false);
                buscarJugadaLibre();
            }
        });

        binding.buttonRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initProgressBar() {
        binding.progressBarJugadas.setIndeterminate(true);
        binding.textViewLoading.setText("Cargando...");
        changeVisibility(true);

    }

    private void changeVisibility(boolean showMenu) {
        binding.layoutprogressBar.setVisibility(showMenu ? View.GONE : View.VISIBLE);
        binding.menuJuego.setVisibility(showMenu ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(jugadaId != "") {
            changeVisibility(false);
            esperarJugador();
        } else {
            changeVisibility(true);
        }
    }

    @Override
    protected void onStop() {
        if(listenerRegistration != null) {
            listenerRegistration.remove();
        }

        if(jugadaId != "") {
            db.collection("jugadas")
                    .document(jugadaId)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            jugadaId = "";
                        }
                    });
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        finish();
    }
}
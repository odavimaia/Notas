package com.example.notas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notas.R;
import com.example.notas.dao.NotaDAO;
import com.example.notas.model.Nota;
import com.example.notas.ui.recycler.adapter.ListaNotasAdapter;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String CHAVE_NOTA = "nota";
    public static final int CODIGO_REQUISICAO_INSERE_NOTA = 1;
    public static final int CODIGO_RESULTADO_NOTA_CRIADA = 2;
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasAsNotas();

        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(this::iniciaFormularioNotaActivity);
    }

    private List<Nota> pegaTodasAsNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        listaNotas.setOnClickListener(view -> {

        });
    }

    private void iniciaFormularioNotaActivity(View view) {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (ehResultadoComNota(requestCode, resultCode)) {
            assert data != null;
            if (temNota(data)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                new NotaDAO().insere(notaRecebida);
                adapter.adiciona(notaRecebida);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean ehResultadoComNota(int requestCode, int resultCode) {
        return verificaSeEhCodigoRequisicaoInsereNota(requestCode) &&
                verificaSeEhCodigoResultadoNotaCriada(resultCode);
    }

    private boolean temNota(@NonNull Intent data) {
        return data.hasExtra(ListaNotasActivity.CHAVE_NOTA);
    }

    private boolean verificaSeEhCodigoResultadoNotaCriada(int resultCode) {
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean verificaSeEhCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }
}

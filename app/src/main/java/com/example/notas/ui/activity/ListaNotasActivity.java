package com.example.notas.ui.activity;

import static com.example.notas.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static com.example.notas.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static com.example.notas.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static com.example.notas.ui.activity.NotaActivityConstantes.CODIGO_RESULTADO_NOTA_CRIADA;
import static com.example.notas.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final String CHAVE_NOTA = "nota";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        List<Nota> todasNotas = pegaTodasAsNotas();

        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(this::vaiParaFormularioNotaActivityInsere);
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
        adapter.setOnItemClickListener(this::vaiParaFormularioNotaActivityAltera);
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this,
                FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

    private void vaiParaFormularioNotaActivityInsere(View view) {
        Intent iniciaFormularioNota =
                new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        insereNota(requestCode, resultCode, data);
        alteraNota(requestCode, resultCode, data);
    }

    private void insereNota(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoInsereNota(requestCode, resultCode)) {
            assert data != null;
            if (temNota(data)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                new NotaDAO().insere(notaRecebida);
                adapter.adiciona(notaRecebida);
            }
        }
    }

    private void alteraNota(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoAlteraNota(requestCode, resultCode)) {
            assert data != null;
            if (temNota(data)) {
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, int resultCode) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                verificaSeEhCodigoResultadoNotaCriada(resultCode);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, int resultCode) {
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

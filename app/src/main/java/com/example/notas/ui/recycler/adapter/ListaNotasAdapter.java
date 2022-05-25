package com.example.notas.ui.recycler.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notas.R;
import com.example.notas.model.Nota;
import com.example.notas.ui.recycler.adapter.listener.OnItemClickListener;

import java.util.List;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;
    private static OnItemClickListener onItemClickListener;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        ListaNotasAdapter.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = criaView(parent);
        return new NotaViewHolder(viewCriada);
    }

    private View criaView(@NonNull ViewGroup parent) {
        return LayoutInflater.from(context)
                .inflate(R.layout.item_nota, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaNotasAdapter.NotaViewHolder holder, int posicao) {
        Nota nota = notas.get(posicao);
        holder.vincula(nota);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
        notifyDataSetChanged();
    }

    static class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private Nota nota;

        public NotaViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);

            itemView.setOnClickListener(view -> onItemClickListener.onItemClick(nota,
                    getAdapterPosition()));
        }

        public void vincula(Nota nota){
            this.nota = nota;
            preencheCampos(nota);
        }

        private void preencheCampos(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void adiciona(Nota nota){
        notas.add(nota);
        notifyDataSetChanged();
    }
}

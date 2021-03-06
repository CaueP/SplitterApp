package com.caue.splitter.model;

import android.util.Log;

import com.caue.splitter.MainPageActivity;
import com.caue.splitter.controller.ServiceGenerator;
import com.caue.splitter.model.services.CheckinClient;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Caue
 * @version 1.0
 * @date 14/04/2017
 */

public class Checkin implements Serializable {
    @Expose
    @SerializedName("usuario")
    private Usuario usuario;

    @Expose
    @SerializedName("mesa")
    private Mesa mesa = new Mesa();

    @Expose
    @SerializedName("comanda")
    private Comanda comanda = new Comanda();

    @Expose
    @SerializedName("isPrimeiroUsuario")
    private boolean isPrimeiroUsuario;

    @Expose
    @SerializedName("isSucesso")
    private boolean isSucesso;

    @Expose
    @SerializedName("error")
    private String error;

    // getters and setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isPrimeiroUsuario() {
        return isPrimeiroUsuario;
    }

    public void setPrimeiroUsuario(boolean primeiroUsuario) {
        isPrimeiroUsuario = primeiroUsuario;
    }

    public boolean isSucesso() {
        return isSucesso;
    }

    public void setSucesso(boolean sucesso) {
        isSucesso = sucesso;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }


    /**
     * Método para realizar o check-in através da API
     *
     * @param activity activity que invocou o método para ser invocada aquando receber a resposta
     */
    public void realizarCheckin(final MainPageActivity activity) {

        // criação do serviço para realizar operações na conta do usuário
        CheckinClient service = ServiceGenerator.createService(CheckinClient.class);
        Log.d("carregarUsuario", "Carregando usuario");
        // Service para baixar objeto com o usuário
        Call<Checkin> checkinCall = service.realizarCheckin(this);

        // callback ao receber a resposta da API
        Callback<Checkin> realizarCheckinCallback = new Callback<Checkin>() {

            @Override
            public void onResponse(Call<Checkin> call, Response<Checkin> response) {
                Checkin checkinResposta;
                Log.d("onResponse", "entered in onResponse - Checkin");
                if (response.isSuccessful()) {
                    Log.d("onResponse", "isSuccessful");
                    Log.d("onResponse", "Body: " + response.body());
                    checkinResposta = response.body();
                    //Log.d("resposta Checkin", "isSucesso: " + Boolean.toString(resposta.isSucesso()));
                    activity.responseCheckinReceived(checkinResposta);
                } else {
                    Log.d("onResponse", "isNOTSuccessful (code: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Checkin> call, Throwable t) {
                Log.d("onFailure", "Ocorreu um erro ao chamar a API - Checkin");
            }
        };

        // call
        checkinCall.enqueue(realizarCheckinCallback);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Mesa: ")
                .append(mesa != null ? getMesa().getNrMesa() : "null")
                .append(" ")
                .append("codEstabelecimento: ")
                .append(mesa != null && mesa.getCodEstabelecimento() != null ? getMesa().getCodEstabelecimento() : "null")
                .append("Comanda: ")
                .append(comanda != null ? comanda.getCodComanda() : "null")
                .append(" ")
                .append("QR Ocupado")
                .append(mesa != null && mesa.getQrCodeOcupado() != null ? mesa.getQrCodeOcupado() : "null")
                .append(" ")
                .append("Responsavel: ")
                .append(mesa != null && mesa.getUsuarioResponsavel() != null ? mesa.getUsuarioResponsavel() : "null");
        return stringBuilder.toString();
    }


}


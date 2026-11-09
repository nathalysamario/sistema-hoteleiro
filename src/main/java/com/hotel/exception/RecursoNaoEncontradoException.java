package com.hotel.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String recurso, String id) {
        super(recurso + " com id " + id + " não encontrado(a).");
    }
}

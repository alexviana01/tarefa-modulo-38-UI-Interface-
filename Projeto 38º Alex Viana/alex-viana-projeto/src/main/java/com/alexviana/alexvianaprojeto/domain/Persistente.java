package com.alexviana.alexvianaprojeto.domain;

import java.io.Serializable;

public interface Persistente extends Serializable {
    Long getId();
    void setId(Long id);
}
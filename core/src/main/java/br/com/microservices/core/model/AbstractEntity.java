package br.com.microservices.core.model;

import java.io.Serializable;

public interface AbstractEntity extends Serializable {
    Long getId();
}

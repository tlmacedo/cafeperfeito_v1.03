package br.com.tlmacedo.cafeperfeito.model.vo;

import br.com.tlmacedo.cafeperfeito.model.vo.enums.WebTipo;
import javafx.beans.property.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "EmailHomePage")
@Table(name = "email_home_page")
public class EmailHomePage implements Serializable {
    private static final long serialVersionUID = 1L;

    private LongProperty id = new SimpleLongProperty();
    private StringProperty descricao = new SimpleStringProperty();

    private IntegerProperty tipo = new SimpleIntegerProperty();

    public EmailHomePage() {
    }

    public EmailHomePage(String descricao, WebTipo tipo) {
        this.descricao = new SimpleStringProperty(descricao);
        this.tipo = new SimpleIntegerProperty(tipo.getCod());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    @Column(length = 120, nullable = false)
    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    @Column(length = 2, nullable = false)
    public WebTipo getTipo() {
        return WebTipo.toEnum(tipo.get());
    }

    public IntegerProperty tipoProperty() {
        return tipo;
    }

    public void setTipo(WebTipo tipo) {
        this.tipo.set(tipo.getCod());
    }

    @Override
    public String toString() {
        return descricaoProperty().get();
    }
}

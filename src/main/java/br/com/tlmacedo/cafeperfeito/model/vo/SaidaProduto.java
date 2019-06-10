package br.com.tlmacedo.cafeperfeito.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-04-08
 * Time: 11:05
 */

@Entity(name = "SaidaProduto")
@Table(name = "saida_produto")
public class SaidaProduto {
    private static final long serialVersionUID = 1L;

    private LongProperty id = new SimpleLongProperty(0);

    private Empresa cliente = new Empresa();
    private Usuario vendedor = new Usuario();
    private SaidaProdutoNfe nfe = new SaidaProdutoNfe();
    private ObjectProperty<LocalDateTime> dataCadastro = new SimpleObjectProperty<>(LocalDateTime.now());
    private ContasAReceber contasAReceber;


    private List<SaidaProdutoProduto> saidaProdutoProdutoList = new ArrayList<>();

    public SaidaProduto() {
    }

    public SaidaProduto(Empresa cliente, Usuario vendedor, LocalDateTime dataCadastro) {
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.dataCadastro = new SimpleObjectProperty<>(dataCadastro);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id.get();
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cliente_id", foreignKey = @ForeignKey(name = "fk_saida_produto_empresa"))
    public Empresa getCliente() {
        return cliente;
    }

    public void setCliente(Empresa cliente) {
        this.cliente = cliente;
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vendedor_id", foreignKey = @ForeignKey(name = "fk_saida_produto_vendedor"))
    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario usuarioCadastro) {
        this.vendedor = usuarioCadastro;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "saidaProduto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    public SaidaProdutoNfe getNfe() {
        return nfe;
    }

    public void setNfe(SaidaProdutoNfe nfe) {
        this.nfe = nfe;
    }

    @CreationTimestamp
    @Column(nullable = false)
    public LocalDateTime getDataCadastro() {
        return dataCadastro.get();
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro.set(dataCadastro);
    }

    public ObjectProperty<LocalDateTime> dataCadastroProperty() {
        return dataCadastro;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<SaidaProdutoProduto> getSaidaProdutoProdutoList() {
        return saidaProdutoProdutoList;
    }

    public void setSaidaProdutoProdutoList(List<SaidaProdutoProduto> saidaProdutoProdutoList) {
        this.saidaProdutoProdutoList = saidaProdutoProdutoList;
    }

    @JsonIgnore
    @OneToOne(mappedBy = "saidaProduto", cascade = CascadeType.ALL, orphanRemoval = true)
    public ContasAReceber getContasAReceber() {
        return contasAReceber;
    }

    public void setContasAReceber(ContasAReceber contasAReceber) {
        this.contasAReceber = contasAReceber;
    }

    @Override
    public String toString() {
        return "SaidaProduto{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", vendedor=" + vendedor +
                ", dataCadastro=" + dataCadastro +
                ", saidaProdutoProdutoList=" + saidaProdutoProdutoList +
                '}';
    }
}

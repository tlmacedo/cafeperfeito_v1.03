package br.com.tlmacedo.cafeperfeito.service.FormatCell;

import br.com.tlmacedo.cafeperfeito.model.vo.Endereco;
import br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-03-20
 * Time: 15:24
 */

public class FormatListCell_Endereco extends ListCell<Endereco> {
    private ImageView imageView = new ImageView();

    public FormatListCell_Endereco() {
    }

    @Override
    protected void updateItem(Endereco item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            switch (item.getTipo()) {
                case PRINCIPAL:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_principal_dp14.png"));
                    break;
                case ENTREGA:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_entrega_dp14.png"));
                    break;
                case COBRANCA:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_cobranca_dp14.png"));
                    break;
                case CORRESPONDENCIA:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_correspondencia_dp14.png"));
                    break;
                case RESIDENCIAL:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_residencial_dp14.png"));
                    break;
                case RECADO:
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_end_recado_dp14.png"));
                    break;
            }
            setGraphic(imageView);
            setText(item.toString());
        }
    }

}

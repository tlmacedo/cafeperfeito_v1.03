package br.com.tlmacedo.cafeperfeito.service.FormatCell;

import br.com.tlmacedo.cafeperfeito.model.vo.EmailHomePage;
import br.com.tlmacedo.cafeperfeito.model.vo.enums.WebTipo;
import br.com.tlmacedo.cafeperfeito.service.ServiceVariaveisSistema;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by IntelliJ IDEA.
 * User: thiagomacedo
 * Date: 2019-03-20
 * Time: 15:30
 */

public class FormatListCell_EmailHomePage extends ListCell<EmailHomePage> {
    private ImageView imageView = new ImageView();

    public FormatListCell_EmailHomePage() {
    }

    @Override
    protected void updateItem(EmailHomePage item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            try {
                if (item.getTipo().equals(WebTipo.EMAIL))
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_email@_dp14.png"));
                else
                    imageView.setImage(new Image(ServiceVariaveisSistema.PATHICONE + "ic_webhome_dp14.png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setGraphic(imageView);
            setText(item.toString());
        }
    }

}

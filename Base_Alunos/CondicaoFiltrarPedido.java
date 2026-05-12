import java.util.function.Predicate;

public class CondicaoFiltrarPedido implements Predicate<Pedido> {

    @Override
    public boolean test(ItemDePedido item1, ItemDePedido item2) {
       return item1.equals(item2);
       
    }

}


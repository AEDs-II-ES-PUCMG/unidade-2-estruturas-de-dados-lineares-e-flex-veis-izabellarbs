import java.util.Comparator;

public class CriterioDeBuscaPorDescricao implements Comparator<ItemDePedido> {

    @Override
    public int compare(ItemDePedido item1, ItemDePedido item2) {

        if (item1.getProduto().descricao.equals(item2.getProduto().descricao)) {
            return 0;
        } else {
            return 1;
        }
    }
}

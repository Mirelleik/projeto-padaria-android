import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    // Lista de produtos para simular um banco de dados
    private val productList = mutableListOf(
        Product(1, "Pão Francês", 50),
        Product(2, "Bolo de Chocolate", 10),
        Product(3, "Pão de Queijo (unid.)", 100)
    )

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProducts)
        val fab: FloatingActionButton = findViewById(R.id.fabAddProduct)

        // Configura o Adapter e o RecyclerView
        productAdapter = ProductAdapter(productList) { product ->
            // Ação ao clicar em um produto (simular venda)
            showSellDialog(product)
        }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Ação ao clicar no botão de adicionar
        fab.setOnClickListener {
            showAddProductDialog()
        }
    }

    private fun showSellDialog(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Vender ${product.name}")
            .setMessage("Vender uma unidade deste produto?")
            .setPositiveButton("Vender") { _, _ ->
                if (product.stock > 0) {
                    product.stock-- // Diminui o estoque
                    productAdapter.notifyDataSetChanged() // Atualiza a lista na tela
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_product, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editTextProductName)
        val stockEditText = dialogView.findViewById<EditText>(R.id.editTextProductStock)

        AlertDialog.Builder(this)
            .setTitle("Adicionar Novo Produto")
            .setView(dialogView)
            .setPositiveButton("Adicionar") { _, _ ->
                val name = nameEditText.text.toString()
                val stock = stockEditText.text.toString().toIntOrNull() ?: 0
                if (name.isNotBlank()) {
                    val newId = (productList.maxOfOrNull { it.id } ?: 0) + 1
                    productList.add(Product(newId, name, stock))
                    productAdapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

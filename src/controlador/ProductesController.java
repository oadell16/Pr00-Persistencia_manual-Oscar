package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeSet;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import model.Pack;
import model.Product;
import model.ProductesDAO;

public class ProductesController{

	//Objecte per gestionar la persistència de les dades
	private ProductesDAO productesDAO;
	//Objecte per gestionar el objecte actual
	private Product product = null;
	//indicador de nou registre
	private boolean nouRegistre = false;
	//objecte per afegir les files de la taula
	private ObservableList<Product> productesData;

	//Elements gràfics de la UI
	@FXML
	private AnchorPane anchorPane;
	private Stage ventana;
	@FXML private TextField idTextField;
	@FXML private TextField nomTextField;
	@FXML private TextField priceTextField;
	@FXML private TextField stockTextField;
	@FXML private DatePicker initialDateTextField;
	@FXML private DatePicker finalDateTextField;
	@FXML private CheckBox packCheckBox;
	@FXML private TextArea idProductsTextArea;
	@FXML private TextField dtoTextField;
	
	@FXML private TableView<Product> productesTable;
	@FXML private TableColumn<Product, Integer> idColumn;
	@FXML private TableColumn<Product, String> nomColumn;

	private ValidationSupport vs;

	// Crear conexion a la base de datos
	public void setConexionBD(Connection conexionBD) {	
		//Crear objecte DAO de persones
		productesDAO = new ProductesDAO(conexionBD);
		
		// Aprofitar per carregar la taula de persones (no ho podem fer al initialize() perque encara no tenim l'objecte conexionBD)
		// https://code.makery.ch/es/library/javafx-tutorial/part2/
		productesData = FXCollections.observableList(productesDAO.getProductesList());
		productesTable.setItems(productesData);
	}

	/**
	 * Inicialitza la classe. JAVA l'executa automàticament després de carregar el fitxer fxml
	 */
	@FXML private void initialize() {
		//Obrir el fitxer de persones
		products = new ProductesDAO();
		products.load();

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
		vs = new ValidationSupport();
		vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(nomTextField, true, Validator.createEmptyValidator("Nom obligatori"));
		vs.registerValidator(priceTextField, true, Validator.createEmptyValidator("Preu obligatori"));
		vs.registerValidator(stockTextField, true, Validator.createEmptyValidator("Stock obligatori"));
		vs.registerValidator(initialDateTextField, true, Validator.createEmptyValidator("Fecha inicial del producto obligatori"));
		vs.registerValidator(finalDateTextField, true, Validator.createEmptyValidator("Fecha final del producto obligatori"));

        //https://howtodoinjava.com/regex/java-regex-validate-email-address/
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onCheckPack(ActionEvent e) throws IOException{
		if (checkPack.isSelected()) {
			idProductsTextArea.setDisable(false);
			dtoTextField.setDisable(false);
			vs.registerValidator(idProductsTextArea, true, Validator.createEmptyValidator("ids dels productes obligatori"));
			vs.registerValidator(dtoTextField, true, Validator.createEmptyValidator("Descompte obligatori"));
		}else{
			vs = new ValidationSupport();
			vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
			vs.registerValidator(nomTextField, true, Validator.createEmptyValidator("Nom obligatori"));
			vs.registerValidator(priceTextField, true, Validator.createEmptyValidator("Preu obligatori"));
			vs.registerValidator(stockTextField, true, Validator.createEmptyValidator("Stock obligatori"));
			vs.registerValidator(initialDateTextField, true, Validator.createEmptyValidator("Fecha inicial del producto obligatori"));
			vs.registerValidator(finalDateTextField, true, Validator.createEmptyValidator("Fecha final del producto obligatori"));

			idProductsTextArea.setDisable(true);
			dtoTextField.setDisable(true);
		}
		
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
				
				if (products.search(Integer.parseInt(idTextField.getText())) instanceof Product) {
					//Comprovar si existeix la persona indicada en el control idTextField
					Product product = products.search(Integer.parseInt(idTextField.getText()));
					
					//posar els valors per modificarlos
					nomTextField.setText(product.getName());
					priceTextField.setText(product.getSalePrice().toString());
					stockTextField.setText(product.getStock().toString());
					stockTextField.setDisable(true);
					initialDateTextField.setValue(product.getInitialCatalogDate());
					finalDateTextField.setValue(product.getEndCatalogDate());
					
				}else if(products.search(Integer.parseInt(idTextField.getText())) instanceof Pack){
					Pack pack = (Pack)products.search(Integer.parseInt(idTextField.getText()));

					nomTextField.setText(pack.getName());
					priceTextField.setText(pack.getSalePrice().toString());
					stockTextField.setText(pack.getStock().toString());
					stockTextField.setDisable(true);
					initialDateTextField.setValue(pack.getInitialCatalogDate());
					finalDateTextField.setValue(pack.getEndCatalogDate());

					idProductsTextArea.setText(pack.getIds().toString());
					dtoTextField.setText(pack.getPercentageDiscount().toString());

				}else{ 
					//nou registre
					nomTextField.setText("");
					priceTextField.setText("");
					stockTextField.setText("");
					stockTextField.setDisable(false);
					initialDateTextField.setValue(null);
					finalDateTextField.setValue(null);
					idProductsTextArea.setText("");
					dtoTextField.setText("");
				}
		}
		
	}
	 
	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides				
		if(isDatosValidos()){
			if (products.search(Integer.parseInt(idTextField.getText())) instanceof Product) {
				Product product = new Product(Integer.parseInt(idTextField.getText()), nomTextField.getText(),Integer.parseInt(priceTextField.getText()),
				Integer.parseInt(stockTextField.getText()),initialDateTextField.getValue(),finalDateTextField.getValue());
				
				
				products.add(product);
				limpiarFormulario();
				System.out.println(products.getMap());
			}else{
				TreeSet<Product> ids = new TreeSet<Product>();
				String[] idsProducts = idProductsTextArea.getText().split(",");

				for (String id : idsProducts) {
					if (products.search(Integer.parseInt(id))!=null) {
						ids.add(products.search(Integer.parseInt(id)));
					}
				}

				if (ids!=null) {
					Pack pack = new Pack(
					Integer.parseInt(idTextField.getText()), 
					nomTextField.getText(),
					Integer.parseInt(priceTextField.getText()),
					Integer.parseInt(stockTextField.getText()),
					initialDateTextField.getValue(),
					finalDateTextField.getValue(),
					ids,
					Integer.parseInt(dtoTextField.getText())
					);

					products.add(pack);


				}
				
				limpiarFormulario();
				System.out.println(products.getMap().toString());
			}
		}
	}

	@FXML private void onActionEliminar(ActionEvent e) throws IOException {

		if(isDatosValidos()){
			if(products.delete(Integer.parseInt(idTextField.getText()))==null){
				limpiarFormulario();
				System.out.println(products.getMap());
			}
		}
	}

	@FXML private void onActionSortir(ActionEvent e) throws IOException {

		sortir();

		ventana.close();
	}

	public void sortir(){
		products.save();
		System.out.println(products.getMap());
	}

	private boolean isDatosValidos() {

		//Comprovar si totes les dades són vàlides
		if (vs.isInvalid()) {
			String errors = vs.getValidationResult().getMessages().toString();
			// Mostrar finestra amb els errors
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(ventana);
			alert.setTitle("Camps incorrectes");
			alert.setHeaderText("Corregeix els camps incorrectes");
			alert.setContentText(errors);
			alert.showAndWait();
		
			return false;
		}

		return true;

	}

	private void limpiarFormulario(){
		idTextField.setText("");
		nomTextField.setText("");
		priceTextField.setText("");
		stockTextField.setText("");
		initialDateTextField.setValue(null);
		finalDateTextField.setValue(null);
		idProductsTextArea.setText("");
		dtoTextField.setText("");
	}
}

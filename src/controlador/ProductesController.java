package controlador;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import model.Pack;
import model.Productes;
import model.ProductesDAO;

public class ProductesController{

	//Objecte per gestionar la persistència de les dades
	private ProductesDAO productesDAO;
	//Objectes per gestionar el objecte actual
	private Productes product = null;
	private Pack pack = null;
	//indicador de nou registre
	private boolean nouRegistre = false;

	//objecte per afegir les files de la taula
	private ObservableList<Productes> productesData;

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
	
	@FXML private TableView<Productes> productesTable;
	@FXML private TableColumn<Productes, Integer> idColumn;
	@FXML private TableColumn<Productes, String> nomColumn;

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
		idColumn.setCellValueFactory(new PropertyValueFactory<Productes,Integer>("id"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<Productes,String>("name"));

		// Quan l'usuari canvia de linia executem el métode mostrarPersona
		productesTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> mostrar(newValue));

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
		if (packCheckBox.isSelected()) {
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

			if (productesDAO.isPack(Integer.parseInt(idTextField.getText()))) {
				packCheckBox.setSelected(true);
			}else{
				packCheckBox.setSelected(false);
				dtoTextField.setText("");
				idProductsTextArea.setText("");
			}

			if (!isCheckedCheckBoxPack() ) {
				product = productesDAO.findProducte(Integer.parseInt(idTextField.getText()));
				mostrarProducte(product);
			}else{
				pack = productesDAO.findPack(Integer.parseInt(idTextField.getText()));
				mostrarPack(pack);
			}
			//Comprovar si existeix la persona indicada en el control idTextField
			
			//seleccionar la fila de la taula asociada al codi introduit
			productesTable.getSelectionModel().select(product);
			productesTable.refresh();
		}
		
	}
	
	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		//verificar si les dades són vàlides
		if(isDatosValidos()){

			//Si no esta checkeada la checkbox pack creara un producto
			if(!productesDAO.isPack(Integer.parseInt(idTextField.getText()))){
				if(nouRegistre){
					product = new Productes(
						Integer.parseInt(idTextField.getText()), 
						nomTextField.getText(), 
						Float.parseFloat(priceTextField.getText()),
						Integer.parseInt(stockTextField.getText()), 
						initialDateTextField.getValue(),
						finalDateTextField.getValue()
					);
					productesData.add(product);
					
				}else{
					//modificació registre existent
					product = productesTable.getSelectionModel().getSelectedItem();

					product.setName(nomTextField.getText());
					product.setPrice(Float.parseFloat(priceTextField.getText()));
					product.setStock(Integer.parseInt(stockTextField.getText()));
					product.setInitialCatalogDate(initialDateTextField.getValue());
					product.setEndCatalogDate(initialDateTextField.getValue());
				}
				productesDAO.saveProduct(product);

			}else{
				List<Integer> listIds = new ArrayList<Integer>();
				String[] arrayIds= idProductsTextArea.getText().split(",");

				for (int i = 0; i < arrayIds.length; i++) {
					listIds.add(Integer.parseInt(arrayIds[i]));
				}
				System.out.println("lisids");
				System.out.println(listIds);

				System.out.println(Integer.parseInt(dtoTextField.getText()));
				
				pack = new Pack(
					Integer.parseInt(idTextField.getText()), 
					nomTextField.getText(), 
					Float.parseFloat(priceTextField.getText()),
					Integer.parseInt(stockTextField.getText()), 
					initialDateTextField.getValue(),
					finalDateTextField.getValue(),
					listIds,
					Integer.parseInt(dtoTextField.getText())
				);

				productesDAO.savePack(pack);
			}
		
		}

		limpiarFormulario();
		productesTable.refresh();
	}

	@FXML private void onActionEliminar(ActionEvent e) throws IOException {
		if(isDatosValidos()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Vol esborrar el producte " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if (!productesDAO.isPack(Integer.parseInt(idTextField.getText()))) {
					if(productesDAO.deleteProducte(Integer.parseInt(idTextField.getText()))){ 
						productesData.remove(productesTable.getSelectionModel().getSelectedIndex());
	
						limpiarFormulario();
						// productesDAO.showAll();
					}
				} else {
					if(productesDAO.deletePack(Integer.parseInt(idTextField.getText()))){ 
						productesData.remove(productesTable.getSelectionModel().getSelectedIndex());
	
						limpiarFormulario();
						// productesDAO.showAll();
					}
				}
				
			}
		}
	}

	@FXML private void onActionSortir(ActionEvent e) throws IOException {
		sortir();
		//tancar el formulari
		((BorderPane)anchorPane.getParent()).setCenter(null);
	}

	public void sortir(){
		// productesDAO.showAll();
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

	private void mostrar(Productes producte){	
		if (productesDAO.isPack(producte.getId())) {
			mostrarPack(productesDAO.findPack(producte.getId()));
		}else{
			mostrarProducte(producte);
		}
		
	}

	private void mostrarProducte(Productes producte) {
		packCheckBox.setSelected(false);
		enableDisablePackFields(false);

		this.isCheckedCheckBoxPack();
		if(producte != null){ 
			//llegir persona (posar els valors als controls per modificar-los)
			nouRegistre = false;
			idTextField.setText(String.valueOf(producte.getId()));
			nomTextField.setText(producte.getName());
			priceTextField.setText(producte.getPrice().toString());
			stockTextField.setText(producte.getStock().toString());
			initialDateTextField.setValue(producte.getInitialCatalogDate());
			finalDateTextField.setValue(producte.getEndCatalogDate());
		}else{ 
			//nou registre
			nouRegistre = true;
			//idTextField.setText(""); no hem de netejar la PK perquè l'usuari ha posat un valor
			nomTextField.setText("");
			priceTextField.setText("");
			stockTextField.setText("");
			initialDateTextField.setValue(null);
			finalDateTextField.setValue(null);
		}
	}
	
	private void mostrarPack(Pack pack) {
		packCheckBox.setSelected(true);
		enableDisablePackFields(true);
		if(pack != null){ 
			//llegir persona (posar els valors als controls per modificar-los)
			nouRegistre = false;
			idTextField.setText(String.valueOf(pack.getId()));
			nomTextField.setText(pack.getName());
			priceTextField.setText(pack.getPrice().toString());
			stockTextField.setText(pack.getStock().toString());
			initialDateTextField.setValue(pack.getInitialCatalogDate());
			finalDateTextField.setValue(pack.getEndCatalogDate());
			
			idProductsTextArea.setText(productesDAO.getListproductsString(pack.getIds()));
			dtoTextField.setText(pack.getPercentageDiscount().toString());
		}else{ 
			//nou registre
			nouRegistre = true;
			//idTextField.setText(""); no hem de netejar la PK perquè l'usuari ha posat un valor
			nomTextField.setText("");
			priceTextField.setText("");
			stockTextField.setText("");
			initialDateTextField.setValue(null);
			finalDateTextField.setValue(null);
			idProductsTextArea.setText("");
			dtoTextField.setText("");
		}
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

	private void enableDisablePackFields(boolean enabled){
		if (enabled) {
			dtoTextField.setDisable(false);
			idProductsTextArea.setDisable(false);
		}else{
			dtoTextField.setDisable(true);
			idProductsTextArea.setDisable(true);
			dtoTextField.setText("");
			idProductsTextArea.setText("");
		}
	}
	
	private boolean isCheckedCheckBoxPack(){
		if (packCheckBox.isSelected()) return true;
		else return false;
	}
}

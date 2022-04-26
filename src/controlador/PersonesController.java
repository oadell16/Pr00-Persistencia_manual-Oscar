package controlador;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import model.Persona;
import model.PersonesDAO;

public class PersonesController{
	//Objecte per gestionar la persistència de les dades
	private PersonesDAO personesDAO;
	//Objecte per gestionar el objecte actual
	private Persona persona = null;
	//indicador de nou registre
	private boolean nouRegistre = false;
	//objecte per afegir les files de la taula
	private ObservableList<Persona> personesData;

	//Elements gràfics de la UI
	@FXML
	private AnchorPane anchorPane;
	private Stage ventana;
	@FXML private TextField idTextField;
	@FXML private TextField dniTextField;
	@FXML private TextField nomTextField;
	@FXML private TextField cognomsTextField;
	@FXML private DatePicker fechaNacimientoDatePicker;
	@FXML private TextField emailTextField;
	@FXML private TextField telefonTextField;
	//direccion
	@FXML private TextField localidadTextField;
	@FXML private TextField provinciaTextField;
	@FXML private TextField codPostalTextField;
	@FXML private TextField domicilioTextField;

	@FXML private TableView<Persona> personesTable;
	@FXML private TableColumn<Persona, Integer> idColumn;
	@FXML private TableColumn<Persona, String> nomColumn;

	private ValidationSupport vs;

	// Crear conexion a la base de datos
	public void setConexionBD(Connection conexionBD) {	
		//Crear objecte DAO de persones
		personesDAO = new PersonesDAO(conexionBD);
		
		// Aprofitar per carregar la taula de persones (no ho podem fer al initialize() perque encara no tenim l'objecte conexionBD)
		// https://code.makery.ch/es/library/javafx-tutorial/part2/
		personesData = FXCollections.observableList(personesDAO.getPersonesList());
		personesTable.setItems(personesData);
	}

	/**
	 * Inicialitza la classe. JAVA l'executa automàticament després de carregar el fitxer fxml
	 */
	@FXML private void initialize() {
		idColumn.setCellValueFactory(new PropertyValueFactory<Persona,Integer>("id"));
		nomColumn.setCellValueFactory(new PropertyValueFactory<Persona,String>("nom"));

		// Quan l'usuari canvia de linia executem el métode mostrarPersona
		personesTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> mostrarPersona(newValue));

		//Validació dades
		//https://github.com/controlsfx/controlsfx/issues/1148
		//produeix error si no posem a les VM arguments això: --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
		vs = new ValidationSupport();
		vs.registerValidator(idTextField, true, Validator.createEmptyValidator("ID obligatori"));
		vs.registerValidator(dniTextField, true, Validator.createEmptyValidator("Dni obligatori"));
		vs.registerValidator(nomTextField, true, Validator.createEmptyValidator("Nom obligatori"));
		vs.registerValidator(cognomsTextField, true, Validator.createEmptyValidator("Cognoms obligatori"));
		vs.registerValidator(fechaNacimientoDatePicker, true, Validator.createEmptyValidator("Data de naixement obligatori"));
        //https://howtodoinjava.com/regex/java-regex-validate-email-address/
        vs.registerValidator(emailTextField, Validator.createRegexValidator("E-mail incorrecte", "^(.+)@(.+)$", Severity.ERROR));
        vs.registerValidator(telefonTextField, Validator.createRegexValidator("Telèfon ha de ser un número", "\\d*", Severity.ERROR));

		//direccion
		vs.registerValidator(localidadTextField, true, Validator.createEmptyValidator("Localitat es obligatori"));
		vs.registerValidator(provinciaTextField, true, Validator.createEmptyValidator("Provincia es obligatori"));
		vs.registerValidator(codPostalTextField, true, Validator.createEmptyValidator("Codi postal es obligatori"));
		vs.registerValidator(domicilioTextField, true, Validator.createEmptyValidator("Domicili es obligatori"));
	}

	public Stage getVentana() {
		return ventana;
	}

	public void setVentana(Stage ventana) {
		this.ventana = ventana;
	}

	@FXML private void onKeyPressedId(KeyEvent e) throws IOException {
		if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB){
			//Comprovar si existeix la persona indicada en el control idTextField
			persona = personesDAO.find(Integer.parseInt(idTextField.getText()));
			mostrarPersona(persona);
			//seleccionar la fila de la taula asociada al codi introduit
			personesTable.getSelectionModel().select(persona);
			personesTable.refresh();
		}
	}
	 
	@FXML private void onActionGuardar(ActionEvent e) throws IOException {
		
		//verificar si les dades són vàlides
		if(isDatosValidos()){
			if(nouRegistre){
				persona = new Persona(
					Integer.parseInt(idTextField.getText()), 
					dniTextField.getText(), 
					nomTextField.getText(), 
					cognomsTextField.getText(),
					fechaNacimientoDatePicker.getValue(),
					emailTextField.getText(), 
					personesDAO.getArrayTelefonos(telefonTextField.getText()),
					null
				);

				personesData.add(persona);
			}else{
				//modificació registre existent
				persona = personesTable.getSelectionModel().getSelectedItem();
				Array telefonos = personesDAO.getArrayTelefonos(telefonTextField.getText());

				persona.setDni(dniTextField.getText()); 
				persona.setNom(nomTextField.getText()); 
				persona.setApellidos(cognomsTextField.getText()); 
				persona.setFecha_nacimiento(fechaNacimientoDatePicker.getValue()); 
				persona.setEmail(emailTextField.getText()); 
				persona.setTelefonos(telefonos);
				persona.setDireccion(null);
			}
			personesDAO.save(persona);
			limpiarFormulario();

			personesTable.refresh();

			personesDAO.showAll();
		}
	}

	@FXML private void onActionEliminar(ActionEvent e) throws IOException {
		if(isDatosValidos()){
			// Mostrar missatge confirmació
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Vol esborrar la persona " + idTextField.getText() + "?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				if(personesDAO.delete(Integer.parseInt(idTextField.getText()))){ 
					personesData.remove(personesTable.getSelectionModel().getSelectedIndex());

					limpiarFormulario();
					personesDAO.showAll();
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
		personesDAO.showAll();
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

	private void mostrarPersona(Persona persona) {
		if(persona != null){ 
			//llegir persona (posar els valors als controls per modificar-los)
			nouRegistre = false;
			idTextField.setText(String.valueOf(persona.getId()));
			idTextField.setText(persona.getDni());
			nomTextField.setText(persona.getNom());
			cognomsTextField.setText(persona.getApellidos());
			fechaNacimientoDatePicker.setValue(persona.getFecha_nacimiento());
			emailTextField.setText(persona.getEmail());
			telefonTextField.setText(persona.getTelefonos().toString());
			//direccion
			provinciaTextField.setText("");
			localidadTextField.setText("");
			codPostalTextField.setText("");
			domicilioTextField.setText("");
		}else{ 
			//nou registre
			nouRegistre = true;
			//idTextField.setText(""); no hem de netejar la PK perquè l'usuari ha posat un valor
			dniTextField.setText("");
			nomTextField.setText("");
			cognomsTextField.setText("");
			fechaNacimientoDatePicker.setValue(null);
			emailTextField.setText("");
			telefonTextField.setText("");
			//direccion
			provinciaTextField.setText("");
			localidadTextField.setText("");
			codPostalTextField.setText("");
			domicilioTextField.setText("");
		}
	}
	
	private void limpiarFormulario(){
		idTextField.setText("");
		dniTextField.setText("");
		nomTextField.setText("");
		cognomsTextField.setText("");
		fechaNacimientoDatePicker.setValue(null);
		emailTextField.setText("");
		telefonTextField.setText("");
		//direccion
		provinciaTextField.setText("");
		localidadTextField.setText("");
		codPostalTextField.setText("");
		domicilioTextField.setText("");
	}
}
